package com.yan.cat.service;

import com.yan.common.utils.E3Result;
import com.yan.pojo.TbContent;

import java.util.List;

public interface ContentService {

    E3Result addContent(TbContent tbContent);
    List<TbContent> getContentListByCid(long cid);
}
