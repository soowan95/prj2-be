package com.example.prj2be.mapper;

import com.example.prj2be.domain.ChatMessage;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MessageMapper {

  @Select("""
  SELECT COUNT(sender)
  FROM chat
  WHERE sender = #{sender}
  """)
  Integer countSender(ChatMessage message);

  @Insert("""
  INSERT INTO chat (sender) VALUE (#{sender})
  """)
  void addSender(ChatMessage message);

  @Insert("""
  INSERT INTO chat (sender) VALUE (#{sender})
  """)
  void addByNickName(String nickName);

  @Delete("""
  DELETE FROM chat WHERE sender = #{sender}
  """)
  void dropSender(ChatMessage message);

  @Delete("""
  DELETE FROM chat WHERE sender = #{nickName}
  """)
  void dropByNickName(String nickName);
}
