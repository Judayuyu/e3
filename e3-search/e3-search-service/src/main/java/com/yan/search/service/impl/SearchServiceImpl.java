package com.yan.search.service.impl;

import com.yan.common.pojo.SearchResult;
import com.yan.search.service.SearchDao;
import com.yan.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 页面搜索
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;
    @Override
    public SearchResult search(String keyword, int page, int rows) {

        SolrQuery query=new SolrQuery();
        //设置查询条件
        query.setQuery(keyword);
        if(page<=0)page=1;
        //每一页有rows条记录，根据传入的page确定要查询记录
        query.setStart((page-1)*rows);
        query.setRows(rows);
        //设置查找域
        query.set("df", "item_title");
        //设置高亮
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("<em>");
        query.set("hl.fl","item_title");
        SearchResult result=new SearchResult();
        try {
            result = searchDao.search(query);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        //设置总记录数
        long recordCount = result.getRecordCount();
        int totalPage=(int)(recordCount/rows);
        if (totalPage>0)totalPage+=1;
        result.setTotalPages(totalPage);
        return result;
    }
}
