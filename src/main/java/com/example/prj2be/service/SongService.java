package com.example.prj2be.service;

import com.example.prj2be.AllSongDTO;
import com.example.prj2be.domain.Song;
import com.example.prj2be.mapper.SongMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SongService {

  private final SongMapper songMapper;

  public List<Song> getSongLimit100() {
    return songMapper.getSongLimit100();
  }

  public List<Map<String, Object>> getMood() {
    return songMapper.getMood();
  }

  public List<Map<String, Object>> getGenre() {
    return songMapper.getGenre();
  }

  public List<Song> getByFilter(List<String> genreList, List<String> moodList) {
    genreList = genreList.stream().filter(a -> !a.isEmpty()).toList();
    moodList = moodList.stream().filter(a -> !a.isEmpty()).toList();

    return songMapper.getByFilter(genreList, moodList);
  }

  // 필터 추가하여 검색
  public List<Song> getByCategoryAndKeyword(String category, String keyword, List<String> genreList, List<String> moodList) {
    genreList = genreList.stream().filter(a -> !a.isEmpty()).toList();
    moodList = moodList.stream().filter(a -> !a.isEmpty()).toList();

    return songMapper.getByCategoryAndKeyword(category, "%" + keyword + "%", genreList, moodList);
  }

  // 비슷한 곡 랜덤으로 5개 추출
  public List<Song> getByGenreAndMood(String genre, String mood, Integer id) {
    List<Song> list = songMapper.getByGenreAndMood(genre, mood, id);
    List<Song> newList = new ArrayList<>();
    List<Integer> numberList = new ArrayList<>();

    while (numberList.size() < Math.min(list.size(), 5)) {
      int number = (int) (Math.random() * list.size());
      if (!numberList.contains(number)) numberList.add(number);
    }

    for (int i = 0; i < list.size(); i++) {
      if (numberList.contains(i)) newList.add(list.get(i));
    }

    return newList;
  }

  public Integer getCode(String category, Song s) {
    if (category.equals("가수")) return s.getArtistCode();
    else if (category.equals("제목")) return s.getTitleCode();
    else return s.getLyricCode();
  }

  public String getByCategory(String category, Song s) {
    if (category.equals("가수")) return s.getArtistName();
    else if (category.equals("제목")) return s.getTitle();
    else return s.getLyric();
  }

  public List<Song> autoComplete(String keyword, String category) {

    List<Song> songList = AllSongDTO.getSongList();

    switch (keyword) {
      case "ㄱ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 1).toList();
      }
      case "ㄴ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 2).toList();
      }
      case "ㄷ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 3).toList();
      }
      case "ㄹ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 4).toList();
      }
      case "ㅁ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 5).toList();
      }
      case "ㅂ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 6).toList();
      }
      case "ㅅ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 7).toList();
      }
      case "ㅇ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 8).toList();
      }
      case "ㅈ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 9).toList();
      }
      case "ㅊ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 10).toList();
      }
      case "ㅋ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 11).toList();
      }
      case "ㅌ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 12).toList();
      }
      case "ㅍ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 13).toList();
      }
      case "ㅎ" -> {
        return songList.stream().filter(a -> getCode(category, a) == 14).toList();
      }
    };

    return songList.stream().filter(a -> getByCategory(category, a).contains(keyword)).toList();
  }

  public boolean insertRequest(Map<String, String> request) {
    return songMapper.insertRequest(request) == 1;
  }
}
