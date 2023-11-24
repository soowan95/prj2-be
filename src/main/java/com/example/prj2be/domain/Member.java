package com.example.prj2be.domain;

import lombok.Data;

@Data
public class Member {
    private String id;
    private String password;
    private String nickName;
    private String email;
}
