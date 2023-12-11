package com.example.prj2be.service;

import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.LikeMapper;
import com.example.prj2be.mapper.MemberMapper;
import com.example.prj2be.mapper.myPlaylistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;
    private final myPlaylistMapper playlistMapper;
    private final LikeMapper likeMapper;

    public Member getMember(String id){
        return mapper.selectById(id);
    }


    public boolean login(Member member, WebRequest request) {
        mapper.login(member);
        Member dbMember = mapper.selectById(member.getId());
        if (dbMember != null){
            if (dbMember.getPassword().equals(member.getPassword())){
                dbMember.setPassword("");
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                return true;
            }
        }
        return false;
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

    public boolean add(Member member) {
        return mapper.insert(member) == 1;
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
        return mapper.deleteByMemberId(id)==1;
    }

    public void logout(Member login) {
        mapper.logout(login);
    }

  public List<String> getLiveUser() {
        return mapper.getLiveUser();
  }
}