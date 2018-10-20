package com.yan.search.service.mapper;

import com.yan.common.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {

    List<SearchItem> getItemList();

    //添加商品的时候根据id查询数据库同步到索引库
    SearchItem getItemById(long itemId);
}
