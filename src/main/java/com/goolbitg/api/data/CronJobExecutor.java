package com.goolbitg.api.data;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.entity.ChallengeRecord;
import com.goolbitg.api.entity.DailyRecord;
import com.goolbitg.api.entity.User;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.repository.ChallengeRecordRepository;
import com.goolbitg.api.repository.DailyRecordRepository;
import com.goolbitg.api.repository.UserRepository;
import com.goolbitg.api.service.ChallengeService;
import com.goolbitg.api.service.TimeService;
import com.goolbitg.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CronJobExecutor
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CronJobExecutor {

    @Autowired
    private final TimeService timeService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final DailyRecordRepository dailyRecordRepository;
    @Autowired
    private final ChallengeRecordRepository challengeRecordRepository;
    @Autowired
    private final ChallengeService challengeService;
    @Autowired
    private final UserService userService;


    @Transactional
    @Scheduled(cron = "1 0 0 * * ?")
    public void finishTheDay() {
        LocalDate today = timeService.getToday();
        LocalDate yesterday = today.minusDays(1);

        log.info("Finishing the day: " + yesterday.toString());
        // 1. Create daily records for all users
        for (User user : userRepository.findAll()) {
            int totalChallenges = challengeRecordRepository.countByUserIdAndDate(user.getId(), today);
            dailyRecordRepository.save(DailyRecord.builder()
                    .userId(user.getId())
                    .date(today)
                    .totalChallenges(totalChallenges)
                    .saving(0)
                    .achievedChallenges(0)
                    .build());
        }
        // 2. Turn status to FAIL for all uncompleted challenges
        for (ChallengeRecord record : challengeRecordRepository.findAllByDateAndStatus(yesterday, ChallengeRecordStatus.WAIT)) {
            record.fail();
            try {
                // NOTE: Mabye I should use other method to cancel leftovers,
                // cause cancelChallenge method declines enroll count.
                challengeService.cancelChallenge(record.getUserId(), record.getChallengeId(), today);
            } catch (Error e) {
                log.error("Canceling challenge failed: ", e);
            }
        }
        // 3. Re-calculate challenge stats
        challengeService.calculateAllChallengeStat(today);
        // 4. Update user stats
        for (User user : userRepository.findAll()) {
            userService.updateUserStat(user.getId(), yesterday);
        }
    }

}
