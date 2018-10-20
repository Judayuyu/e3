package com.yan.order.interceptor;

import com.yan.carts.service.CartService;
import com.yan.common.utils.CookieUtils;
import com.yan.common.utils.E3Result;
import com.yan.common.utils.JsonUtils;
import com.yan.pojo.TbItem;
import com.yan.pojo.TbUser;
import com.yan.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//生成订单之前拦截请求判断是否登录
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${SSO_URL}")
    private String SSO_URL;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //判断用户是否登录
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        if(StringUtils.isEmpty(token)){
            //若未登录跳转到登陆页面 由于登录页面不再同一个工程，用redirect
            httpServletResponse.sendRedirect(SSO_URL+"/page/login?redirect="+httpServletRequest.getRequestURL());
            return false;
        }
        //若已经登录，则根据token取用户信息
        E3Result result = tokenService.getUserByToken(token);
        if(result.getStatus()!=200){
            //用户信息已经过期也跳转
            httpServletResponse.sendRedirect(SSO_URL+"/page/login?redirect="+httpServletRequest.getRequestURL());
            return false;
        }
        TbUser tbUser=(TbUser) result.getData();
        httpServletRequest.setAttribute("user",tbUser);
        //判断cookie中是否有购物车数据，若有则取出来合并
        String cart = CookieUtils.getCookieValue(httpServletRequest, "cart", true);
        if(!StringUtils.isEmpty(cart)){
            System.out.println("cart:"+cart);
            //若有则合并
            cartService.mergeCart(tbUser.getId(),JsonUtils.jsonToList(cart,TbItem.class));
        }
        //若没有则不需要合并，在redis中可获得用户购物车数据，直接放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
