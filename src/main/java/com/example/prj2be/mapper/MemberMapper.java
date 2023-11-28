package com.example.prj2be.mapper;


import com.example.prj2be.domain.Member;
import org.apache.ibatis.annotations.*;

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