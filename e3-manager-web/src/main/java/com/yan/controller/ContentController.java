package com.yan.controller;

import com.yan.cat.service.ContentService;
import com.yan.common.utils.E3Result;
import com.yan.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/save",method = RequestMethod.POST)
    public E3Result addContent(TbContent tbContent){
        E3Result e3Result = contentService.addContent(tbContent);
        return e3Result;
    }
}
