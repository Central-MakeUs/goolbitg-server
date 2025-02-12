package com.goolbitg.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.goolbitg.api.entity.Notice;
import com.goolbitg.api.entity.RegistrationToken;
import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.repository.NoticeRepository;
import com.goolbitg.api.repository.RegistrationTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * NoticeServiceImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private final NoticeRepository noticeRepository;
    @Autowired
    private final RegistrationTokenRepository registrationTokenRepository;
    @Autowired
    private final TimeService timeService;

    @Override
    @Transactional
    public void sendMessage(String userId, String message, NoticeType type) {
        Notice notice = Notice.builder()
            .receiverId(userId)
            .message(message)
            .publishedAt(timeService.getNow())
            .type(type)
            .build();

        noticeRepository.save(notice);

        for (RegistrationToken token : registrationTokenRepository.findAllByUserId(userId)) {
            sendMessageInner(token.getRegistrationToken(), message);
        }
    }

    private void sendMessageInner(String registrationToken, String message) {
        Message noticeMessage = Message.builder()
            .setNotification(Notification.builder()
                .setTitle("굴비잇기")
                .setBody(message)
                .build())
            .setToken(registrationToken)
            .build();

        try {
            FirebaseMessaging.getInstance().send(noticeMessage);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send notice message", e);
        }
    }

}
