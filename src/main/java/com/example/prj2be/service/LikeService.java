package com.example.prj2be.service;

import com.example.prj2be.domain.Like;
import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeMapper mapper;

    public Map<String,Object> update(Like like, Member login) {
        like.setMamberId(login.getId());
        int count = 0;
        if(mapper.delete(like) == 0) {
            count = mapper.insert(like); // 1
        }
        int countLike = mapper.countByMemberId(like.getMamberId());

        return Map.of("like",count == 1,
                "countlike",countLike);
    }
}
