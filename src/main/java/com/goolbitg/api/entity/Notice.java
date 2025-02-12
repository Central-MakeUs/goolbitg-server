package com.goolbitg.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.model.NoticeType;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Notice
 */
@Entity
@Table(name = "notices")
@Getter
public class Notice {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "receiver_id")
    @NonNull
    private String receiverId;

    @Column(name = "message")
    @NonNull
    private String message;

    @Column(name = "published_at")
    @NonNull
    private LocalDateTime publishedAt;

    @Column(name = "type")
    @NonNull
    private NoticeType type;

    @Column(name = "read")
    private Boolean read;

    @Builder
    public Notice(
        String receiverId,
        String message,
        LocalDateTime publishedAt,
        NoticeType type
    ) {
        this.receiverId = receiverId;
        this.message = message;
        this.publishedAt = publishedAt;
        this.type = type;
        read = false;
    }

}
