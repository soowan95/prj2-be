package com.example.prj2be.mapper;

import com.example.prj2be.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
        INSERT INTO comment (songId, memberId, comment)
        VALUES (#{songId}, #{memberId}, #{comment})
        """)
    int insert(Comment comment);

    @Select("""
        SELECT *
        FROM comment
        WHERE songId = #{songId}
        """)
    List<Comment> selectBySongId(Integer songId);
}
