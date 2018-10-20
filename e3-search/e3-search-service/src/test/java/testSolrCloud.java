import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class testSolrCloud {
    @Test
    public void testadd() throws IOException, SolrServerException {
        List<String> zkHosts = new ArrayList<String>();
        zkHosts.add("192.168.153.133:2182");
        zkHosts.add("192.168.153.133:2183");
        zkHosts.add("192.168.153.133:2184");
        Optional<String> zkChroot = Optional.of("/");
        //builder的构造函数需要一个List和一个Optional
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder(zkHosts, zkChroot);
        CloudSolrClient solrClient = builder.build();
        //设置默认查询的集合
        solrClient.setDefaultCollection("collection2");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "cloud");
        document.addField("item_title", "这是一个cloud测试");
        solrClient.add(document);
        solrClient.commit();
        solrClient.close();

    }
    @Test
    public void testCloudTest() throws IOException, SolrServerException {
        List<String> zkHosts = new ArrayList<String>();
        zkHosts.add("192.168.153.133:2182");
        zkHosts.add("192.168.153.133:2183");
        zkHosts.add("192.168.153.133:2184");
        Optional<String> zkChroot = Optional.of("/");
        //builder的构造函数需要一个List和一个Optional
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder(zkHosts, zkChroot);
        CloudSolrClient solrClient = builder.build();
        solrClient.setDefaultCollection("collection2");
        SolrQuery query=new SolrQuery();
        query.setQuery("*:*");
        QueryResponse query1 = solrClient.query(query);
        SolrDocumentList results = query1.getResults();
        for(SolrDocument solrDocument:results){
            System.out.println(solrDocument.get("item_title"));
        }
    }

}
