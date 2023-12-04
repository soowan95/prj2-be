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

        like.setMemberId(login.getId());

        int count = 0;
        if(mapper.delete(like) == 0) {
            count = mapper.insert(like); // 1
        }

        int countLike = mapper.countByBoardId(like.getBoardId());

        return Map.of("isLike", mapper.isLike(login.getId(), like.getBoardId()) == 1,
                "countLike",countLike,
                "boardId", like.getBoardId());
    }

    public Map<String, Object> get(Integer boardId, Member login) {
        int countLike = mapper.countByBoardId(boardId);
        boolean like = false;
        if(login != null) {
            Like like1 = mapper.selectByBoardIdAndMemberId(boardId, login.getId());
            like = like1 != null;
        }
        return Map.of("like",like, "countLike", countLike, "boardId", boardId);
    }
}
