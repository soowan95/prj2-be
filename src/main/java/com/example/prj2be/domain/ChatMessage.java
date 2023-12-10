package com.example.prj2be.domain;

import lombok.Data;

@Data
public class ChatMessage {

  public enum MessageType {
    ENTER, TALK, LEAVE
  }

  private MessageType type;
  private String sender;
  private String message;
  private Boolean isOnline;
}
