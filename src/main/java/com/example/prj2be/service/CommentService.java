package com.example.prj2be.service;

import com.example.prj2be.domain.Comment;
import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper mapper;

    public boolean add(Comment comment, Member login) {
        comment.setMemberId(login.getId());
        return mapper.insert(comment) == 1;
    }

    public boolean validate(Comment comment) {
        if (comment == null) {
            return false;
        }

        if (comment.getSongId() == null || comment.getSongId() < 1) {
            return false;
        }

        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }

        return true;
    }

    public Map<String, Object> list(Integer page, String songId) {

        Map<String, Object> map = new HashMap<>();
        Map<String, Integer> pageInfo = new HashMap<>();

        int len = mapper.selectBySongId(songId).size();

        // 마지막 페이지 번호
        int lastPageNo = (len - 1) / 5 + 1;
        // 시작 페이지 번호
        int startPageNo = (page - 1) / 10 * 10 + 1;
        // 10개 씩 보여줄때 마지막 페이지 번호
        int endPageNo = startPageNo + 9;
        endPageNo = Math.min(lastPageNo, endPageNo);
        int prevPageNo = startPageNo - 10;
        int nextPageNo = endPageNo + 1;

        pageInfo.put("currentPageNo", page);
        pageInfo.put("startPageNo", startPageNo);
        pageInfo.put("endPageNo", endPageNo);

        if (prevPageNo > 0) pageInfo.put("prevPageNo", prevPageNo);
        if (nextPageNo <= lastPageNo) pageInfo.put("nextPageNo", nextPageNo);

        int from = (page - 1) * 5;

        map.put("commentList", mapper.selectByPaging(from, songId));
        map.put("pageInfo", pageInfo);

        return map;
    }

    public boolean remove(Integer id) {

        return mapper.deleteById(id) == 1;
    }

    public boolean hasAccess(String id, Member login) {
        // id가 null 또는 공백인 경우 처리
        if (id == null || id.isBlank()) {
            return false;
        }

        try {
            Integer commentId = Integer.parseInt(id);
            Comment comment = mapper.selectById(commentId);

            return comment.getMemberId().equals(login.getId());
        } catch (NumberFormatException e) {
            // id를 Integer로 변환할 수 없는 경우 처리
            return false;
        }
    }

    public boolean update(Comment comment) {
        return mapper.update(comment) == 1;
    }

    public boolean updateValidate(Comment comment) {
        if (comment == null) {
            return false;
        }

        if (comment.getId() == null) {
            return false;
        }

        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }

        return true;
    }

}
