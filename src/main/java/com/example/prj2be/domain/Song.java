package com.example.prj2be.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Song {

  private Integer id;
  private String title;
  private String lyric;
  private String album;
  private List<String> mood;
  private String artistName;
  private Date release;
  private List<String> genre;
  private Integer titleCode;
  private Integer artistCode;
  private Integer lyricCode;
}
