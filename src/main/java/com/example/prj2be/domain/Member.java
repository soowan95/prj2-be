package com.example.prj2be.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    private String id;
    private String password;
    private String nickName;
    private String email;
    private LocalDateTime inserted;
}
