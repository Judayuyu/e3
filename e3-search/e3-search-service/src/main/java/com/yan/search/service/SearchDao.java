package com.yan.search.service;

import com.yan.common.jedis.JedisClient;
import com.yan.common.pojo.SearchItem;
import com.yan.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {

    private HttpSolrClient solrServer;
    //根据查询条件查询索引库
    public SearchResult search(SolrQuery solrQuery) throws IOException, SolrServerException {
        String urlString = "http://192.168.153.133:8983/solr/new_core";
        solrServer =new HttpSolrClient.Builder(urlString)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        QueryResponse queryResponse = solrServer.query(solrQuery);
        //取高亮
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        SolrDocumentList results = queryResponse.getResults();
        //获取总记录数
        long numFound = results.getNumFound();
        SearchResult searchResult=new SearchResult();
        searchResult.setRecordCount(numFound);
        List<SearchItem>list=new ArrayList<>();
        for(SolrDocument solrDocument:results){
            SearchItem item=new SearchItem();
            item.setId((String) solrDocument.get("id"));
            item.setCategoryName((String) solrDocument.get("item_category_name"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSellPoint((String) solrDocument.get("item_sell_point"));
            //取高亮显示
            List<String> strings = highlighting.get(solrDocument.get("id")).get("item_title");
            String title="";
            if(strings!=null&&strings.size()>0){
                title=strings.get(0);
            }
            else {
                title= (String) solrDocument.get("item_title");
            }
            item.setTitle(title);

            list.add(item);

        }
        searchResult.setItemList(list);
        return searchResult;
    }
}
