package com.example.prj2be.domain;

import lombok.Data;

@Data
public class Like {
    private Integer id;
    private String memberId;
    private Integer boardId;
    private Boolean isLike;
}
