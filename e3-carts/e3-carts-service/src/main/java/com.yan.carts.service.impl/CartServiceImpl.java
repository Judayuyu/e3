package com.yan.carts.service.impl;

import com.yan.carts.service.CartService;
import com.yan.common.jedis.JedisClient;
import com.yan.common.utils.E3Result;
import com.yan.common.utils.JsonUtils;
import com.yan.mapper.TbItemMapper;
import com.yan.pojo.TbItem;
import com.yan.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//登录时购物车处理
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbItemMapper tbItemMapper;
    @Override
    public E3Result addCart(Long userId, Long itemId,int num) {
        //判断redis中是否有该用户的购物车是否已经有此商品
        //hashkey: "CART:" + userId    field:itemId   value:商品信息
        //一个userID可以对应多个field
        Boolean hexists = jedisClient.hexists("CART:" + userId, itemId + "");
        System.out.println(hexists);
        if(hexists){
            //如果存在则数量相加
            String json = jedisClient.hget("CART:" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            System.out.println("添加的商品："+tbItem.getTitle()+"数量:"+num);
            tbItem.setNum(tbItem.getNum()+num);
            //写回redis
            jedisClient.hset("CART:"+userId,itemId+"",JsonUtils.objectToJson(itemId));
            return E3Result.ok();
        }
        //如果不存在则查出商品信息存入redis
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //设置购物车数据量
        tbItem.setNum(num);
        //取一张图片
        String image = tbItem.getImage();
        if (StringUtils.isNotBlank(image)) {
            tbItem.setImage(image.split(",")[0]);
        }
        //添加到购物车列表  保存时间？？？
        jedisClient.hset( "CART:" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();

    }

    //合并购物车
    @Override
    public E3Result mergeCart(Long userId, List<TbItem> tbItemList) {
        //将cookie中的tbItemList加入redis中
        //遍历list，加入redis中的购物车,若存在则加数量，不存在则添加新的key
        //逻辑与addCart方法一样，直接调用
        for (TbItem tbItem:tbItemList) {
            addCart(userId,tbItem.getId(),tbItem.getNum());
        }
        return E3Result.ok();
    }
    //获取用户购物车列表的商品用于显示
    @Override
    public List<TbItem> getCartList(long userId) {
        //根据用户ID查询购物车列表
        List<String> list = jedisClient.hvals("CART:" + userId);
        System.out.println("用户购物车:"+list);
        List<TbItem> tbItemList=new ArrayList<>();
        for (String tbitem:list) {
            TbItem tbItem= JsonUtils.jsonToPojo(tbitem,TbItem.class);
            tbItemList.add(tbItem);
        }
        return tbItemList;
    }

    @Override
    public E3Result updateCartNum(long userId, long itemId, int num) {
        //更新
        String json = jedisClient.hget("CART:" + userId, itemId + "");
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        tbItem.setNum(num);
        jedisClient.hset("CART:" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(long userId, long itemId) {
        //删除购物车商品
        jedisClient.hdel("CART:"+userId,itemId+"");
        return E3Result.ok();

    }

    @Override
    public E3Result clearCartItem(long userId) {
        jedisClient.del("CART:"+userId);
        return E3Result.ok();
    }
}
