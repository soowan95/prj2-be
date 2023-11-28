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

    @PostMapping("signup")
    public void signup(@RequestBody Member member) {
        System.out.println("member = " + member);
        service.add(member);
    }

    @GetMapping(value = "check", params = "id")
    public ResponseEntity checkId(String id) {
        if (service.getId(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity updateMember(
            @RequestParam("id") String idForRecovery,
            @RequestParam("q") String securityQuestion,
            @RequestParam("a") String securityAnswer,
            @RequestParam("p") String newPassword
    ) {
        if (idForRecovery == null || idForRecovery.isEmpty() ||
                securityQuestion == null || securityQuestion.isEmpty() ||
                securityAnswer == null || securityAnswer.isEmpty() ||
                newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (! service.updatePassword(idForRecovery, securityQuestion, securityAnswer, newPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
      }
      
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
}