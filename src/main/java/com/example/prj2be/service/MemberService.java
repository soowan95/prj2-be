package com.example.prj2be.service;

import com.example.prj2be.domain.Auth;
import com.example.prj2be.domain.Member;
import com.example.prj2be.domain.MyPlaylist;
import com.example.prj2be.mapper.*;
import com.example.prj2be.util.Parse;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final PlaylistService playlistService;

    private final MemberMapper mapper;
    private final myPlaylistMapper playlistMapper;
    private final MessageMapper messageMapper;
    private final LikeMapper likeMapper;
    private final CommentMapper commentMapper;
    private final SongMapper songMapper;
    private final S3Client s3;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    public Member getMember(String id) {
        return mapper.selectById(id);
    }

    public Member login(Member member, WebRequest request) {

        mapper.login(member);
        Member dbMember = mapper.selectById(member.getId());
        String photoUrl = "";
        if (dbMember.getProfilePhoto().matches("http.*")) photoUrl = dbMember.getProfilePhoto();
        else if (dbMember.getProfilePhoto().equals("userdefault.jpg")) photoUrl = urlPrefix + "prj2/user/default/" + dbMember.getProfilePhoto();
        else photoUrl = urlPrefix + "prj2/user/" + dbMember.getId() + "/" + dbMember.getProfilePhoto();
        dbMember.setProfilePhoto(photoUrl);
        if (dbMember != null){
            if (Parse.parsePasswordCode(dbMember.getPassword()).equals(member.getPassword())){
                List<Auth> auth = mapper.selectAuthById(member.getId());
                dbMember.setAuth(auth);
                dbMember.setPassword("");
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
            }
        }

        return dbMember;
    }

    public boolean validate(Member member) {
        if (member == null) {
            return false;
        }

        if (member.getEmail().isBlank()) {
            return false;
        }

        if (member.getPassword().isBlank()) {
            return false;
        }

        if (member.getId().isBlank()) {
            return false;
        }
        return true;
    }

    public boolean kakaoAdd(Member member) {
        member.setPassword(Parse.passwordCode(member.getPassword()));
        return mapper.kakaoInsert(member) == 1;
    }

    public void upload(String id, MultipartFile file) throws IOException {
        String key = "prj2/user/" + id +  "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public void add(Member member, MultipartFile profilePhoto) throws IOException {
        member.setPassword(Parse.passwordCode(member.getPassword()));

        mapper.insert(member,profilePhoto.getOriginalFilename());
        upload(member.getId(),profilePhoto);
    }

    public void addOnlyInfo(Member member) {
        member.setPassword(Parse.passwordCode(member.getPassword()));

        mapper.insert(member, "userdefault.jpg");
    }

    public boolean update(Member member, MultipartFile profilePhoto, WebRequest request) throws IOException {
        Member dbMember = mapper.getMemberById(member);

        if (member.getNickName() == null) member.setNickName(dbMember.getNickName());
        if (member.getEmail() == null) member.setEmail(dbMember.getEmail());

        String prePhoto = dbMember.getProfilePhoto();
        member.setProfilePhoto(profilePhoto.getOriginalFilename());

        if (!member.getNickName().equals(dbMember.getNickName())) {
            messageMapper.dropByNickName(dbMember.getNickName());
            mapper.update(member);
            messageMapper.addByNickName(member.getNickName());
        } else mapper.update(member);

        if(!prePhoto.equals("userdefault.jpg")) {
            deleteObject(member.getId(), prePhoto);
        }
        upload(member.getId(),profilePhoto);
        member.setProfilePhoto(urlPrefix + "prj2/user/" + member.getId() + "/" + profilePhoto.getOriginalFilename());
        member.setPassword("");

        request.setAttribute("login", member, RequestAttributes.SCOPE_SESSION);
        return true;
    }

    public Boolean updateOnlyInfo(Member member, WebRequest request) {
        Member dbMember = mapper.getMemberById(member);

        if (member.getNickName() == null) member.setNickName(dbMember.getNickName());
        if (member.getEmail() == null) member.setEmail(dbMember.getEmail());

        if (!member.getNickName().equals(dbMember.getNickName())) {
            messageMapper.dropByNickName(dbMember.getNickName());
            mapper.updateOnlyInfo(member);
            messageMapper.addByNickName(member.getNickName());
        } else mapper.updateOnlyInfo(member);

        Member newMember = mapper.getMemberById(member);

        if (!newMember.getProfilePhoto().matches("http://.*") && newMember.getProfilePhoto().equals("userdefault.jpg")) newMember.setProfilePhoto(urlPrefix + "prj2/user/defalut/" + newMember.getProfilePhoto());
        else if (!newMember.getProfilePhoto().matches("http://.*") && !newMember.getProfilePhoto().equals("userdefault.jpg")) newMember.setProfilePhoto(urlPrefix + "prj2/user/" + newMember.getId() + "/" + newMember.getProfilePhoto());

        request.setAttribute("login", newMember, RequestAttributes.SCOPE_SESSION);

        return true;
    }

    public void deleteObject(String id, String profilePhoto) {
        String key = "prj2/user/"+id+"/"+ profilePhoto;
        DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3.deleteObject(objectRequest);
    }


    public String getId(String id) {
        return mapper.selectId(id);
    }

    public String getEmail(String email) {
        return mapper.selectByEmail(email);
    }

    public String getNickName(String nickName) {
        return mapper.selectByNickName(nickName);
    }

    public boolean isValidIdAndAnswer(String id, String answer) {
        Member member = mapper.selectById(id);
        return member != null && member.getSecurityAnswer().equals(answer);
    }

    public Map<String, String> getPassword(String id, String securityQuestion, String securityAnswer) {
        if (!isValidIdAndAnswer(id, securityAnswer)) {
            return null;
        }

        Member member = mapper.selectById(id);

        // 가져온 비밀번호 반만 보여주기
        String originalPassword = Parse.parsePasswordCode(member.getPassword());
        int len = originalPassword.length() / 2;
        String maskedPassword = originalPassword.substring(0, len) + "*".repeat(originalPassword.length() - len);

        // 비밀번호와 닉네임을 함께 반환
        Map<String, String> response = new HashMap<>();
        response.put("fetchedPassword", maskedPassword);
        response.put("nickName", member.getNickName());
        return response;
    }

    public boolean updatePassword(String id, String securityQuestion, String securityAnswer, String newPassword) {
        if (!isValidIdAndAnswer(id, securityAnswer)) {
            return false;
        }

        String newPasswordCode = Parse.passwordCode(newPassword);

        mapper.updatePassword(id, securityQuestion, securityAnswer, newPasswordCode);
        return true;
    }

    public boolean update(Member member) {
        return mapper.update(member) == 1;
    }

    public int checkId(String id) {
        return mapper.checkId(id);
    }

    public boolean deleteMember(String id) {
        Member member = new Member();
        member.setId(id);
        member = mapper.getMemberById(member);

        // chat방에서 나가기
        messageMapper.dropByNickName(member.getNickName());

        // 플레이리스트 중 내가 누른 좋아요 삭제
        likeMapper.deleteByMemberId(member.getId());

        // 내 플레이리스트에 눌린 좋아요 삭제
        List<MyPlaylist> myPlayList = playlistMapper.getMyPlayList(member.getId());
        myPlayList.stream().map(MyPlaylist::getListId).forEach((likeMapper::deleteByListId));

        // 내 플레이리스트에 담긴 곡 삭제
        myPlayList.stream().map(MyPlaylist::getListId).forEach((playlistMapper::deleteSongByMyPlaylist));

        // s3에 올린 플레이리스트 사진 삭제
        myPlayList.forEach(a -> playlistService.deleteObject(a.getListId(), a.getCoverimage()));

        // 내 플레이리스트 삭제
        playlistMapper.deleteByMemberId(member.getId());

        // 내가 쓴 댓글 삭제
        commentMapper.deleteByMemberId(member.getId());

        // 내가 보낸 곡 요청 삭제
        songMapper.deleteRequestById(member.getId());

        // s3에 올린 멤버 사진 삭제
        deleteObject(member.getId(), member.getProfilePhoto());

        //멤버 삭제
        return mapper.deleteByMemberId(id) == 1;
    }

    public List<String> getQuestions(String id) {
        return mapper.getQuestions(id);
    }

    public void logout(Member login) {
        mapper.logout(login);
    }

    public List<String> getLiveUser() {
        return mapper.getLiveUser();
    }

    public void kakaoUpdatePassword(String id, String s) {
        mapper.kakaoUpdatePassword(id, s);
    }

    // 채팅창에 프로필 띄우기 위함
    public Member getByNickName(String sender) {
        return mapper.getByNickName(sender);
    }

    public Boolean isValidPassword(Member login, JsonNode password) {
        Member memberById = mapper.getMemberById(login);
        return Parse.passwordCode(password.get("password").toString().replaceAll("\"", "")).equals(memberById.getPassword());
    }
}