package com.goolbitg.api.v1.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goolbitg.api.v1.entity.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * @param buyOrNotId
     * @param lastId
     * @return
     */
    @Query("SELECT m FROM ChatMessage m WHERE m.buyOrNotId = :buyOrNotId AND m.id > :lastId ORDER BY m.id ASC")
    List<ChatMessage> findHistory(
        @Param("buyOrNotId") Long buyOrNotId,
        @Param("lastId") int lastId
    );

}
