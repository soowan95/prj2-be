package com.example.prj2be.controller;

import com.example.prj2be.domain.Comment;
import com.example.prj2be.domain.Member;
import com.example.prj2be.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment,
                                      @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.validate(comment)) {
            if (service.add(comment, login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public List<Comment> list(@RequestParam("id") String songId) {
        return service.list(songId);
    }

    @DeleteMapping("{id}")
    public ResponseEntity remove(@PathVariable String id,
                       @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!service.hasAccess(id, login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Integer commentId = Integer.parseInt(id);

            if (service.remove(commentId)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (NumberFormatException e) {
            // id를 Integer로 변환할 수 없는 경우 처리
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("edit")
    public ResponseEntity update(@RequestBody Comment comment,
                                 @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.hasAccess(comment.getId(), login)) {
            if (!service.updateValidate(comment)) {
                return ResponseEntity.badRequest().build();
            }

            if (service.update(comment)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


    }

}
