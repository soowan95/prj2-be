package com.example.prj2be.controller;

import com.example.prj2be.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  private final SimpMessageSendingOperations sendingOperations;
  private final SimpMessagingTemplate template;

  @MessageMapping("/chat/enter")
  public void enter(ChatMessage message) {
    if (ChatMessage.MessageType.ENTER.equals(message.getType())) message.setMessage(message.getSender()+"님이 입장하였습니다.");
    sendingOperations.convertAndSend("/topic/chat/room", message);
  }

  @MessageMapping("topic/chat/room")
  public void talk(ChatMessage message) {
    template.convertAndSend("/topic/chat/room", message);
  }
}
