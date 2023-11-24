package com.example.prj2be.controller;

import com.example.prj2be.domain.Song;
import com.example.prj2be.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/song")
public class SongController {

  private final SongService songService;

//  top100 찾기
  @GetMapping("top100/")
  public List<Song> top100() {
    return songService.getSongLimit100();
  }

//  모든 mood 찾기
  @GetMapping("mood")
  public List<Map<String, Object>> mood() {
    return songService.getMood();
  }

//  모드 genre 찾기
  @GetMapping("genre")
  public List<Map<String, Object>> genre() {
    return songService.getGenre();
  }

//  필터 거친 top100 찾기
  @GetMapping("top100/{include}")
  public List<Song> filter(@PathVariable String include) {
    return songService.getByFilter(include);
  }

//  검색
  @GetMapping("search")
  public List<Song> search(@RequestParam("sc") String category,
                           @RequestParam("sk") String keyword) {
    return songService.getByCategoryAndKeyword(category, keyword);
  }

//  비슷한 곡 탐색
  @GetMapping("similar")
  public List<Song> similar(String genre, String mood, Integer id) {
    return songService.getByGenreAndMood(genre, mood, id);
  }
}
