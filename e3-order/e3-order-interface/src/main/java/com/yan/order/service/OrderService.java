package com.yan.order.service;

import com.yan.common.utils.E3Result;
import com.yan.order.pojo.OrderInfo;

public interface OrderService {
    E3Result creatOrder(OrderInfo orderInfo);

}
