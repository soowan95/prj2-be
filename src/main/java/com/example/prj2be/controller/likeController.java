package com.example.prj2be.controller;

import com.example.prj2be.domain.Like;
import com.example.prj2be.domain.Member;
import com.example.prj2be.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class likeController {


    private final LikeService service;

    @PostMapping("/like")
    public ResponseEntity <Map<String, Object>>like(@SessionAttribute(value = "login", required = false)Member login,
                                                    @RequestBody Like like ) {

        if(login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(service.update(like));
    }
    @GetMapping("board/{likelistId}")
//  --> 좋아요 눌렀을 때
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable String likelistId,
            @SessionAttribute(value = "login", required = false) Member login) {

        return ResponseEntity.ok(service.get(likelistId,login));
    }
}

