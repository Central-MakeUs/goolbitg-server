package com.goolbitg.api.v1.data;

import java.time.LocalDate;
import java.util.List;

import com.goolbitg.api.v1.entity.Challenge;
import com.goolbitg.api.v1.entity.ChallengeRecord;
import com.goolbitg.api.v1.entity.User;
import com.goolbitg.api.v1.repository.ChallengeRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeRepository;
import com.goolbitg.api.v1.repository.DailyRecordRepository;
import com.goolbitg.api.v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.v1.entity.DailyRecord;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.v1.service.BuyOrNotService;
import com.goolbitg.api.v1.service.ChallengeService;
import com.goolbitg.api.v1.service.NoticeService;
import com.goolbitg.api.v1.service.TimeService;
import com.goolbitg.api.v1.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * CronJobExecutor
 */
@Slf4j
@Component
public class CronJobExecutor {

    @Autowired
    private TimeService timeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DailyRecordRepository dailyRecordRepository;
    @Autowired
    private ChallengeRecordRepository challengeRecordRepository;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private BuyOrNotService buyOrNotService;


    @Transactional
    @Scheduled(cron = "1 0 0 * * ?")
    public void finishTheDay() {
        LocalDate today = timeService.getToday();
        LocalDate yesterday = today.minusDays(1);

        log.info("Finishing the day: " + yesterday.toString());
        // 1. Create daily records for all users
        for (User user : userRepository.findAll()) {
            int totalChallenges = challengeRecordRepository.countByUserIdAndDateAndStatus(user.getId(), today, ChallengeRecordStatus.WAIT);
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
            try {
                challengeService.failChallenge(record.getUserId(), record.getChallengeId(), yesterday);
            } catch (Exception e) {
                log.error("Canceling challenge failed: ", e);
            }
        }
        // 3. Re-calculate challenge stats
        challengeService.calculateAllChallengeStat(today);
        // 4. Update user stats
        for (User user : userRepository.findAll()) {
            try {
                userService.updateUserStat(user.getId(), yesterday);
            } catch (Exception e) {
                log.error("Updating user stat failed: ", e);
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 22 * * ?")
    public void sendChallengeAlarm() {
        LocalDate today = timeService.getToday();

        for (User user : userRepository.findAll()) {
            String userId = user.getId();
            List<ChallengeRecord> imcompleted = challengeRecordRepository.findAllIncompletedRecords(userId, today);
            if (imcompleted.size() > 0) {
                Challenge challenge = challengeRepository.findById(imcompleted.get(0).getChallengeId())
                    .orElseThrow(() -> new IllegalStateException("챌린지 없음"));

                log.info("챌린지 미완료 알림 전송", userId);
                String message = String.format("오늘 아직 [%s]을 달성하지 못했어요. 늦기 전에 인증해 주세요.", challenge.getTitle());
                noticeService.sendMessage(userId, message, NoticeType.CHALLENGE);
            } else {
                log.info("챌린지 미완료 알림 전송", userId);
                String message = String.format("오늘 챌린지를 모두 달성했어요! 내일도 잊지말고 기록해보세요.");
                noticeService.sendMessage(userId, message, NoticeType.CHALLENGE);

            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 */6 * * * *")
    public void sendBuyOrNotVoteAlarm() {
        for (BuyOrNotDto post : buyOrNotService.getTimeCompletedBuyOrNots()) {
            String message = String.format("{%s}에 대한 투표결과가 나왔어요. 어서 확인해보세요", post.getProductName());
            noticeService.sendMessage(post.getWriterId(), message, NoticeType.VOTE);
        }
    }

}
