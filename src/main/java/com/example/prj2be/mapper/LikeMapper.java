package com.example.prj2be.mapper;

import com.example.prj2be.domain.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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

        @Insert("""
select count(id) from memberlike
where mamberId = #{mamberId}
""")
    int Count(String mamberId);
}
