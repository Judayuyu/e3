package com.yan.item.controller;

import com.yan.pojo.TbItem;
import com.yan.pojo.TbItemDesc;
import com.yan.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pojo.Item;

/**
 * 商品详情页面内容显示
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable("itemId") Long id, Model model){
        TbItem item = itemService.getItemById(id);
        Item item1=new Item(item);
        System.out.println("商品详情页面内容显示");
        TbItemDesc itemDescByIc = itemService.getItemDescByIc(id);
        model.addAttribute("item",item1);
        model.addAttribute("itemDesc",itemDescByIc);
        return "item";
    }
}
