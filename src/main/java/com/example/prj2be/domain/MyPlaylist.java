package com.example.prj2be.domain;

import lombok.Data;

@Data
public class MyPlaylist {
    private String id;
    private String listId;
    private String listName;
    private Boolean isLike;
    private Integer songId;
    private String playlistId;
    private Integer countLike;
    private Integer totalSongCount;
}
