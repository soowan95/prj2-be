package com.example.prj2be.controller;

import com.example.prj2be.AllSongDTO;
import com.example.prj2be.domain.Member;
import com.example.prj2be.domain.Song;
import com.example.prj2be.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/song")
public class SongController {

  private final SongService songService;

  //  top100 찾기
  @GetMapping("top100")
  public List<Song> top100() {
    return songService.getSongLimit100();
  }

  //  모든 mood 찾기
  @GetMapping("mood")
  public List<Map<String, Object>> mood() {
    return songService.getMood();
  }

  //  모든 genre 찾기
  @GetMapping("genre")
  public List<Map<String, Object>> genre() {
    return songService.getGenre();
  }

  //  필터 거친 top100 찾기
  @GetMapping("ft100")
  public List<Song> filter(@RequestParam(value = "genre", required = false) List<String> genreList,
                           @RequestParam(value = "mood", required = false) List<String> moodList
  ) {

    return songService.getByFilter(genreList, moodList);
  }

  //  검색
  @GetMapping("search")
  public List<Song> search(@RequestParam("sc") String category,
                           @RequestParam("sk") String keyword,
                           @RequestParam(value = "genre", required = false) List<String> genreList,
                           @RequestParam(value = "mood", required = false) List<String> moodList) {
    return songService.getByCategoryAndKeyword(category, keyword, genreList, moodList);
  }

  //  비슷한 곡 탐색
  @GetMapping("similar")
  public List<Song> similar(String genre, String mood, Integer id) {
    return songService.getByGenreAndMood(genre, mood, id);
  }

  // 자동 완성
  @GetMapping("autoComplete")
  public List<Song> autoComplete(@RequestParam("sc") String category,
                                 @RequestParam("sk") String keyword) {

    return songService.autoComplete(keyword, category);
  }

  // 요청 받기 전에 그 곡이 db에 있는지 확인
  @GetMapping("isExist")
  public Boolean isExist(String title, String artist) {
    return songService.isExist(title, artist);
  }

  // 요청 받은 곡 insert
  @PostMapping("request")
  public ResponseEntity request(@RequestBody Map<String, String> request,
                                @SessionAttribute(value = "login", required = false) Member login) {

    if (login == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    if (songService.insertRequest(request)) return ResponseEntity.ok().build();
    else return ResponseEntity.badRequest().build();
  }

  @GetMapping("requestList")
  public List<Map<String, Object>> requestList(){

    return songService.requestList();
  }

  @GetMapping("mySongRequestList")
  public ResponseEntity<List<Map<String, Object>>> mySongRequestList(@SessionAttribute(value = "login", required = false) Member login){
    if (login == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(songService.mySongRequestList(login.getId()));
  }

  @GetMapping("{id}")
  public ResponseEntity<Song> getSongById(@PathVariable Integer id) {
    Song song = songService.getSongById(id);
    if (song != null) {
      return ResponseEntity.ok(song);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("plusSongPoint")
  public ResponseEntity plusSP(@RequestBody Song song) {

    if (songService.updateSongPointById(song.getId())) return ResponseEntity.ok().build();
    return ResponseEntity.internalServerError().build();
  }

  @GetMapping("chartlist")
  public  List<Song> chartlist(Integer id) {
    return songService.chartlist(id);
  }


  @PostMapping("insert")
  public ResponseEntity insert(Song song,
                               @RequestParam(value = "files", required = false) MultipartFile files) throws IOException {

    if (songService.insertSong(song, files)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.internalServerError().build();
    }
  }

  @GetMapping("albumList")
  public List<Map<String,Object>> albumList(@RequestParam Integer id){
    return songService.songListById(id);
  }

  @GetMapping("songEdit")
  public void songEdit(@RequestParam String artistName,
                       @RequestParam String artistGroup){

    Song song = new Song();
    song.setArtistGroup(artistGroup);
    song.setArtistName(artistName);
    songService.getArtistCode(song);
  }

  @PutMapping("songEdit")
  public void songEdit(Song song,
                       @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {


    songService.updateSong(song, file);
  }

  @PutMapping("songEditOnlyInfo")
  public void songEdit(@RequestBody Song song) {
    songService.updateSongOnlyInfo(song);
  }

  @PutMapping("updateRequest")
  public ResponseEntity<Void> updateRequest(@RequestBody Song song) {
    if(songService.updateRequest(song)) return ResponseEntity.ok().build();
    return ResponseEntity.internalServerError().build();
  }
}
