package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.Notice;

/**
 * NoticeRepository
 */
public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
