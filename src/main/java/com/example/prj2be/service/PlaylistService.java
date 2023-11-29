package com.example.prj2be.service;

import com.example.prj2be.domain.Member;
import com.example.prj2be.domain.MyPlaylist;
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


    public boolean validate(MyPlaylist playlist) {
        if (playlist == null) {
            return false;
        }
        return true;
    }

    public boolean add(MyPlaylist playlist, Member login) {
        playlist.setListId(login.getId());
        return mapper.insert(playlist)==1;
    }

    public List<MyPlaylist> getMyPlayList(String listId) {
        return mapper.getMyPlayList(listId);
    }

    public List<MyPlaylist> list(String listId, String listName) {
        return mapper.myListByListName(listId, listName);
    }
}
