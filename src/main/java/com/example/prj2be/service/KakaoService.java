package com.example.prj2be.service;

import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class KakaoService {

  private final MemberService memberService;

  public boolean kakaoLogin(Member member, WebRequest request) {

    if (memberService.checkId(member.getId()) != 1) {
      member.setPassword("1234");
      member.setInserted(LocalDateTime.now());
      memberService.kakaoAdd(member);
    }

    System.out.println(member);
    member.setPassword("1234");
    return memberService.login(member, request);
  };
}
