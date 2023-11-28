package com.example.prj2be.Controller;


import com.example.prj2be.Domain.Member;
import com.example.prj2be.Domain.MyPlaylist;
import com.example.prj2be.Service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myList")
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
}
