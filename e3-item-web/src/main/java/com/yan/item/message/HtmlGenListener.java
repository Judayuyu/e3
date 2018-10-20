package com.yan.item.message;

import com.yan.pojo.TbItem;
import com.yan.pojo.TbItemDesc;
import com.yan.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import pojo.Item;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

//监听商品添加消息，若有商品添加，则生成对应的静态页面
public class HtmlGenListener implements MessageListener {
    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage= (TextMessage) message;
            //从消息中获取存入的id
            String text = textMessage.getText();
            Long itemId=new Long(text);
            //查询数据库前等待事务提交
            Thread.sleep(1000);
            //根据id查数据库获取页面需要的信息
            TbItem item = itemService.getItemById(itemId);
            Item item1=new Item(item);
            TbItemDesc itemDescByIc = itemService.getItemDescByIc(itemId);
            //创建一个数据集，将内容放入
            Map<String,Object> data=new HashMap<>();
            data.put("item",item1);
            data.put("itemDesc",itemDescByIc);
            //加载模板,动态填充模板里的内容
            Configuration conf = freeMarkerConfigurer.getConfiguration();
            Template template = conf.getTemplate("item.ftl");
            //文件名为 id.html
            Writer out=new FileWriter("D:\\openSource&jar\\freeMaker\\item\\"+itemId+".html");
            //生成页面
            template.process(data,out);
            out.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
