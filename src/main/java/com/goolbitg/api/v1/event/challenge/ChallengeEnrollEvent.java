package com.goolbitg.api.v1.event.challenge;

import java.time.LocalDate;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * ChallengeEnrollEvent
 */
@Getter
public class ChallengeEnrollEvent extends ApplicationEvent {

    private String userId;
    private long challengeId;
    private LocalDate date;
    private boolean carriedOver;

    public ChallengeEnrollEvent(
        Object source,
        String userId,
        long challengeId,
        LocalDate date,
        boolean carriedOver
    ) {
        super(source);
        this.userId = userId;
        this.challengeId = challengeId;
        this.date = date;
        this.carriedOver = carriedOver;
    }

}
