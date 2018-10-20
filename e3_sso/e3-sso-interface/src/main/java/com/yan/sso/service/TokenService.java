package com.yan.sso.service;

import com.yan.common.utils.E3Result;

public interface TokenService {
    E3Result getUserByToken(String token);
}
