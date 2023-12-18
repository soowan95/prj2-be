package com.example.prj2be.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
  private Integer artistId;
  private String artistName;
  private String artistGroup;
  private String artistFileUrl;
  private LocalDate release;
  private Integer titleHangulCode;
  private Integer artistHangulCode;
  private Integer lyricHangulCode;
  private String genre;
  private String songUrl;
  private String requestTitle;
  private String requestArtist;
  private Integer indexForPlay;
  private String member;
}
