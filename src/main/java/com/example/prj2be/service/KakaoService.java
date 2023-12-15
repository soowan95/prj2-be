package com.example.prj2be.service;

import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.MemberMapper;
import com.example.prj2be.util.Parse;
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

  public Member kakaoLogin(Member member, WebRequest request) {
    String password = creatPassword();

    if (memberService.checkId(member.getId()) != 1) {
      member.setPassword(password);
      member.setInserted(LocalDateTime.now());
      member.setNickName("k-" + member.getNickName());
      memberService.kakaoAdd(member);
    } else {
      memberService.kakaoUpdatePassword(member.getId(), Parse.passwordCode(password));
    }

    member.setPassword(password);
    return memberService.login(member, request);
  };

  public String creatPassword() {
    StringBuilder sb = new StringBuilder();

    int len = (int) (Math.random() * 5) + 5;

    for (int i = 0; i < len; i++) {
      int s = (int) (Math.random() * 37);

      if (s >= 0 && s < 10) sb.append((int) String.valueOf(s).charAt(0));
      else if (s == 11) sb.append((int) 'a');
      else if (s == 12) sb.append((int) 'b');
      else if (s == 13) sb.append((int) 'c');
      else if (s == 14) sb.append((int) 'd');
      else if (s == 15) sb.append((int) 'e');
      else if (s == 16) sb.append((int) 'f');
      else if (s == 17) sb.append((int) 'g');
      else if (s == 18) sb.append((int) 'h');
      else if (s == 19) sb.append((int) 'i');
      else if (s == 20) sb.append((int) 'j');
      else if (s == 21) sb.append((int) 'k');
      else if (s == 22) sb.append((int) 'l');
      else if (s == 23) sb.append((int) 'm');
      else if (s == 24) sb.append((int) 'n');
      else if (s == 25) sb.append((int) 'o');
      else if (s == 26) sb.append((int) 'p');
      else if (s == 27) sb.append((int) 'q');
      else if (s == 28) sb.append((int) 'r');
      else if (s == 29) sb.append((int) 's');
      else if (s == 30) sb.append((int) 't');
      else if (s == 31) sb.append((int) 'u');
      else if (s == 32) sb.append((int) 'v');
      else if (s == 33) sb.append((int) 'w');
      else if (s == 34) sb.append((int) 'x');
      else if (s == 35) sb.append((int) 'y');
      else sb.append((int) 'z');
    }

    return sb.toString();
  }
}
