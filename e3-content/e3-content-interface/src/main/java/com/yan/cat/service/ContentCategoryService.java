package com.yan.cat.service;

import com.yan.common.pojo.EasyUITreeNode;
import com.yan.common.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCatList(long parentId);
    E3Result addContentCategory(long parentId,String name);
}
