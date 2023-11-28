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
            WHERE memberId = #{mamberId}
        """)
    int delete(Like like);

        @Insert("""
            INSERT INTO memberlike (memberId)
            values (#{mamberId})
        """)
    int insert(Like like);


        @Select("""
            SELECT count(id) FROM memberLike
            WHERE  boardIdId = #{boardIdId}
            """)
    int countByMemberId(Integer boardIdId);
}
