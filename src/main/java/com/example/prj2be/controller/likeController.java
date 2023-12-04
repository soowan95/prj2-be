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
                                                    @RequestBody Map<String, Object> map ) {
        // map으로 로그인 정보를 담았다
        Like like = new Like();

        like.setBoardId(Integer.parseInt(map.get("listId").toString()));

        if(login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(service.update(like, login));
    }
    @GetMapping("board/{boardId}")
//  --> 좋아요 눌렀을 때
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable Integer boardId,
            @SessionAttribute(value = "login", required = false) Member login) {

        return ResponseEntity.ok(service.get(boardId,login));
    }
}

