import com.yan.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClientTest {
    @Test
    public void testJedisClient()throws Exception{
        ApplicationContext ioc=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedisClient= ioc.getBean(JedisClient.class);
        System.out.println(jedisClient);
        jedisClient.set("myset","jedisClient");
        System.out.println(jedisClient.get("myset"));

    }
}
