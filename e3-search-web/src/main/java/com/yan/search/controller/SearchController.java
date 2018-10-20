package com.yan.search.controller;

import com.yan.common.pojo.SearchResult;
import com.yan.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


//商品搜索
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Value("${ROWS}")
    private Integer ROWS;
    @RequestMapping("/search")
    public String SearchItemList(String keyword,
                                 @RequestParam(defaultValue = "1")Integer page,
                                 Model model) throws Exception {
        //将keyword转码后在页面回显
        keyword=new String(keyword.getBytes("iso-8859-1"),"utf-8");
        SearchResult search = searchService.search(keyword, page, ROWS);
        model.addAttribute("query",keyword);
        model.addAttribute("totalPages",search.getTotalPages());
        model.addAttribute("page",page);
        model.addAttribute("recourdCount",search.getRecordCount());
        model.addAttribute("itemList",search.getItemList());
        return "search";

    }
}
