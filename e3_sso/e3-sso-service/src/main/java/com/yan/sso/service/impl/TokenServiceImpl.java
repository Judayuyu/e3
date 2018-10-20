package com.yan.sso.service.impl;

import com.yan.common.jedis.JedisClient;
import com.yan.common.utils.E3Result;
import com.yan.common.utils.JsonUtils;
import com.yan.pojo.TbUser;
import com.yan.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;
    @Override
    public E3Result getUserByToken(String token) {
        //根据cookie中的token取出用户信息
        String s = jedisClient.get("SESSION:"+token);
        System.out.println(s);
        //查看是否为空，若为空则说明用户信息已经过期
        if(StringUtils.isEmpty(s)){
            return E3Result.build(201,"用户信息已过期");
        }
        TbUser tbUser = JsonUtils.jsonToPojo(s, TbUser.class);
        //更新token过期时间
        jedisClient.expire("SESSION:"+token,1800);
        return E3Result.ok(tbUser);
    }
}
