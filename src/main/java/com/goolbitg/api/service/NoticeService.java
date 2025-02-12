package com.goolbitg.api.service;

import com.goolbitg.api.model.NoticeType;

/**
 * NoticeService
 */
public interface NoticeService {

    void sendMessage(String userId, String message, NoticeType type);
}
