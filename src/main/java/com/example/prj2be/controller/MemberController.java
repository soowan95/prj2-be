package com.example.prj2be.controller;

import com.example.prj2be.domain.Member;
import com.example.prj2be.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")

public class MemberController {
    private final MemberService service;



    @PostMapping("login")
    public ResponseEntity login(@RequestBody Member member){
        if (service.login(member)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

}

}

