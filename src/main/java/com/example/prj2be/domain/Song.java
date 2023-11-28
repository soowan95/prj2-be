package com.example.prj2be.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Song {

  private Integer id;
  private String title;
  private String lyric;
  private String album;
  private String mood;
  private String artistName;
  private Date release;
  private String genre;
  private Integer titleCode;
  private Integer artistCode;
  private Integer lyricCode;
}
