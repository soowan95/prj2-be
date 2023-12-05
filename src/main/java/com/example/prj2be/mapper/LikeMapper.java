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
                WHERE memberId = #{memberId} AND likelistId = #{likelistId}
            """)
    int delete(Like like);

    @Insert("""
                INSERT INTO memberlike (memberId, likelistId)
                values (#{memberId}, #{likelistId})
            """)
    int insert(Like like);


    @Select("""
            SELECT count(id) FROM memberlike
            WHERE  likelistId = #{likelistId}
            """)
    Integer countByBoardId(String likelistId);

    @Select("""
            SELECT * FROM memberlike
            WHERE likelistId = #{likelistId}
            AND memberId = #{memberId}
            """)
    Like selectByBoardIdAndMemberId(String likelistId, String memberId);

    @Select("""
    SELECT COUNT(id)
    FROM memberlike
    WHERE memberId = #{memberId} AND likelistId = #{likelistId}
    """)
    Integer isLike(String memberId, String likelistId);
}
