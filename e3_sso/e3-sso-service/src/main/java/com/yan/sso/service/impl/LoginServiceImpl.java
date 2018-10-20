package com.yan.sso.service.impl;

import com.yan.common.jedis.JedisClient;
import com.yan.common.utils.E3Result;
import com.yan.common.utils.JsonUtils;
import com.yan.mapper.TbUserMapper;
import com.yan.pojo.TbUser;
import com.yan.pojo.TbUserExample;
import com.yan.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;
    @Override
    public E3Result userLogin(String username, String password) {
        //1.判断用户名密码是否正确
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if(list==null||list.size()==0){
            //如果没有该用户名的密码则返回错误
            return E3Result.build(400,"用户名或密码错误");
        }
        //2.取出用户信息并验证密码是否正确
        TbUser tbUser=list.get(0);
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())){
            //如果密码不一致，返回
            return E3Result.build(400,"用户名或密码错误");
        }
        //3.验证通过，生成token
        String token= UUID.randomUUID().toString();
        //4.把用户信息写入redis，key：token  value:用户信息
        tbUser.setPassword(null);
        System.out.println("用户登录："+tbUser.getUsername());
        jedisClient.set("SESSION:"+token,JsonUtils.objectToJson(tbUser));
        //5.设置Session的过期时间
        jedisClient.expire("SESSION:"+token,18000);
        //6.把token返回
        return E3Result.ok(token);
    }
}
