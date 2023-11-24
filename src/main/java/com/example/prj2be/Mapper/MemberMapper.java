package com.example.prj2be.Mapper;

import com.example.prj2be.Domain.Auth;
import com.example.prj2be.Domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {
    
    @Select("""
select * from member;
""")
    Member selectById(String id);


}
