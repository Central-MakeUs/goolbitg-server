package com.goolbitg.api.v1.event.challenge;

import java.time.LocalDate;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * ChallengeCancelEvent
 */
@Getter
public class ChallengeCancelEvent extends ApplicationEvent {

    private String userId;
    private long challengeId;
    private LocalDate date;
    private boolean checkedToday;

    public ChallengeCancelEvent(
        Object source,
        String userId,
        long challengeId,
        LocalDate date,
        boolean checkedToday
    ) {
        super(source);
        this.userId = userId;
        this.challengeId = challengeId;
        this.date = date;
        this.checkedToday = checkedToday;
    }

}
