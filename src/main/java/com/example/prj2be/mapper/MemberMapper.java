package com.example.prj2be.mapper;

import com.example.prj2be.domain.Auth;
import org.apache.ibatis.annotations.*;
import com.example.prj2be.domain.Member;
import org.springframework.web.multipart.MultipartFile;

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
            INSERT INTO member (id, password, nickName, email, securityQuestion, securityAnswer, profilePhoto)
            VALUES (#{member.id}, #{member.password}, #{member.nickName}, #{member.email}, #{member.securityQuestion}, #{member.securityAnswer}, #{profilePhoto})
            """)
    int insert(Member member, String profilePhoto);

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

    @Update("""
            update member
            set nickName = #{nickName}, email=#{email}, profilePhoto = #{profilePhoto}
            where id = #{id}
            """)
    int update(Member member);

    @Select("""
            SELECT COUNT(id)
            FROM member
            WHERE id = #{id}
            """)
    int checkId(String id);

    @Delete("""
            delete from member
            where id = #{id}
            """)
    int deleteByMemberId(String id);

    @Select("""
    SELECT securityQuestion
    FROM member
    WHERE id = #{id}
    """)
    List<String> getQuestions(String id);
            
    @Update("""
    UPDATE member SET online = TRUE WHERE id = #{id}
    """)
    void login(Member member);

    @Update("""
    UPDATE member SET online = FALSE WHERE id = #{id}
    """)
    void logout(Member login);

    @Select("""
    SELECT nickName
    FROM member
    WHERE online = TRUE
    """)
    List<String> getLiveUser();
            
    @Insert("""
            INSERT INTO member (id, password, nickName, email, securityQuestion, securityAnswer, profilePhoto)
            VALUES (#{id}, #{password}, #{nickName}, #{email}, #{securityQuestion}, #{securityAnswer}, #{profilePhoto})
            """)
    int kakaoInsert(Member member);

    @Select("""
select profilePhoto
from member
where id = #{id}
""")
    String getPhotoNameById(Member member);
            
    @Select("""
SELECT * FROM auth
WHERE loginId = #{id}
""")
    List<Auth> selectAuthById(String id);

    @Update("""
    UPDATE member
    SET password = #{s}
    WHERE id = #{id}
    """)
    void kakaoUpdatePassword(String id, String s);
}