package com.goolbitg.api.v1.event.challenge;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.goolbitg.api.v1.entity.ChallengeStat;
import com.goolbitg.api.v1.entity.ChallengeStatId;
import com.goolbitg.api.v1.entity.DailyRecord;
import com.goolbitg.api.v1.entity.DailyRecordId;
import com.goolbitg.api.v1.entity.SpendingType;
import com.goolbitg.api.v1.entity.User;
import com.goolbitg.api.v1.entity.UserStat;
import com.goolbitg.api.v1.exception.UserException;
import com.goolbitg.api.v1.repository.ChallengeStatRepository;
import com.goolbitg.api.v1.repository.DailyRecordRepository;
import com.goolbitg.api.v1.repository.SpendingTypeRepository;
import com.goolbitg.api.v1.repository.UserRepository;

/**
 * ChallengeEventSubscriber
 */
@Component
public class ChallengeEventSubscriber {

    @Autowired
    private ChallengeStatRepository challengeStatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DailyRecordRepository dailyRecordRepository;
    @Autowired
    private SpendingTypeRepository spendingTypeRepository;

    @EventListener
    public void updateStatOnEnroll(ChallengeEnrollEvent event) {
        long challengeId = event.getChallengeId();
        String userId = event.getUserId();
        ChallengeStatId challengeStatId = new ChallengeStatId(challengeId, userId);
        ChallengeStat challengeStat = challengeStatRepository.findById(challengeStatId)
                .orElse(ChallengeStat.getDefault(challengeId, userId));
        UserStat userStat = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId))
                .getStat();

        if (challengeStat.getEnrollCount() == 0) {
            userStat.increaseChallengeCount();
        }
        challengeStat.enroll();

        challengeStatRepository.save(challengeStat);
    }

    @EventListener
    public void updateDailyRecordOnEnroll(ChallengeEnrollEvent event) {
        if (!event.isCarriedOver()) {
            String userId = event.getUserId();
            LocalDate date = event.getDate();
            DailyRecordId dailyRecordId = new DailyRecordId(userId, date);
            DailyRecord dailyRecord = dailyRecordRepository.findById(dailyRecordId)
                    .orElseGet(() -> DailyRecord.getDefault(userId, date));
            dailyRecord.enroll();
            dailyRecordRepository.save(dailyRecord);
        }
    }

    @EventListener
    public void updateStatOnCheck(ChallengeCheckEvent event) {
        ChallengeStatId challengeStatId = new ChallengeStatId(event.getChallengeId(), event.getUserId());
        ChallengeStat challengeStat = challengeStatRepository.findById(challengeStatId)
                .orElseThrow();
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> UserException.userNotExist(event.getUserId()));
        UserStat userStat = user.getStat();
        SpendingType currentType = user.getSpendingType();

        challengeStat.increaseCount();
        int newGuage = userStat.getAchievementGuage() + event.getReward();
        if (currentType.getGoal() != null && newGuage >= currentType.getGoal()) {
            SpendingType newType = spendingTypeRepository.findById(currentType.getId() + 1).orElseGet(null);
            newGuage = newGuage - currentType.getGoal();
            user.setSpendingType(newType);
        }
        userStat.setAchievementGuage(newGuage);
    }

    @EventListener
    public void updateDailyRecordOnCheck(ChallengeCheckEvent event) {
        String userId = event.getUserId();
        LocalDate date = event.getDate();
        DailyRecordId dailyRecordId = new DailyRecordId(userId, date);
        DailyRecord dailyRecord = dailyRecordRepository.findById(dailyRecordId)
                .orElseGet(() -> DailyRecord.getDefault(userId, date));
        dailyRecord.achieve(event.getReward());
        dailyRecordRepository.save(dailyRecord);
    }

    @EventListener
    public void updateStatOnCancel(ChallengeCancelEvent event) {
        long challengeId = event.getChallengeId();
        String userId = event.getUserId();
        ChallengeStatId challengeStatId = new ChallengeStatId(challengeId, userId);
        ChallengeStat challengeStat = challengeStatRepository.findById(challengeStatId)
                .orElseThrow();
        challengeStat.cancel();
    }
    
    @EventListener
    public void updateDailyRecordOnCancel(ChallengeCancelEvent event) {
        String userId = event.getUserId();
        LocalDate date = event.getDate();
        DailyRecordId dailyRecordId = new DailyRecordId(userId, date);
        DailyRecord dailyRecord = dailyRecordRepository.findById(dailyRecordId)
                .orElseGet(() -> DailyRecord.getDefault(userId, date));
        if (!event.isCheckedToday()) {
            dailyRecord.cancel();
        }
    }

}
