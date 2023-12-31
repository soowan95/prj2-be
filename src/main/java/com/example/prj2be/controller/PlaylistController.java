package com.example.prj2be.controller;


import com.example.prj2be.domain.Member;
import com.example.prj2be.domain.MemberPlayList;
import com.example.prj2be.domain.MyPlaylist;
import com.example.prj2be.domain.Song;
import com.example.prj2be.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myList")
public class PlaylistController {

    private final PlaylistService service;

    @GetMapping("get")
    public List<MyPlaylist> getList(String id, String songId) {
        return service.getMyPlayList(id, songId);
    }


    @GetMapping("favorite")
    public List<MyPlaylist> favoriteList(String id) {
        return service.getFavoriteList(id);
    }
  
    @GetMapping("getByListId")
    public MyPlaylist getByListId(@RequestParam Integer listId,
                                  @SessionAttribute("login") Member login) {
        return service.getByListId(listId, login.getId());
    }
  
    @GetMapping("favoriteListName")
    public List<Song> favoriteListName(String listId) {
        List<Song> songs = service.getFavoriteListName(listId);
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setIndexForPlay(i);
        }
        return songs;
    }


    @DeleteMapping("editFavoriteList")
    public ResponseEntity delete(@RequestParam String songId,
                                 @RequestParam String playlistId,
                                 @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.deleteByFavoriteList(songId, playlistId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("recommendOrderByLike")
    public List<MemberPlayList> getRecommendPlaylist() {

     return service.getRecommendPlaylist();

    }


    @GetMapping("recommendOrderByViews")
    public List<MemberPlayList> getRecommendByViews() {
        return service.getRecommendByViews();
    }


    @GetMapping("topPlaylist")
    public List<Map<String,Object>> topPlaylist(String listId) {
        return service.getTopPlaylist(listId);
    }
  
    @PutMapping("hitscount")
    public Integer hitsCount(@RequestParam String id) {
        service.updateHitsCount(id);
        return service.getCountById(id);
    }

    @PostMapping("insertMyPlaylist")
    public void insertMyPlaylist(@RequestBody MyPlaylist myPlaylist) {
        System.out.println(myPlaylist);
       service.insertMyPlaylist(myPlaylist.getListId(), myPlaylist.getSongId());
    }

    @PostMapping("createPlaylist")
    public ResponseEntity createPlaylist(MemberPlayList memberPlayList,
                                         @RequestParam(value = "coverimage",required = false)MultipartFile coverImage
                                         ) throws IOException {

        if (coverImage != null ? service.createPlaylist(memberPlayList,coverImage) : service.createPlaylistWithDefaultImg(memberPlayList)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("check")
    public ResponseEntity checkNickName(String listName, String memberId) {
        if (service.getListName(listName, memberId) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }


    @DeleteMapping("{listId}")
    public void deletePlaylist(@PathVariable String listId) {
        service.deletePlaylist(listId);
    }


    @PutMapping("editPlaylist")
    public ResponseEntity edltPlaylist(MemberPlayList memberPlayList,
                                       @RequestParam(value = "coverimage",required = false) MultipartFile coverImage) throws IOException {
        if(coverImage != null ? service.editPlaylist(memberPlayList,coverImage) : service.editPlaylistWithDefaultImg(memberPlayList)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("lock")
    public List<MyPlaylist> updateIsLock(@RequestBody MyPlaylist myPlaylist) {
        return service.updateIsLock(myPlaylist);
    }
}
