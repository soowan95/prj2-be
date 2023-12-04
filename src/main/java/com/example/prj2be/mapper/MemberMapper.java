package com.example.prj2be.mapper;

import org.apache.ibatis.annotations.*;
import com.example.prj2be.domain.Member;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Select("""
select * from member
where email =  #{email}
""")
    String selectByEmail(String email);

    @Select("""
            
            select * from member
            where nickName = #{nickName}
            """)
    String selectByNickName(String nickName);
            
    @Insert("""
            INSERT INTO member (id, password, nickName, email, securityQuestion, securityAnswer)
            VALUES (#{id}, #{password}, #{nickName}, #{email}, #{securityQuestion}, #{securityAnswer})
            """)
    int insert(Member member);

    @Select("""
            SELECT id FROM member
            WHERE id = #{id}
            """)
    String selectId(String id);

    @Update("""
            UPDATE member
            SET password = #{newPassword},
                securityQuestion = #{securityQuestion},
                securityAnswer = #{securityAnswer}
            WHERE id = #{id}
            """)
    int updatePassword(String id, String securityQuestion, String securityAnswer, String newPassword);

    @Select("""
            SELECT * FROM member
            WHERE id = #{id}
            """)
    Member selectById(String id);

    @Select("""
    SELECT COUNT(id)
    FROM member
    WHERE id = #{id}
    """)
    int checkId(String id);
}