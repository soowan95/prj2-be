package com.example.prj2be;

import com.example.prj2be.domain.Song;
import lombok.Data;

import java.util.List;

public class AllSongDTO {

  private static List<Song> songList;

  public static List<Song> getSongList() {
    return songList;
  }

  public static void setSongList(List<Song> songLists) {
    songList = songLists;
  }
}
