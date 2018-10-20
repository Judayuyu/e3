package com.yan.controller;

import com.yan.common.utils.E3Result;
import com.yan.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//导入商品数据到索引库
@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importItemList() throws Exception {
        E3Result e3Result = searchItemService.importAllItems();
        return e3Result;
    }
}
