package com.example.prj2be.service;

import com.example.prj2be.domain.Song;
import com.example.prj2be.mapper.SongMapper;
import lombok.RequiredArgsConstructor;
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

  public List<Song> getByCategoryAndKeyword(String category, String keyword) {
    return songMapper.getByCategoryAndKeyword(category, "%" + keyword + "%");
  }

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
}
