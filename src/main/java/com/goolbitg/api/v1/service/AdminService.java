package com.goolbitg.api.v1.service;

import com.goolbitg.api.v1.exception.CommonException;

/**
 * AdminService
 */
public interface AdminService {

    void authenticateAdmin(String password) throws CommonException;

}
