package com.example.prj2be.service;

import com.example.prj2be.domain.*;
import com.example.prj2be.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PlaylistService {

    private final myPlaylistMapper mapper;
    private final LikeMapper likeMapper;
    private final SongMapper songMapper;
    private final ArtistMapper artistMapper;
    private final FileMapper fileMapper;
    private final S3Client s3;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    public void upload(String id, MultipartFile file) throws IOException {
        String key = "prj2/playlist/" + id +  "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public void deleteObject(String id, String profilePhoto) {
        String key = "prj2/playlist/"+id+"/"+ profilePhoto;
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3.deleteObject(objectRequest);
    }

    public boolean validate(MyPlaylist playlist) {
        if (playlist == null) {
            return false;
        }
        return true;
    }
  
    public List<MyPlaylist> getMyPlayList(String id, String songId) {
        List<MyPlaylist> playList = mapper.getMyPlayList(id);

        for (MyPlaylist list : playList) {
            list.setCountLike(likeMapper.countByBoardId(list.getListId()));
            list.setTotalSongCount(mapper.chartlist(Integer.parseInt(list.getListId())).size());
            list.setIsSongContain(mapper.getCountBySongId(songId, list.getListId()) >= 1);
            list.setIsLike(likeMapper.isLike(list.getId(), list.getListId()).isPresent());

            if (!mapper.getSongIdBylistId(list.getListId()).isEmpty() && list.getCoverimage().equals("defaultplaylist.jpg")) {
                Integer mySongId = mapper.getSongIdBylistId(list.getListId()).get(0);
                Integer artistCode = songMapper.getArtistCodeBySongId(mySongId);
                String picture = artistMapper.getPictureByCode(artistCode);
                if (picture.equals("artistdefault.png")) list.setPhoto(urlPrefix + "prj2/artist/default/" + picture);
                else list.setPhoto(urlPrefix + "prj2/artist/" + artistCode + "/" + picture);
            } else if (!list.getCoverimage().equals("defaultplaylist.jpg")) list.setPhoto(urlPrefix + "prj2/playlist/" + list.getListId() + "/" + list.getCoverimage());
            else list.setPhoto(urlPrefix + "prj2/playlist/default/" + list.getCoverimage());
        }

        return playList;
    }
  

    public List<MyPlaylist> getFavoriteList(String id) {
        List<MyPlaylist> lists = mapper.selectFavoriteList(id);

        for (MyPlaylist list : lists) {
            if (!mapper.getSongIdBylistId(list.getListId()).isEmpty() && list.getCoverimage().equals("defaultplaylist.jpg")) {
                Integer mySongId = mapper.getSongIdBylistId(list.getListId()).get(0);
                Integer artistCode = songMapper.getArtistCodeBySongId(mySongId);
                String picture = artistMapper.getPictureByCode(artistCode);
                if (picture.equals("artistdefault.png")) list.setPhoto(urlPrefix + "prj2/artist/default/" + picture);
                else list.setPhoto(urlPrefix + "prj2/artist/" + artistCode + "/" + picture);
            } else if (!list.getCoverimage().equals("defaultplaylist.jpg")) list.setPhoto(urlPrefix + "prj2/playlist/" + list.getListId() + "/" + list.getCoverimage());
            else list.setPhoto(urlPrefix + "prj2/playlist/default/" + list.getCoverimage());
        }

        return lists;
    }

    public MyPlaylist getByListId(Integer listId, String id) {
        MyPlaylist list = mapper.getByListId(listId);

            list.setCountLike(likeMapper.countByBoardId(String.valueOf(listId)));
            //첫페이지 //countByBoardId는 라이크가 몇개인지
//            list.setSongId(likeMapper.countBySongId(list.getSongId()));
            list.setIsLike(likeMapper.isLike(id, list.getListId()).isPresent());
            //첫페이지에서 내가 좋아요 누른 거를 볼 수 있게 list의 id랑 list의 listId가 값이 1이면 ture 0이면 false
            list.setTotalSongCount(mapper.chartlist(Integer.parseInt(list.getListId())).size());
            //setTotalSongCount은 domain TotalSongCount에 저장할건데 chartlist의 ListId를 불러와서 갯수를 카운트하고 싶은데 String이라서 Integer로 형변환해서 카운트

            if (!mapper.getSongIdBylistId(list.getListId()).isEmpty() && list.getCoverimage().equals("defaultplaylist.jpg")) {
                Integer mySongId = mapper.getSongIdBylistId(list.getListId()).get(0);
                Integer artistCode = songMapper.getArtistCodeBySongId(mySongId);
                String picture = artistMapper.getPictureByCode(artistCode);
                if (picture.equals("artistdefault.png")) list.setPhoto(urlPrefix + "prj2/artist/default/" + picture);
                else list.setPhoto(urlPrefix + "prj2/artist/" + artistCode + "/" + picture);
            } else if (!list.getCoverimage().equals("defaultplaylist.jpg"))
                list.setPhoto(urlPrefix + "prj2/playlist/" + list.getListId() + "/" + list.getCoverimage());
            else list.setPhoto(urlPrefix + "prj2/playlist/default/" + list.getCoverimage());

            list.setTotalSongCount(mapper.chartlist(Integer.parseInt(list.getListId())).size());

        return list;
    }
      
    public List<Song> getFavoriteListName(String listId) {
        return mapper.selectByFavoriteListName(listId);
    }

    public boolean deleteByFavoriteList(String songId, String playlistId) {
        return mapper.deleteByFavoriteList(songId, playlistId)==1;
    }

    public List<MemberPlayList> getRecommendPlaylist() {
        List<MemberPlayList> recommendPlaylist = mapper.getRecommendPlaylist();

        for (MemberPlayList list : recommendPlaylist) {
            if (!mapper.getSongIdBylistId(list.getId()).isEmpty() && list.getCover().equals("defaultplaylist.jpg")) {
                Integer mySongId = mapper.getSongIdBylistId(list.getId()).get(0);
                Integer artistCode = songMapper.getArtistCodeBySongId(mySongId);
                String picture = artistMapper.getPictureByCode(artistCode);
                if (picture.equals("artistdefault.png")) list.setPicture(urlPrefix + "prj2/artist/default/" + picture);
                else list.setPicture(urlPrefix + "prj2/artist/" + artistCode + "/" + picture);
            } else if (!list.getCover().equals("defaultplaylist.jpg")) list.setPicture(urlPrefix + "prj2/playlist/" + list.getId() + "/" + list.getCover());
            else list.setPicture(urlPrefix + "prj2/playlist/default/" + list.getCover());
        }

        return recommendPlaylist;
    }

    public List<MemberPlayList> getRecommendByViews() {
        List<MemberPlayList> recommendPlaylist = mapper.getRecommendByViews();

        for (MemberPlayList memberPlayList : recommendPlaylist) {
            Song song = new Song();
            song.setArtistGroup(memberPlayList.getGroup());
            song.setArtistName(memberPlayList.getName());
            if (memberPlayList.getCover().equals("defaultplaylist.jpg")) memberPlayList.setPictureUrl(urlPrefix + "prj2/artist/"+songMapper.getArtistCode(song)+ "/"+memberPlayList.getPicture());
            else memberPlayList.setPictureUrl(urlPrefix+"prj2/playlist/"+memberPlayList.getId()+"/"+memberPlayList.getCover());
        }

        return recommendPlaylist;
    }





    public List<Map<String,Object>> getTopPlaylist(String listId) {
        return mapper.getTopPlaylist(listId);
    }
  
    public Integer getCountById(String id) {
        return mapper.getCountById(id);
    }
  
    public Integer updateHitsCount(String id) {
        return mapper.updateHitsCount(id);
    }
  
    public void insertMyPlaylist(String listId, Integer songId) {
        mapper.insertMyPlaylist(listId, songId);
    }

    public String getListName(String listName, String memberId) {
        return mapper.selectByListName(listName, memberId);
    }

    public boolean createPlaylist(MemberPlayList memberPlayList, MultipartFile coverimage) throws IOException{
        memberPlayList.setPicture(coverimage.getOriginalFilename());
        mapper.createPlaylist(memberPlayList);
        upload(memberPlayList.getId(),coverimage);
        return true;
    }

    public Boolean createPlaylistWithDefaultImg(MemberPlayList memberPlayList) {
        memberPlayList.setPicture("defaultplaylist.jpg");
        mapper.createPlaylistWithDefaultImg(memberPlayList);
        return true;
    }

    public boolean deletePlaylist(String listId) {

        MemberPlayList memberPlayList = new MemberPlayList();
        memberPlayList.setId(listId);

        // 지우려는 플레이리스트의 노래 제거
        mapper.deleteSongByMyPlaylist(listId);
        // 플레이리스트의 좋아요 갯수 제거
        mapper.deleteLikeCountByPlaylistLike(listId);
        // s3에 올린 사진 삭제
        deleteObject(listId, mapper.getCoverImageByPlaylistId(memberPlayList));
        // 이 멤버의 플레이리스트 제거
        return mapper.deleteByListId(listId)==1;
    }

    public boolean editPlaylist(MemberPlayList memberPlayList, MultipartFile coverImage) throws IOException {
        memberPlayList.setPicture(coverImage.getOriginalFilename());
        mapper.editPlaylist(memberPlayList);
        upload(memberPlayList.getId(), coverImage);
        return true;
    }

    public boolean editPlaylistWithDefaultImg(MemberPlayList memberPlayList) {
        memberPlayList.setPicture("defaultplaylist.jpg");
        mapper.editPlaylistWithDefaultImg(memberPlayList);
        return true;
    }

    public List<MyPlaylist> updateIsLock(MyPlaylist myPlaylist) {
        mapper.updateIsLockByPlaylistId(myPlaylist.getListId());
        return null;
    }
}
