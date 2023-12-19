package com.example.prj2be.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChatMessage {

  public enum MessageType {
    ENTER, TALK, LEAVE
  }

  private MessageType type;
  private String sender;
  private String message;
  private String profile;
  private Integer onlineCount;
  private List<String> isOnline;
}
