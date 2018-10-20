package com.yan.sso.service;

import com.yan.common.utils.E3Result;

public interface LoginService {
    E3Result userLogin(String username,String password);
}
