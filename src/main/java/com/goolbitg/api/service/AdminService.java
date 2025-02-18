package com.goolbitg.api.service;

import com.goolbitg.api.exception.CommonException;

/**
 * AdminService
 */
public interface AdminService {

    void authenticateAdmin(String password) throws CommonException;

}
