package com.yan.carts.service;

import com.yan.common.utils.E3Result;
import com.yan.pojo.TbItem;

import java.util.List;

public interface CartService {
    E3Result addCart(Long userId,Long itemId,int num);
    E3Result mergeCart(Long userId, List<TbItem>tbItemList);
    List<TbItem> getCartList(long userId);
    E3Result updateCartNum(long userId,long itemId,int num);
    E3Result deleteCartItem(long userId, long itemId);
    E3Result clearCartItem(long userId);
}
