package com.goolbitg.api.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.goolbitg.api.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "apple_id")
    private String appleId;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spending_type_id")
    private SpendingType spendingType;

    @Embedded
    @Builder.Default
    private Agreement agreement = new Agreement();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    @Builder.Default
    private UserSurvey survey = UserSurvey.getDefault();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    @Builder.Default
    private UserStat stat = UserStat.getDefault();


    public void setSpendingType(SpendingType spendingType) {
        this.spendingType = spendingType;
    }

    public void updateInfo(String nickname, LocalDate birthday, Gender gender) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateAgreement(
        boolean agreement1,
        boolean agreement2,
        boolean agreement3,
        boolean agreement4
    ) {
        if (agreement == null) {
            agreement = new Agreement();
        }
        agreement.update(agreement1, agreement2, agreement3, agreement4);
    }

    public int getRegisterStatus() {

        if (agreement.getAgreement1() == null) return 0;
        if (nickname == null) return 1;
        if (survey.getCheck1() == null) return 2;
        if (survey.getAvgIncomePerMonth() == null) return 3;
        if (survey.getPrimeUseDay() == null) return 4;
        if (stat.getChallengeCount() == 0) return 5;
        return 6;

    }

    public boolean isRequiredInfoCompleted() {
        return (agreement.getAgreement1() != null &&
            nickname != null &&
            survey.getCheck1() != null &&
            survey.getAvgIncomePerMonth() != null &&
            stat.getChallengeCount() > 0);

    }

}
