package com.example.prj2be.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class Song {

  private Integer id;
  private String title;
  private String lyric;
  private String album;
  private String mood;
  private String artistName;
  private String artistGroup;
  private LocalDateTime release;
  private Integer titleHangulCode;
  private Integer artistHangulCode;
  private Integer lyricHangulCode;
  private String genre;
  private String songUrl;
  private String requestTitle;
  private String requestArtist;
  private Integer indexForPlay;

  private MultipartFile file;

}
