package com.yan.portal.controller;

import com.yan.cat.service.ContentService;
import com.yan.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//首页展示
@Controller
public class IndexController {

    //从springmvc配置的资源文件中取出cid
    @Value("${Content_Cid}")
    private long cid;
    @Autowired
    private ContentService contentService;

    //取出轮播图内容
    @RequestMapping("/index")
    public String showIndex(Map<String,Object> map){
        //查询内容列表
        List<TbContent> ad1List = contentService.getContentListByCid(cid);


        map.put("ad1List",ad1List);
        return "index";
    }
}
