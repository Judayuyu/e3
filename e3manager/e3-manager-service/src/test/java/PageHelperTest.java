import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yan.mapper.TbItemMapper;
import com.yan.pojo.TbItem;
import com.yan.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class PageHelperTest {
    ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
    TbItemMapper tbItemMapper=applicationContext.getBean(TbItemMapper.class);
    @Test
    public void test(){
        PageHelper.startPage(1,5);
        TbItemExample tbItemExample=new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        PageInfo<TbItem>pageInfo=new PageInfo<>(tbItems);
        System.out.println(pageInfo.getPageSize());
    }
}
