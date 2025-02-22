package com.goolbitg.api.service;

import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.model.PaginatedNoticeDto;

/**
 * NoticeService
 */
public interface NoticeService {

    void sendMessage(String userId, String message, NoticeType type);
    PaginatedNoticeDto getNotices(Integer page, Integer size, String userId, NoticeType type);
    void readNotice(Long noticeId);

}
