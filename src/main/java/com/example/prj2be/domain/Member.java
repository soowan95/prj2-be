package com.example.prj2be.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Member {
    private String id;
    private String password;
    private String nickName;
    private String email;
    private LocalDateTime inserted;
    private String securityQuestion;
    private String securityAnswer;
    private Boolean online;
    private String profilePhoto;
    private List<Auth> auth;
  
    public boolean isAdmin() {
        if (auth != null) {
            return auth.stream()
                    .map(a -> a.getName())
                    .anyMatch(n -> n.equals("admin"));
        }

        return false;
    }
}
