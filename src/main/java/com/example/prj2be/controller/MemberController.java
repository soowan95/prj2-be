package com.example.prj2be.controller;

import com.example.prj2be.domain.Member;
import com.example.prj2be.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService service;

    @PostMapping("signup")
    public void signup(Member member,
                       @RequestParam(value = "profilePhoto",required = false)MultipartFile profilePhoto) throws IOException {


        service.add(member,profilePhoto);
    }

    @GetMapping(value = "check")
    public ResponseEntity checkId(@RequestParam String id) {
        if (service.getId(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(String email) {
        if (service.getEmail(email) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "check", params = "nickName")
    public ResponseEntity checkNickName(String nickName) {
        if (service.getNickName(nickName) == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/get-password")
    public ResponseEntity<Map<String, String>> foundPassword(
            @RequestParam("id") String idForRecovery,
            @RequestParam("q") String securityQuestion,
            @RequestParam("a") String securityAnswer) {
        if (idForRecovery == null || idForRecovery.isEmpty() ||
                securityQuestion == null || securityQuestion.isEmpty() ||
                securityAnswer == null || securityAnswer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Map<String, String> response = service.getPassword(idForRecovery, securityQuestion, securityAnswer);

        if (response != null) {
            // 가져온 비밀번호 반환
            return ResponseEntity.ok(response);
        } else {
            // 인증 실패 시 401 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @RequestMapping("/update-password")
    public ResponseEntity updatePassword(
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
    public void logout(HttpSession session,
                       @SessionAttribute(value = "login", required = false) Member login) {
        if (session != null) {
            service.logout(login);
            session.invalidate();
        }
    }

     @GetMapping("login")
     public Member login(@SessionAttribute(value = "login", required = false) Member login){
          return login;
  }


    @PutMapping("edit")
    public ResponseEntity edit(Member member,
                               @SessionAttribute(value = "login",required = false)Member login,
                               @RequestParam(value = "photo",required = false)MultipartFile profilePhoto,
                               WebRequest request) throws IOException {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.update(member,profilePhoto,request)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<Member> view(String id,
                                       @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Member member = service.getMember(id);

        return ResponseEntity.ok(member);
    }


    @DeleteMapping
    public ResponseEntity delete (String id,
                                  HttpSession session,
                                  @SessionAttribute(value = "login",required = false)Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.deleteMember(id)) {
            session.invalidate();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("questions")
    public List<String> getQuestions(@RequestParam String id) {
        return service.getQuestions(id);
    }


}
