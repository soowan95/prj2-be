package com.example.prj2be.mapper;

import com.example.prj2be.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("""
        INSERT INTO comment (songId, memberId, comment)
        VALUES (#{songId}, #{memberId}, #{comment})
        """)
    int insert(Comment comment);

    @Select("""
        SELECT
            c.id,
            c.comment,
            c.inserted,
            c.songId,
            c.memberId,
            m.nickName memberNickName
        FROM comment c JOIN member m ON c.memberId = m.id
        WHERE songId = #{songId}
        ORDER BY c.id DESC ;
        """)
    List<Comment> selectBySongId(String songId);

    @Select("""
    SELECT
            c.id,
            c.comment,
            c.inserted,
            c.songId,
            c.memberId,
            m.nickName memberNickName
    FROM comment c JOIN member m ON c.memberId = m.id
    WHERE songId = #{songId}
    ORDER BY c.id DESC
    LIMIT #{from}, 5
    """)
    List<Comment> selectByPaging(Integer from, String songId);

    @Delete("""
        DELETE FROM comment
        WHERE memberId = #{memberId}
        """)
    int deleteByMemberId(String memberId);

    @Delete("""
        DELETE FROM comment
        WHERE id = #{id}
        """)
    int deleteById(Integer id);

    @Select("""
        SELECT * FROM comment
        WHERE id = #{id}
        """)
    Comment selectById(Integer id);

    @Update("""
        UPDATE comment
            SET comment = #{comment}
        WHERE id = #{id}
        """)
    int update(Comment comment);
}
