package com.yan.search.service.impl;


import com.yan.common.pojo.SearchItem;
import com.yan.common.utils.E3Result;
import com.yan.search.service.SearchItemService;
import com.yan.search.service.mapper.ItemMapper;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;


    private HttpSolrClient solrServer;
    @Override
    public E3Result importAllItems() {
        String urlString = "http://192.168.153.133:8983/solr/new_core";
        solrServer =new HttpSolrClient.Builder(urlString)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        //从数据库中查询商品并导入solr文档对象
        try {
            List<SearchItem> itemList = itemMapper.getItemList();
            for(SearchItem searchItem:itemList){
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point",searchItem.getSellPoint());
                document.addField("item_price",searchItem.getPrice());
                document.addField("item_image",searchItem.getImage());
                document.addField("item_category_name",searchItem.getCategoryName());
                solrServer.add(document);
            }
            solrServer.commit();
            return  E3Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return E3Result.build(500,"数据导入Solr异常");
        }

    }
}
