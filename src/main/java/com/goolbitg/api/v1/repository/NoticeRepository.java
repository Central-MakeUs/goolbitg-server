package com.goolbitg.api.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.Notice;
import com.goolbitg.api.model.NoticeType;

/**
 * NoticeRepository
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAllByReceiverId(String userId, Pageable pageReq);

    Page<Notice> findAllByReceiverIdAndType(String userId, NoticeType type, Pageable pageReq);

}
