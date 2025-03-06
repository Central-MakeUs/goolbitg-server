package com.goolbitg.api.v1.event.challenge;

import java.time.LocalDate;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * ChallengeCheckEvent
 */
@Getter
public class ChallengeCheckEvent extends ApplicationEvent {

    private String userId;
    private long challengeId;
    private LocalDate date;
    private int reward;

    public ChallengeCheckEvent(
        Object source,
        String userId,
        long challengeId,
        LocalDate date,
        int reward
    ) {
        super(source);
        this.userId = userId;
        this.challengeId = challengeId;
        this.date = date;
        this.reward = reward;
    }

}
