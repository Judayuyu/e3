package com.yan.sso.controller;


import com.yan.common.utils.E3Result;
import com.yan.pojo.TbUser;
import com.yan.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//用户注册数据校验
@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }

    //校验注册信息
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable("param")String param,@PathVariable("type")int type){
        E3Result e3Result = registerService.checkData(param, type);
        return e3Result;

    }
    //处理注册信息
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser tbUser){
        return registerService.register(tbUser);
    }


}
