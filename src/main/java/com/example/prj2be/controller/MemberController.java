package com.example.prj2be.controller;

import com.example.prj2be.domain.Member;
import com.example.prj2be.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")

public class MemberController {
    private final MemberService service;


    @PostMapping("login")
    public ResponseEntity login(@RequestBody Member member, WebRequest request) {



        if (service.login(member, request)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }


    @PostMapping("logout")
    public void logout(HttpSession session){
        if (session != null){
            session.invalidate();;
        }
    }

@GetMapping("login")
    public Member login(@SessionAttribute(value = "login", required = false) Member login){
        return login;
}


// TODO: 로그인 창에서 회원가입 누르면 해당페이지로 이동







}












