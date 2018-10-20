package com.yan.order.pojo;

import com.yan.pojo.TbOrder;
import com.yan.pojo.TbOrderItem;
import com.yan.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;
//用于封装订单信息的表单数据
public class OrderInfo extends TbOrder implements Serializable {
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
