package com.example.prj2be.service;

import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;

    public Member getMember(String id){
        return mapper.selectById(id);
    }


    public boolean login(Member member) {
        Member dbMember = mapper.selectById(member.getId());
        if (dbMember != null){
            if (dbMember.getPassword().equals(member.getPassword())){
                return true;

            }
        }
        return false;
    }
}














