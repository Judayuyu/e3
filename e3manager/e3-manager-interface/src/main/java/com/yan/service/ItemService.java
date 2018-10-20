package com.yan.service;

import com.yan.common.pojo.EasyUiDataGridResult;
import com.yan.common.utils.E3Result;
import com.yan.pojo.TbItem;
import com.yan.pojo.TbItemDesc;

import java.util.List;

public interface ItemService {
    public TbItem getItemById(long id);
    EasyUiDataGridResult getItemList(int page,int pagesize);
    public E3Result addItem(TbItem tbItem,String desc);
    TbItemDesc getItemDescByIc(long itemId);
}
