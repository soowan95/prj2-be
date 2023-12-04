package com.example.prj2be.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id;
    private Integer songId;
    private String memberId;
    private String comment;
    private LocalDateTime inserted;
}
