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
    public List<Map<String,Object>> favoriteList(String id) {
        return service.getFavoriteList(id);
    }
  
    @GetMapping("getByListId")
    public MyPlaylist getByListId(@RequestParam Integer listId) {
        MyPlaylist playlist = service.getByListId(listId);
        List<LocalDate> dates = service.getRelease(listId);
        playlist.setRelease(dates.get(0));
        playlist.setUpdate(dates.get(dates.size()-1));
        return playlist;
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
    public void insertMyPlaylist(Integer listId, Integer id) {
       service.insertMyPlaylist(listId, id);
    }

    @PostMapping("createPlaylist")
    public ResponseEntity createPlaylist(MemberPlayList memberPlayList,
                                         @RequestParam(value = "coverimage",required = false)MultipartFile coverImage
                                         ) throws IOException {

//        MyPlaylist myPlaylist = new MyPlaylist();
//        myPlaylist.setListName(listName);
//        myPlaylist.setMemberId(memberId);
        if (service.createPlaylist(memberPlayList,coverImage)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping(value = "check", params = "listName")
    public ResponseEntity checkNickName(String listName) {
        if (service.getListName(listName) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }


    @DeleteMapping("{listId}")
    public void deletePlaylist(@PathVariable String listId) {
        service.deletePlaylist(listId);
    }

}
