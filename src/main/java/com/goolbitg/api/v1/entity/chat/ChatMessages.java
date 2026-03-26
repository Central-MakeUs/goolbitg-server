package com.goolbitg.api.v1.entity.chat;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.v1.entity.BaseEntity;
import com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_messages")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessages {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "content")
    private String content;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDateTime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSentDateTime(LocalDateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }

}

