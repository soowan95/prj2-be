package com.example.prj2be.service;

import com.example.prj2be.domain.Like;
import com.example.prj2be.domain.Member;
import com.example.prj2be.domain.MyPlaylist;
import com.example.prj2be.mapper.LikeMapper;
import com.example.prj2be.mapper.myPlaylistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PlaylistService {

    private final myPlaylistMapper mapper;
    private final LikeMapper likeMapper;


    public boolean validate(MyPlaylist playlist) {
        if (playlist == null) {
            return false;
        }
        return true;
    }
  
    public List<MyPlaylist> getMyPlayList(String id) {
        List<MyPlaylist> playList = mapper.getMyPlayList(id);
        for (MyPlaylist list : playList) {
            list.setCountLike(likeMapper.countByBoardId(list.getListId()));
            //첫페이지 //countByBoardId는 라이크가 몇개인지
//            list.setSongId(likeMapper.countBySongId(list.getSongId()));
            list.setIsLike(likeMapper.isLike(list.getId(), list.getListId()) == 1);
            //첫페이지에서 내가 좋아요 누른 거를 볼 수 있게 list의 id랑 list의 listId가 값이 1이면 ture 0이면 false
            list.setTotalSongCount(mapper.chartlist(Integer.parseInt(list.getListId())).size());
        }
        return playList;
    }
  
    public List<Map<String,Object>> getRecommended() {
        return mapper.selectRecommended();
    }

    public List<Map<String, Object>> getFavoriteList(String id) {
        return mapper.selectFavoriteList(id);
    }
}
