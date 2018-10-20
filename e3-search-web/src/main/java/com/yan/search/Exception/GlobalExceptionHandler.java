package com.yan.search.Exception;

import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

//全局异常处理
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object o, Exception e) {
        //打印控制台
        e.printStackTrace();
        //写日志
        logger.debug("测试输出的日志.....");
        logger.info("系统发生异常");
        logger.warn("warn...");
        logger.error("系统发生异常:",e);
        //发邮件，发短信
        //使用jmail工具包
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("error/exception");
        //显示错误页面
        return null;
    }
}
