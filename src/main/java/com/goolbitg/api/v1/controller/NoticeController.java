package com.goolbitg.api.v1.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.goolbitg.api.NoticeApi;
import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.model.PaginatedNoticeDto;
import com.goolbitg.api.v1.security.AuthUtil;
import com.goolbitg.api.v1.service.NoticeService;

import lombok.RequiredArgsConstructor;

/**
 * NoticeController
 */
@RestController
@RequiredArgsConstructor
public class NoticeController implements NoticeApi {

    @Autowired
    private final NoticeService noticeService;

    @Override
    public ResponseEntity<PaginatedNoticeDto> getNotices(@Valid Integer page, @Valid Integer size,
            @Valid NoticeType type) throws Exception {
        String userId = AuthUtil.getLoginUserId();

        PaginatedNoticeDto result = noticeService.getNotices(page, size, userId, type);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> readNotice(Long noticeId) throws Exception {
        noticeService.readNotice(noticeId);

        return ResponseEntity.ok().build();
    }

}
