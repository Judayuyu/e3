package com.yan.cat.service.impl;

import com.yan.cat.service.ContentService;
import com.yan.common.jedis.JedisClient;
import com.yan.common.utils.E3Result;
import com.yan.common.utils.JsonUtils;
import com.yan.mapper.TbContentMapper;
import com.yan.pojo.TbContent;
import com.yan.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 内容管理
 * 添加内容
 * 展示内容
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;
    @Override
    public E3Result addContent(TbContent tbContent) {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insert(tbContent);
        //缓存同步
        System.out.println("缓存同步");
        jedisClient.hdel("CONTENT_LIST",89+"");
        return E3Result.ok();
    }
    @Override
    public List<TbContent> getContentListByCid(long cid) {
        //查询缓存，若没有则查询数据库
        try {
            String json = jedisClient.hget("CONTENT_LIST", cid + "");
            if(!StringUtils.isEmpty(json)){
                List<TbContent> tbContents = JsonUtils.jsonToList(json, TbContent.class);
                return tbContents;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbContentExample tbContentExample=new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        //selectByExampleWithBLOBs 查出text列，即content那一列
        List<TbContent> tbContents = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
        //将查出的结果添加到缓存
        try {
            jedisClient.hset("CONTENT_LIST",cid+"",JsonUtils.objectToJson(tbContents));
            }catch (Exception e){
                    e.printStackTrace();
            }
        return tbContents;
    }
}
