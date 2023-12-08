package com.example.prj2be.service;

import com.example.prj2be.domain.ChatMessage;
import com.example.prj2be.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MessageService {

  private final MessageMapper messageMapper;

  public Integer countSender(ChatMessage message) {
    return messageMapper.countSender(message);
  }

  public void addSender(ChatMessage message) {
    messageMapper.addSender(message);
  }

  public void dropSender(ChatMessage message) {
    messageMapper.dropSender(message);
  }
}
