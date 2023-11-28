package com.example.prj2be.mapper;

import com.example.prj2be.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberMapper {

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
}
