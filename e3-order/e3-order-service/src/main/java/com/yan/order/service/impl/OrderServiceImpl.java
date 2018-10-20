package com.yan.order.service.impl;

import com.yan.common.jedis.JedisClient;
import com.yan.common.utils.E3Result;
import com.yan.mapper.TbOrderItemMapper;
import com.yan.mapper.TbOrderMapper;
import com.yan.mapper.TbOrderShippingMapper;
import com.yan.order.pojo.OrderInfo;
import com.yan.order.service.OrderService;

import com.yan.pojo.TbOrderItem;
import com.yan.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

//订单处理
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private JedisClient jedisClient;
    //生成订单
    @Override
    public E3Result creatOrder(OrderInfo orderInfo) {
         //生成订单号 使用redis的incr生成
        if(!jedisClient.exists("ORDER_ID_Gen")){
            //设置key和刚开始的订单号
            jedisClient.set("ORDER_ID_Gen","100544");
        }
        String orderId = jedisClient.incr("ORDER_ID_Gen").toString();
        //补全属性
        orderInfo.setOrderId(orderId);
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入订单表
        tbOrderMapper.insert(orderInfo);
        //插入订单明细表
        //向订单明细表插入数据。
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            //生成明细id incr对存储在指定key的数值执行原子的加1操作。
            //
            //如果指定的key不存在，那么在执行incr操作之前，会先将它的值设定为0
            String odId = jedisClient.incr("ORDER_DETAIL_ID_GEN_KEY").toString();
            //补全pojo的属性
            tbOrderItem.setId(odId);
            tbOrderItem.setOrderId(orderId);
            //向明细表插入数据
            tbOrderItemMapper.insert(tbOrderItem);
        }
        //插入订单物流表
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        tbOrderShippingMapper.insert(orderShipping);
        //返回E3Result，包含订单号
        return E3Result.ok(orderId);
    }
}
