package com.yan.service.impl;

import com.yan.common.pojo.EasyUITreeNode;
import com.yan.mapper.TbItemCatMapper;
import com.yan.pojo.TbItemCat;
import com.yan.pojo.TbItemCatExample;
import com.yan.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
            //根据parentId查询子节点列表
            TbItemCatExample example = new TbItemCatExample();
            TbItemCatExample.Criteria criteria = example.createCriteria();
            //设置查询条件
            criteria.andParentIdEqualTo(parentId);
            //执行查询
            List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
            //创建返回结果List
            List<EasyUITreeNode> resultList = new ArrayList<>();
            //把列表转换成EasyUITreeNode列表
            for (TbItemCat tbItemCat : list) {
                EasyUITreeNode node = new EasyUITreeNode();
                //设置属性
                node.setId(tbItemCat.getId());
                node.setText(tbItemCat.getName());
                node.setState(tbItemCat.getIsParent() ? "closed" : "open");
                //添加到结果列表
                resultList.add(node);
            }
            //返回结果
            return resultList;
    }
}
