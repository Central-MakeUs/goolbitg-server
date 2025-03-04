package com.goolbitg.api.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.v1.entity.Notice;

/**
 * NoticeRepository
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAllByReceiverIdOrderByIdDesc(String userId, Pageable pageReq);

    Page<Notice> findAllByReceiverIdAndTypeOrderByIdDesc(String userId, NoticeType type, Pageable pageReq);

}
