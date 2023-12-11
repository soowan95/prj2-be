package com.example.prj2be.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberPlayList {
    private String id;
    private String memberId;
    private String listName;
    private String name;
    private String pictureUrl;
    private String likedMember;
    private Integer likelistId;
    private String title;
    private String lyric;
    private String album;
    private LocalDate release;
    private String genre;
    private String mood;
    private Integer songs;
    private Integer count;
    private String group;
    private String picture;
}
