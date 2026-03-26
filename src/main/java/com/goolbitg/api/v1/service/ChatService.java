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

    public ChatMessage storeMessage(Long buyOrNotId, String userId, String content) {
        ChatMessage chatMessage = ChatMessage.builder()
            .buyOrNotId(buyOrNotId)
            .userId(userId)
            .content(content)
            .sentDateTime(LocalDateTime.now())
            .build();

        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

    public List<ChatMessage> getMessageHistory(Long buyOrNotId, int lastId) {
        return chatMessageRepository.findAllByIdGreaterThanOrderByIdAsc(buyOrNotId, lastId);
    }

}
