package com.example.prj2be.service;

import com.example.prj2be.domain.Auth;
import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.LikeMapper;
import com.example.prj2be.mapper.MemberMapper;
import com.example.prj2be.mapper.myPlaylistMapper;
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

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;
    private final myPlaylistMapper playlistMapper;
    private final LikeMapper likeMapper;
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
        if (dbMember.getProfilePhoto().equals("userdefault.jpg")) photoUrl = urlPrefix + "prj2/user/default/" + dbMember.getProfilePhoto();
        else photoUrl = urlPrefix + "prj2/user/" + dbMember.getId() + "/" + dbMember.getProfilePhoto();
        dbMember.setProfilePhoto(photoUrl);
        if (dbMember != null){
            if (dbMember.getPassword().equals(member.getPassword())){
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
        return mapper.kakaoInsert(member) ==1;
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

        mapper.insert(member,profilePhoto.getOriginalFilename());
        upload(member.getId(),profilePhoto);
    }

    public boolean update(Member member, MultipartFile profilePhoto, WebRequest request) throws IOException {
        String prePhoto = mapper.getPhotoNameById(member);
        member.setProfilePhoto(profilePhoto.getOriginalFilename());
        mapper.update(member);
        if(!prePhoto.equals("userdefault.jpg")) {
            deleteObject(member.getId(), prePhoto);
        }
        upload(member.getId(),profilePhoto);
        member.setProfilePhoto(urlPrefix + "prj2/user/" + member.getId() + "/" + member.getProfilePhoto());
        member.setPassword("");
        request.setAttribute("login", member, RequestAttributes.SCOPE_SESSION);
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
        String originalPassword = member.getPassword();
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

        mapper.updatePassword(id, securityQuestion, securityAnswer, newPassword);
        return true;
    }
  
    public boolean update(Member member) {
        return mapper.update(member) == 1;
    }

    public int checkId(String id) {
        return mapper.checkId(id);
    }

    public boolean deleteMember(String id) {
        // 이 멤버의 플레이리스트 삭제
        playlistMapper.deleteByMemberId(id);

        // 이 멤버의 좋아요 클릭 삭제
        likeMapper.deleteByMemberId(id);

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
}