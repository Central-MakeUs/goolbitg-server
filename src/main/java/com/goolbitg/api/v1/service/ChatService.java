package com.goolbitg.api.v1.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.goolbitg.api.v1.entity.chat.ChatMessage;
import com.goolbitg.api.v1.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage storeMessage(Long buyOrNotId, String userId, String username, String content) {
        ChatMessage chatMessage = ChatMessage.builder()
            .buyOrNotId(buyOrNotId)
            .userId(userId)
            .username(username)
            .content(content)
            .sentDateTime(LocalDateTime.now())
            .build();

        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

    public List<ChatMessage> getMessageHistory(Long buyOrNotId, Long lastId) {
        return chatMessageRepository.findHistory(buyOrNotId, lastId);
    }

}
