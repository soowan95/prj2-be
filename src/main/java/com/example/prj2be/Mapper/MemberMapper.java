package com.example.prj2be.Mapper;


import com.example.prj2be.Domain.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {


    @Select("""
select * from member
where id = #{id}
""")
    Member selectById(String id);


    @Insert("""
insert into member (id, password, nickName, email)
values (#{id},#{password},#{nickName},#{email}) 
""")
    int insert(Member member);

    @Select("""
select * from member
where id =  #{id}
""")
    String selectId(String id);

    @Select("""
select * from member
where email =  #{email}
""")
    String selectEmail(String email);

    @Select("""
            select * from member
            where nickName = #{nickName}
            """)
    String selectNickName(String nickName);
}