package com.example.prj2be.service;

import com.example.prj2be.domain.MyPlaylist;
import com.example.prj2be.domain.Song;
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

    public List<Song> getFavoriteListName(String listId) {
        return mapper.selectByFavoriteListName(listId);
    }


    public boolean deleteByFavoriteList(String songId, String playlistId) {
        return mapper.deleteByFavoriteList(songId, playlistId)==1;
    }
}
