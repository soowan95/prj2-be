package com.example.prj2be.service;

import com.example.prj2be.AllSongDTO;
import com.example.prj2be.domain.Song;
import com.example.prj2be.mapper.ArtistMapper;
import com.example.prj2be.mapper.CommentMapper;
import com.example.prj2be.mapper.SongMapper;
import com.example.prj2be.mapper.myPlaylistMapper;
import com.example.prj2be.util.Parse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SongService {

  private final SongMapper songMapper;
  private final CommentMapper commentMapper;
  private final ArtistMapper artistMapper;
  private final myPlaylistMapper myPlaylistMapper;
  public List<Song> getSongLimit100() {
    List<Song> songList = songMapper.getSongLimit100();

    for (int i = 0; i < songList.size(); i++) {
      songList.get(i).setIndexForPlay(i);
    }

    return songList;
  }

  public List<Map<String, Object>> getMood() {
    return songMapper.getMood();
  }

  public List<Map<String, Object>> getGenre() {
    return songMapper.getGenre();
  }

  public List<Song> getByFilter(List<String> genreList, List<String> moodList) {
    genreList = genreList.stream().filter(a -> !a.isEmpty()).map(a -> "%" + a + "%").toList();
    moodList = moodList.stream().filter(a -> !a.isEmpty()).map(a -> "%" + a + "%").toList();

    return songMapper.getByFilter(genreList, moodList);
  }

  // 필터 추가하여 검색
  public List<Song> getByCategoryAndKeyword(String category, String keyword, List<String> genreList, List<String> moodList) {
    genreList = genreList.stream().filter(a -> !a.isEmpty()).map(a -> "%" + a + "%").toList();
    moodList = moodList.stream().filter(a -> !a.isEmpty()).map(a -> "%" + a + "%").toList();

    return songMapper.getByCategoryAndKeyword(category, "%" + keyword + "%", genreList, moodList);
  }

  // 비슷한 곡 랜덤으로 5개 추출
  public List<Song> getByGenreAndMood(String genre, String mood, Integer id) {
    genre = "%" + genre + "%";
    mood = "%" + mood + "%";
    List<Song> list = songMapper.getByGenreAndMood(genre, mood, id);
    List<Song> newList = new ArrayList<>();
    List<Integer> numberList = new ArrayList<>();

    while (numberList.size() < Math.min(list.size(), 5)) {
      int number = (int) (Math.random() * list.size());
      if (!numberList.contains(number)) numberList.add(number);
    }

    for (int i : numberList) {
      newList.add(list.get(i));
    }

    return newList;
  }

  public Integer getCode(String category, Song s) {
    if (category.equals("가수")) return s.getArtistHangulCode();
    else if (category.equals("제목")) return s.getTitleHangulCode();
    else return s.getLyricHangulCode();
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

    if (request.get("title").isBlank() || request.get("artist").isBlank()) return false;

    return songMapper.insertRequest(request) == 1;
  }

  public Boolean isExist(String title, String artist) {
    List<Song> songList = AllSongDTO.getSongList();

    return songList.stream().filter(a -> a.getArtistName().equalsIgnoreCase(artist) && a.getTitle().equalsIgnoreCase(title)).count() >= 1;
  }

  public List<Map<String, Object>> requestList() {
    return songMapper.getByRequestList();
  }

  public Song getSongById(Integer id) {
    return songMapper.getSongById(id);
  }

  public boolean updateSongPointById(Integer songId) {
    Song song = songMapper.getSongById(songId);

    Integer artistCode = artistMapper.getArtistCodeByNG(song.getArtistName(), song.getArtistGroup());

    return songMapper.updateSongPoint(song, artistCode) >= 1;
  }

  public List<Song> chartlist(Integer id) {
    List<Song> chartList = new ArrayList<>(); // list Song 타입의 데이터와 배열을 생성
    List<Integer> songIds = myPlaylistMapper.chartlist(id); // songIds의 아이디만 받아온것
    for (Integer i : songIds) { // songIds의 아이디를 하나하나 i에 담은 것
      chartList.add(songMapper.getSongById(i)); //songIds 아이디의 곡 정보들을 chartList에 add로 담은 것
    }
    return chartList;
  }

  public Boolean insertSong(Song song) {
    // 한글 코드 파싱해서 저장
    song.setArtistHangulCode(Parse.hangulCode(song.getArtistName()));
    song.setTitleHangulCode(Parse.hangulCode(song.getTitle()));
    song.setLyricHangulCode(Parse.hangulCode(song.getLyric()));

    // 가수 코드
    Integer artistCode = songMapper.getArtistCode(song);

    songMapper.insertSongPoint(song, artistCode);
    songMapper.updateSongRequest(song);

    // artistCode 찾기 위함
    if (song.getArtistGroup().isBlank()) song.setArtistGroup("solo");

    // 자동완성 위한 전역에 새로 저장한 song 추가
    AllSongDTO.getSongList().add(song);

    return songMapper.insertSong(song, artistCode) == 1;
  }

  public Integer getArtistCode(Song song) {
    return songMapper.getArtistCode(song);
  }

  public void insertArtist(Song song) {
    songMapper.insertArtist(song);
  }

  public boolean deleteMember(String id) {
    // 멤버가 작성한 댓글 삭제
    commentMapper.deleteByMemberId(id);

//    // songPage에 달린 댓글들 지우기
//    commentMapper.deleteBySongId(id);

    return songMapper.deleteById(id) == 1;
  }

  public List<Map<String, Object>> albumList(String album) {
    List<Map<String, Object>> albumList = songMapper.getByAlbumList(album);
    for (int i = 0; i < albumList.size(); i++) {
      albumList.get(i).put("id", i + 1);
    }

    return albumList;
  }
}