package com.example.prj2be.domain;

import lombok.Data;

@Data
public class ChatMessage {

  public enum MessageType {
    ENTER, TALK
  }

  private MessageType type;
  private String sender;
  private String message;
}
