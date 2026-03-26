package com.goolbitg.api.v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByIdGreaterThanOrderByIdAsc(Long buyOrNotId, int lastId);

}
