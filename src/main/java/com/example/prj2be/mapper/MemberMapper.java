package com.example.prj2be.mapper;

import com.example.prj2be.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface MemberMapper {

    @Insert("""
INSERT INTO member(id, password, nickName, email, inserted)
VALUES (#{id}, #{password}, #{nickName}, #{email}, #{inserted})
""")
    int insert(Member member);



    @Select("""
SELECT *
FROM member
WHERE id = #{id}
""")
    Member selectById(String id);


}


