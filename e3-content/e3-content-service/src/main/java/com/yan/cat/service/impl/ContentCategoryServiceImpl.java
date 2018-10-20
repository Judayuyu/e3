package com.yan.cat.service.impl;

import com.yan.common.pojo.EasyUITreeNode;
import com.yan.cat.service.ContentCategoryService;
import com.yan.common.utils.E3Result;
import com.yan.mapper.TbContentCategoryMapper;
import com.yan.pojo.TbContentCategory;
import com.yan.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    @Override
    //添加内容种类 （广告位、展示栏、、）
    public List<EasyUITreeNode> getContentCatList(long parentId) {
        //根据parentID查询子节点列表
        TbContentCategoryExample tbContentCategoryExample=new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //转换成EasyUITreeNode的列表
        List<EasyUITreeNode>treeNodeList=new ArrayList<>();
        for(TbContentCategory tbContentCategory:tbContentCategories){
            //将每一个子节点放入list数组中
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent()?"cloesd":"open");
            node.setText(tbContentCategory.getName());
            treeNodeList.add(node);
        }
        return treeNodeList;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        //创建一个tb_content_category表对应的pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //设置pojo的属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //1(正常),2(删除)
        contentCategory.setStatus(1);
        //默认排序就是1
        contentCategory.setSortOrder(1);
        //新添加的节点一定是叶子节点
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入到数据库
        tbContentCategoryMapper.insert(contentCategory);
        System.out.println("返回的id:"+contentCategory.getId());
        //判断父节点的isparent属性。如果不是true改为true
        //根据parentid查询父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            //更新到数数据库
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果，返回E3Result，包含pojo
        return E3Result.ok(contentCategory);
    }
}
