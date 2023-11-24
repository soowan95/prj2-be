package com.example.prj2be.Service;

import com.example.prj2be.Domain.Auth;
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
@Transactional(rollbackFor = Exception.class)
public class MemberService {
    private final MemberMapper mapper;

    public boolean login(Member member, WebRequest request) {
        Member dbMember = mapper.selectById(member.getId());

        if(dbMember != null) {
            if (dbMember.getPassword().equals(member.getPassword())) {
//                List<Auth> auth = mapper.selectAuthById(member.getId());
//                dbMember.setAuth(auth);

                dbMember.setPassword("");
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                return true;
            }
        }
        return false;
    }
}
