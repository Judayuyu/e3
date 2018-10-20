package com.yan.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yan.common.jedis.JedisClient;
import com.yan.common.pojo.EasyUiDataGridResult;
import com.yan.common.utils.E3Result;
import com.yan.common.utils.IDUtils;
import com.yan.common.utils.JsonUtils;
import com.yan.mapper.TbItemDescMapper;
import com.yan.mapper.TbItemMapper;
import com.yan.pojo.TbItem;
import com.yan.pojo.TbItemDesc;
import com.yan.pojo.TbItemExample;
import com.yan.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicDestination;


    @Override
    public TbItem getItemById(long id) {
        //查看商品详情时先查询缓存
        try {
            String json = jedisClient.get("ITEM_INFO:" + id + ":BASE");
            if(!StringUtils.isEmpty(json)){
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //缓存没有则查询数据库
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        //把结果添加缓存
        if(tbItem!=null){
            try {
                //以id为key
                //缓存目录为 ITEN_INFO
                //                    id
                //                      "ITEM_INFO:"+id+":BASE"
                jedisClient.set("ITEM_INFO:"+id+":BASE",JsonUtils.objectToJson(tbItem));
                //设置过期时间  一小时
                jedisClient.expire("ITEM_INFO:"+id+":BASE",3600);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return tbItem ;
    }
    @Override
    public TbItemDesc getItemDescByIc(long itemId) {
        //查看商品详情时先查询缓存
        try {
            String json = jedisClient.get("ITEM_INFO:" + itemId + ":DESC");
            if(!StringUtils.isEmpty(json)){
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        //把结果添加缓存
        if(tbItemDesc!=null){
            try {
                //以id为key
                //缓存目录为 ITEN_INFO
                //                    id
                //                      "ITEM_INFO:"+id+":BASE"
                jedisClient.set("ITEM_INFO:"+itemId+":DESC",JsonUtils.objectToJson(tbItemDesc));
                //设置过期时间  一小时
                jedisClient.expire("ITEM_INFO:"+itemId+":DESC",3600);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return tbItemDesc;
    }

    @Override
    public EasyUiDataGridResult getItemList(int page, int pagesize) {
        //将表现层中获取的page和row设置分页信息
        PageHelper.startPage(page,pagesize);
        TbItemExample tbItemExample=new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        //构造返回对象
        EasyUiDataGridResult result=new EasyUiDataGridResult();
        result.setRows(tbItems);
        PageInfo<TbItem>pageInfo=new PageInfo<>(tbItems);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        //1.生成商品ID
        long itemID=IDUtils.genItemId();
        //2.补全Item的属性，如创建时间等
        tbItem.setId(itemID);
        //  1-正常 2-下架 3-删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //3.插入item表
        tbItemMapper.insert(tbItem);
        //4.插入itemdesc表
        //4.1 创建表对应的pojo
        TbItemDesc tbItemDesc=new TbItemDesc();
        //4.2 补全属性
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setItemId(itemID);
        tbItemDesc.setUpdated(new Date());
        //4.3 插入
        tbItemDescMapper.insert(tbItemDesc);
        //发送一个商品添加的主题消息，监听此主题的服务将收到此消息(同步索引)
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemID+"");
            }
        });
        return E3Result.ok();
    }



}
