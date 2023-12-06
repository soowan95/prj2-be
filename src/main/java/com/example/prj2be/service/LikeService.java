package com.example.prj2be.service;


import com.example.prj2be.domain.PlaylistLike;
import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeMapper mapper;

    public Map<String,Object> update(PlaylistLike playlistLike) {
        int count = 0;
        if(mapper.delete(playlistLike) == 0) {
            count = mapper.insert(playlistLike); // 1
        }

        int countLike = mapper.countByBoardId(playlistLike.getLikelistId());

        return Map.of("isLike", mapper.isLike(playlistLike.getMemberId(), playlistLike.getLikelistId()) == 1,
                //memberId와 listId에 대한 좋아요 여부 확인 mepper.islike메서드가 1을 반환하면 해당 하는 좋아요가 존재한다는 뜻
                "countLike",countLike,
                //listId에 해당하는 데이터베이스 테이블에서의 특정 행의수를 나타낸다 즉 전제 좋아요 갯수 등을 나타낸다
                "boardId", playlistLike.getLikelistId());
                // 해당좋아요가 어떤 곳에 속하는지 나타내는것
    }

    public Map<String, Object> get(String likelistId, Member login) {
        int countLike = mapper.countByBoardId(likelistId);
        // 데이터베이스에서 해당 플레이리스트레 대한 좋아요 수를 조회합니다
        boolean like = false;
        if(login != null) {
            PlaylistLike playlistLike1 = mapper.selectByBoardIdAndMemberId(likelistId, login.getId());
            //현재 로그인한 회원의 ID와 플레이리스트ID를 사용하여 좋아요 정보를 조회한다
            like = playlistLike1 != null;
            // 조회된 좋아요 정보가 있으면, 해당 플레이리스트를 현재 로그인한 회원이 좋아요 했다고 설정
        }
        return Map.of("like",like, "countLike", countLike, "likelistId", likelistId);
        // like키에는 좋아요여부 //countlike키에는 좋아요 수  //playlistId키에는 플레이리스트ID값을 담아 반환
    }
}
