

import org.apache.solr.client.solrj.SolrClient;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class testSolr {
    @Test
    public void addDocument()throws Exception{

        String urlString = "http://192.168.153.133:8983/solr/new_core";
        SolrClient solrServer =new HttpSolrClient.Builder(urlString)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","doc03");
        document.addField("item_sell_point"," 金装喜康力幼儿配方奶粉 3段（1-3岁幼儿适用）900克, 雅培新配方三大亲体科技，国际大奖智锁罐更安心");
        document.addField("item_price",1000);
        solrServer.add(document);
        // 4、 提交
        solrServer.commit();
    }
    @Test
    public void deleteDocument()throws Exception{
        String urlString = "http://192.168.153.133:8983/solr/new_core";
        HttpSolrClient solrServer =new HttpSolrClient.Builder(urlString)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        //删除文档
        solrServer.deleteById("doc01");
        solrServer.commit();
    }
    @Test
    public void queryIndex() throws IOException, SolrServerException {
        String urlString = "http://192.168.153.133:8983/solr/new_core";
        HttpSolrClient solrServer =new HttpSolrClient.Builder(urlString)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        SolrQuery query=new SolrQuery();
        query.setQuery("*:*");
        QueryResponse query1 = solrServer.query(query);
        SolrDocumentList results = query1.getResults();
        System.out.println("Record Num:"+results.getNumFound());
        for (SolrDocument solrDocument : results) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
        }
    }
    @Test
    public void queryIndexPlus() throws IOException, SolrServerException {
        String urlString = "http://192.168.153.133:8983/solr/new_core";
        HttpSolrClient solrServer =new HttpSolrClient.Builder(urlString)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        SolrQuery query=new SolrQuery();
        query.setQuery("手机");
        query.setStart(0);
        query.setRows(20);

        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        QueryResponse query1 = solrServer.query(query);
        SolrDocumentList results = query1.getResults();
        System.out.println("Record Num:"+results.getNumFound());
        Map<String, Map<String, List<String>>> highlighting = query1.getHighlighting();
        for (SolrDocument solrDocument : results) {
            System.out.println(solrDocument.get("id")); query.set("df", "item_title");
            //取高亮显示
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list !=null && list.size() > 0 ) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            System.out.println(title);
            System.out.println(solrDocument.get("item_sell_point"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));
            System.out.println(solrDocument.get("item_category_name"));
        }
    }
}
