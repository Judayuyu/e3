package com.yan.sso.service.impl;

import com.yan.common.utils.E3Result;
import com.yan.mapper.TbUserMapper;
import com.yan.pojo.TbUser;
import com.yan.pojo.TbUserExample;
import com.yan.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

//用户注册处理
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Override
    public E3Result checkData(String param, int type) {
        //根据不同的type校验数据
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //1:用户名 2：手机号 3：邮箱
        if(type==1){
            criteria.andUsernameEqualTo(param);
        }else if(type==2){
            criteria.andPhoneEqualTo(param);
        }else if(type==3){
            criteria.andEmailEqualTo(param);
        }else {

            return E3Result.build(400,"数据类型出错");
        }
        //查看是否有与paarm相同的数据
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if(list!=null&&list.size()>0){
            //若有则返回false 表示用户名不可用
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }
    //将注册信息插入数据库
    @Override
    public E3Result register(TbUser tbUser) {
        //对数据有效性校验
        if(StringUtils.isEmpty(tbUser.getUsername())||
                StringUtils.isEmpty(tbUser.getPassword())||
               StringUtils.isEmpty(tbUser.getPassword())){
            return E3Result.build(400,"用户数据不完整");
        }
        //补全属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //对密码进行md5加密
        String s = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(s);
        //插入数据库
        tbUserMapper.insert(tbUser);
        return E3Result.ok();
    }
}
