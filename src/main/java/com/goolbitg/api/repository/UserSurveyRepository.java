package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.goolbitg.api.entity.UserSurvey;

/**
 * UserSurveyRepository
 */
public interface UserSurveyRepository extends JpaRepository<UserSurvey, String> {

}
