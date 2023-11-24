package com.example.prj2be.mapper;

import com.example.prj2be.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    @Select("""
select * from member
where id= #{id}
""")
    Member selectById(String id);
}
