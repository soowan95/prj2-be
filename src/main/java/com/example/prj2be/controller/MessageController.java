package com.example.prj2be.controller;

import com.example.prj2be.domain.ChatMessage;
import com.example.prj2be.service.MessageService;
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
  private final MessageService messageService;

  @MessageMapping("/chat/enter")
  public void enter(ChatMessage message) {
    if (messageService.countSender(message) == 0 && ChatMessage.MessageType.ENTER.equals(message.getType()) && message.getSender() != null) {
      message.setMessage(message.getSender() + "님이 입장하였습니다.");
      message.setIsOnline(true);
      sendingOperations.convertAndSend("/topic/chat/room", message);
    }
    if (messageService.countSender(message) == 0 && message.getSender() != null) messageService.addSender(message);
  }

  @MessageMapping("/chat/leave")
  public void leave(ChatMessage message) {
    if (ChatMessage.MessageType.LEAVE.equals(message.getType()) && message.getSender() != null) message.setMessage(message.getSender() + "님이 퇴장하였습니다.");
    message.setIsOnline(false);
    sendingOperations.convertAndSend("/topic/chat/room", message);
    messageService.dropSender(message);
  }

  @MessageMapping("/topic/chat/room")
  public void talk(ChatMessage message) {
    message.setIsOnline(true);
    template.convertAndSend("/topic/chat/room", message);
  }
}
