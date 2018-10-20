package com.yan.search.service.message;


import com.yan.common.pojo.SearchItem;
import com.yan.search.service.mapper.ItemMapper;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 * <p>Title: ItemAddMessageListener</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class ItemAddMessageListener implements MessageListener {
	
	@Autowired
	private ItemMapper itemMapper;

	private HttpSolrClient solrServer;

	@Override
	public void onMessage(Message message) {
		String urlString = "http://192.168.153.133:8983/solr/new_core";
		solrServer =new HttpSolrClient.Builder(urlString)
				.withConnectionTimeout(10000)
				.withSocketTimeout(60000)
				.build();
		try {
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			System.out.println("获取的id为"+itemId);
			//等待事务提交。在添加商品的时候，可能事务还未提交而消息已经收到，此时查询数据库报空指针异常
			Thread.sleep(1000);
			//根据商品id查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//创建一个文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSellPoint());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategoryName());
			//把文档写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
