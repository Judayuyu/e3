package com.yan.carts.controller;

import com.yan.carts.service.CartService;
import com.yan.common.utils.CookieUtils;
import com.yan.common.utils.E3Result;
import com.yan.common.utils.JsonUtils;
import com.yan.pojo.TbItem;
import com.yan.pojo.TbUser;
import com.yan.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;
    //添加购物车处理
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable("itemId")Long id,
                          @RequestParam(value = "num",defaultValue = "1")Integer num,
                          HttpServletRequest request,
                          HttpServletResponse response){
        //判断用户是否登录  若已经登录则拦截器会在request中加入user属性
        TbUser user = (TbUser) request.getAttribute("user");
        if(user!=null){
            System.out.println(user.getUsername());
            //如果登录，将信息写入redis
            cartService.addCart(user.getId(),id,num);
            return "cartSuccess";

        }
        //若未登录使用cookie
        //从cookie中取出购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断商品是否存在
        boolean flag=false;
        for(TbItem tbItem:cartList){
            if(tbItem.getId()==id.longValue()){
                flag=true;
                //存在则数量相加
                tbItem.setNum(tbItem.getNum()+num);
                break;
            }
        }
        if(!flag){
            //如果不存在则根据id查出tbItem信息，写入cookie
            TbItem tbItem=itemService.getItemById(id);
            //设置商品数量
            tbItem.setNum(num);
            //取一张图片
            String image = tbItem.getImage();
            if (!StringUtils.isEmpty(image)) {
                tbItem.setImage(image.split(",")[0]);
            }
            //把商品添加到商品列表
            cartList.add(tbItem);
        }
        //43200s
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), 43200, true);
        return "cartSuccess";

    }
    //根据cookie获取商品信息
    public List<TbItem> getCartListFromCookie(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if(StringUtils.isEmpty(json)){
            return new ArrayList<>();
        }
        List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);
        return tbItems;

    }
    //显示购物车页面
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request, HttpServletResponse response){
        //判断用户是否登录
        TbUser tbUser= (TbUser) request.getAttribute("user");
        //取出cookie中的购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        System.out.println(cartList.toString());
        //如果登录则从cookie中获取购物车列表和redis中的购物车合并
        if(tbUser!=null){
            System.out.println("showCartList user:"+tbUser.getUsername());

            //如果已经登录则合并,存入redis
            cartService.mergeCart(tbUser.getId(),cartList);
            //把cookie的购物车删除
            CookieUtils.deleteCookie(request,response,"cart");
            System.out.println("cookie的购物车删除");
            cartList = cartService.getCartList(tbUser.getId());

        }
        //将购物车信息传递给页面
        request.setAttribute("cartList",cartList);

        return "cart";
    }
    //更新购物车
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
                                  HttpServletRequest request,HttpServletResponse response){
        //判断用户是否登录
        TbUser tbUser= (TbUser) request.getAttribute("user");
        if(tbUser!=null){
            //若登陆
            cartService.updateCartNum(tbUser.getId(),itemId,num);
            return E3Result.ok();
        }
        //从cookie中获取购物车列表
        List<TbItem> list = getCartListFromCookie(request);
        //找到相应id的商品
        for(TbItem tbItem:list){
            if(tbItem.getId()==itemId.longValue()){
                tbItem.setNum(num);
                break;
            }
        }
        //写回cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), 43200, true);
        return E3Result.ok();
    }
    //删除购物车
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
                                 HttpServletResponse response){
        //判断用户是否登录
        TbUser tbUser= (TbUser) request.getAttribute("user");
        if(tbUser!=null){
            //若登陆
            cartService.deleteCartItem(tbUser.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        List<TbItem> tbItemList = getCartListFromCookie(request);
        for(TbItem tbItem:tbItemList){
            if(tbItem.getId()==itemId.longValue()){
                //从cookie中删除该商品
                tbItemList.remove(tbItem);
                break;
            }
        }
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(tbItemList), 43200, true);

        return "redirect:/cart/cart.html";
    }

}
