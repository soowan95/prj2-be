package com.example.prj2be.controller;


import com.example.prj2be.domain.Member;
import com.example.prj2be.domain.MyPlaylist;
import com.example.prj2be.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myList")
public class PlaylistController {

    private final PlaylistService service;

    @GetMapping("get")
    public List<MyPlaylist> getList(String id) {
        return service.getMyPlayList(id);
    }

    @GetMapping("recommended")
    public List<Map<String,Object>> recommendedList() {
        return service.getRecommended();
    }

    @GetMapping("favorite")
    public List<Map<String,Object>> favoriteList(String id) {
        return service.getFavoriteList(id);
    }
}
