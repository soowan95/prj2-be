package com.example.prj2be.service;

import com.example.prj2be.domain.Comment;
import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Comment> list(String songId) {
            return mapper.selectBySongId(songId);
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
