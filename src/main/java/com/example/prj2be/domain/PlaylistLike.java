package com.example.prj2be.domain;

import lombok.Data;

@Data
public class PlaylistLike {
    private Integer id;
    private String memberId;
    private String likelistId;
    private Boolean isLike;
    private Integer listId;
}
