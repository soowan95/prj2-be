package com.example.prj2be.service;

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
            //countByBoardId는 라이크가 몇개인지
        return mapper.getMyPlayList(id);
    }

    public List<MyPlaylist> getAllList(Member login) {
        List<MyPlaylist> allList = mapper.getAllList();

        if (login != null) {
            allList.forEach((e) -> {
                Integer i = likeMapper.isLike(login.getId(), e.getId());
                e.setIsLike(i == 1);
                // 회원아이디랑 차트아이디랑 같은지 아닌지
            });
        }

        return playList;
        // 모르니까 수완이에게 물어보자
    }
  
    public List<Map<String,Object>> getRecommended() {
        return mapper.selectRecommended();
    }

    public List<Map<String, Object>> getFavoriteList(String id) {
        return mapper.selectFavoriteList(id);
    }
}
