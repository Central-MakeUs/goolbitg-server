package com.goolbitg.api.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Getter;

/**
 * Agreemenet
 */
@Embeddable
@Getter
public class Agreement {

    @Column(name = "allow_push_notification")
    private Boolean pushNotificationAgreement;

    @Column(name = "agreement1")
    private Boolean agreement1;

    @Column(name = "agreement2")
    private Boolean agreement2;

    @Column(name = "agreement3")
    private Boolean agreement3;

    @Column(name = "agreement4")
    private Boolean agreement4;

    public void update(
        boolean agreement1,
        boolean agreement2,
        boolean agreement3,
        boolean agreement4
    ) {
        this.agreement1 = agreement1;
        this.agreement2 = agreement2;
        this.agreement3 = agreement3;
        this.agreement4 = agreement4;
    }

    public void allowPushNotification() {
        pushNotificationAgreement = true;
    }

}
