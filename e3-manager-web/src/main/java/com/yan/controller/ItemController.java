package com.yan.controller;

import com.yan.common.pojo.EasyUiDataGridResult;
import com.yan.common.utils.E3Result;
import com.yan.pojo.TbItem;
import com.yan.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{id}")
    public TbItem getItem(@PathVariable long id){
        TbItem itemById = itemService.getItemById(id);
        return itemById;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUiDataGridResult getItemList(Integer page,
                                            Integer rows){
        EasyUiDataGridResult result=itemService.getItemList(page,rows);
       return result;

    }

    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem tbItem,String desc){
        E3Result e3Result = itemService.addItem(tbItem, desc);
        return e3Result;
    }

    //商品后台编辑
    @RequestMapping(value = "/rest/item/update",method = RequestMethod.POST)
    public void update(TbItem tbItem){
        System.out.println(tbItem.getTitle());
    }
}
