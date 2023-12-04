package com.example.prj2be.mapper;

import com.example.prj2be.domain.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Delete("""
                DELETE FROM memberlike
                WHERE memberId = #{memberId} AND playlistId = #{listId}
            """)
    int delete(Like like);

    @Insert("""
                INSERT INTO memberlike (memberId, playlistId)
                values (#{memberId}, #{listId})
            """)
    int insert(Like like);


    @Select("""
            SELECT count(id) FROM memberlike
            WHERE  playlistId = #{listId}
            """)
    int countByBoardId(Integer listId);

    @Select("""
            SELECT * FROM memberlike
            WHERE playlistId = #{listId}
            AND memberId = #{memberId}
            """)
    Like selectByBoardIdAndMemberId(Integer listId, String memberId);

    @Select("""
    SELECT COUNT(id)
    FROM memberlike
    WHERE memberId = #{memberId} AND playlistId = #{listId}
    """)
    Integer isLike(String memberId, Integer listId);
}
