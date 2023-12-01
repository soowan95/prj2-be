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
@RequestMapping("/main/api/myList")
public class PlaylistController {

    private final PlaylistService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody MyPlaylist playlist,
                              @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.validate(playlist)) {
            if (service.add(playlist, login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    @GetMapping("get")
    public List<MyPlaylist> getList(String listId) {
        System.out.println(listId);

        return service.getMyPlayList(listId);
    }


    @GetMapping("recommended")
    public List<Map<String,Object>> recommendedList() {
        return service.getRecommended();
    }
}
