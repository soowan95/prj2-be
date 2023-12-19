package com.example.prj2be.domain;

import lombok.Data;

@Data
public class SongLike {
    private Integer id;
    private String memberId;
    private Integer likedSong;
}
