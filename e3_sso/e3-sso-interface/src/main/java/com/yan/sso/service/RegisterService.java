package com.yan.sso.service;

import com.yan.common.utils.E3Result;
import com.yan.pojo.TbUser;

public interface RegisterService {
    //type 值为1,2,3  1代表查询name 2-phone 3-email
    E3Result checkData(String param,int type);
    E3Result register(TbUser tbUser);
}
