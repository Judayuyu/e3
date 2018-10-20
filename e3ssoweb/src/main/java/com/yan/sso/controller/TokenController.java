package com.yan.sso.controller;

import com.yan.common.utils.E3Result;
import com.yan.common.utils.JsonUtils;
import com.yan.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//根据token查询用户信息
@Controller
public class TokenController {
    @Autowired
    private TokenService tokenService;
    //指定响应json
    @RequestMapping(value = "/user/token/{token}",produces = "application/json;charset=utf-8")
    @ResponseBody
     public String getUserByToken(@PathVariable("token")String token,
                                    @RequestParam("callback")String callback){
        E3Result result = tokenService.getUserByToken(token);
        //如果是jsonp请求，则包含callback参数
        if(!StringUtils.isEmpty(callback)){
            ////把结果封装成一个js语句响应
            //			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            //			mappingJacksonValue.setJsonpFunction(callback);
            //			return mappingJacksonValue;
            //若callback存在，则把结果封装成一个js语句
            return callback+"("+JsonUtils.objectToJson(result)+")";
        }
        return JsonUtils.objectToJson(result);
    }

}
