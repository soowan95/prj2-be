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
                WHERE memberId = #{memberId} AND boardId = #{boardId}
            """)
    int delete(Like like);

    @Insert("""
                INSERT INTO memberlike (memberId, boardId)
                values (#{memberId}, #{boardId})
            """)
    int insert(Like like);


    @Select("""
            SELECT count(id) FROM memberlike
            WHERE  boardId = #{boardId}
            """)
    int countByBoardId(Integer boardId);

    @Select("""
            SELECT * FROM memberlike
            WHERE boardId = #{boardId}
            AND memberId = #{memberId}
            """)
    Like selectByBoardIdAndMemberId(Integer boardId, String memberId);

    @Select("""
            SELECT COUNT(id)
            FROM memberlike
            WHERE memberId = #{memberId} AND boardId = #{boardId}
            """)
    Integer isLike(String memberId, Integer boardId);

    @Delete("""
            delete from memberlike
            where memberId = #{id}
            """)
    int deleteByMemberId(String id);
}
