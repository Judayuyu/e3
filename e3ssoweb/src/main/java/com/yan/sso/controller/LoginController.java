package com.yan.sso.controller;

import com.yan.common.utils.CookieUtils;
import com.yan.common.utils.E3Result;
import com.yan.pojo.TbUser;
import com.yan.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;
    @RequestMapping("/page/login")
    public String showLogin(String redirect,Model model) {
        System.out.println("回调url"+redirect);
        model.addAttribute("redirect",redirect);
        return "login";
    }
    //处理登录请求
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password,
                          HttpServletRequest request,
                          HttpServletResponse response){
        E3Result result = loginService.userLogin(username, password);
        System.out.println(result.getStatus());
        //判断是否登录成功
        if(result.getStatus()==200){
            String token = result.getData().toString();
            //登录成功将token写入cookie  浏览器关闭则删除cookie
            CookieUtils.setCookie(request,response,"token",token);
        }
        return result;
    }
}
