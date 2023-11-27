package com.example.prj2be.Service;

import com.example.prj2be.Domain.Member;
import com.example.prj2be.Mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor=Exception.class)
public class MemberService {
    private final MemberMapper mapper;

    public boolean login(Member member, WebRequest request) {
        Member dbMember = mapper.selectById(member.getId());

        if(dbMember != null) {
            if (dbMember.getPassword().equals(member.getPassword())) {


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
        return mapper.selectEmail(email);
    }

    public String getNickName(String nickName) {
        return mapper.selectNickName(nickName);
    }
}
