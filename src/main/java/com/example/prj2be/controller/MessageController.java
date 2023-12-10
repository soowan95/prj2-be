package com.example.prj2be.controller;

import com.example.prj2be.domain.ChatMessage;
import com.example.prj2be.domain.Member;
import com.example.prj2be.service.MemberService;
import com.example.prj2be.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

  private final SimpMessageSendingOperations sendingOperations;
  private final MessageService messageService;
  private final MemberService memberService;

  @MessageMapping("/chat/enter")
  public void enter(ChatMessage message) {
    List<String> liveUser = memberService.getLiveUser();
    if (messageService.countSender(message) == 0 && ChatMessage.MessageType.ENTER.equals(message.getType()) && message.getSender() != null) {
      message.setIsOnline(liveUser);
      message.setMessage(message.getSender() + "님이 입장하였습니다.");
      sendingOperations.convertAndSend("/topic/chat/room", message);
    }
    if (messageService.countSender(message) == 0 && message.getSender() != null) messageService.addSender(message);
  }

  @MessageMapping("/chat/leave")
  public void leave(ChatMessage message) {
    List<String> liveUser = memberService.getLiveUser();
    if (ChatMessage.MessageType.LEAVE.equals(message.getType()) && message.getSender() != null) {
      message.setMessage(message.getSender() + "님이 퇴장하였습니다.");
      message.setIsOnline(liveUser);
      sendingOperations.convertAndSend("/topic/chat/room", message);
    }
    messageService.dropSender(message);
  }
}
