package com.example.prj2be.service;

import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;

    public Member getMember(String id){
        return mapper.selectById(id);
    }


    public boolean login(Member member, WebRequest request) {
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
  
    public void add(Member member) {
        mapper.insert(member);
    }

    public String getId(String id) {
        return mapper.selectId(id);
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

}