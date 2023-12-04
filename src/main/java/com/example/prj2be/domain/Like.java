package com.example.prj2be.domain;

import lombok.Data;

@Data
public class Like {
    private Integer id;
    private String memberId;
    private Integer listId;
    private Boolean isLike;
}
