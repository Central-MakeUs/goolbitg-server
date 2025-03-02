package com.goolbitg.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.goolbitg.api.entity.Notice;
import com.goolbitg.api.entity.RegistrationToken;
import com.goolbitg.api.exception.NoticeException;
import com.goolbitg.api.model.NoticeDto;
import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.model.PaginatedNoticeDto;
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

    /* ------------ Implementations ----------- */

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

    @Override
    public PaginatedNoticeDto getNotices(Integer page, Integer size, String userId, NoticeType type) {
        Pageable pageReq = PageRequest.of(page, size);
        Page<Notice> result;
        if (type == null) {
            result = noticeRepository.findAllByReceiverId(userId, pageReq);
        } else {
            result = noticeRepository.findAllByReceiverIdAndType(userId, type, pageReq);
        }

        return getPaginatedNoticeDto(result);
    }

    @Override
    @Transactional
    public void readNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> NoticeException.noticeNotExist(noticeId));

        if (notice.getRead())
            throw NoticeException.readAlready(noticeId);

        notice.read();

        noticeRepository.save(notice);
    }

    /* ----------- DTO Mappers ------------ */

    private NoticeDto getNoticeDto(Notice notice) {
        NoticeDto dto = new NoticeDto();
        dto.setId(notice.getId());
        dto.setReceiverId(notice.getReceiverId());
        dto.setMessage(notice.getMessage());
        dto.setPublishDateTime(notice.getPublishedAt().toString());
        dto.setRead(notice.getRead());
        return dto;
    }

    private PaginatedNoticeDto getPaginatedNoticeDto(Page<Notice> result) {
        PaginatedNoticeDto dto = new PaginatedNoticeDto();
        dto.setPage(result.getNumber());
        dto.setSize((int)result.getNumberOfElements());
        dto.setTotalSize((int)result.getTotalElements());
        dto.setTotalPages(result.getTotalPages());
        dto.setItems(
            result.getContent().stream().map(i -> getNoticeDto(i)).toList()
        );

        return dto;
    }


    /* ------------- Utils ---------------- */

    private void sendMessageInner(String registrationToken, String message) {
        Message noticeMessage = Message.builder()
            .setNotification(Notification.builder()
                .setTitle("굴비잇기")
                .setBody(message)
                .build())
            .setToken(registrationToken)
            .setApnsConfig(ApnsConfig.builder()
                .setAps(Aps.builder()
                    .setContentAvailable(true)
                    .putCustomData("apns-push-type", "background")
                    .putCustomData("apns-priority", 5)
                    .build())
                .build())
            .build();

        try {
            FirebaseMessaging.getInstance().send(noticeMessage);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send notice message", e);
        }
    }

}
