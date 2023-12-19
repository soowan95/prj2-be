package com.example.prj2be.controller;

import com.example.prj2be.domain.Member;
import com.example.prj2be.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoContoroller {

  private final KakaoService kakaoService;

  @PostMapping("login")
  public ResponseEntity<Member> kakaoLogin(@RequestBody Member member, WebRequest request) {
    if (kakaoService.kakaoLogin(member, request) != null) return ResponseEntity.ok(kakaoService.kakaoLogin(member, request));

    return ResponseEntity.internalServerError().build();
  }
}
