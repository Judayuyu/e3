import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JedisTest {
    @Test
    public void test() throws Exception{
        //创建一个Jedis对象
        Jedis jedis=new Jedis("192.168.153.133",6379);
        //直接使用jedis操作redis，每个方法对应一个命令
        jedis.set("test3","first");
        String test1 = jedis.get("test3");
        System.out.println(test1);
        jedis.close();
    }
    @Test
    public void testJedisPool(){
        //创建一个连接池对象
        JedisPool jedisPool=new JedisPool("192.168.153.133",6379);
        //获取一个连接
        Jedis jedis = jedisPool.getResource();
        //操作redis
        String str=jedis.get("test1");
        System.out.println(str);
        //关闭连接
        jedis.close();
        //关闭连接池
        jedisPool.close();
    }
    //连接集群
    @Test
    public void testJedisCluster() throws IOException {
        Set<HostAndPort>nodes=new HashSet<>();
        nodes.add(new HostAndPort("192.168.153.133",7001));
        nodes.add(new HostAndPort("192.168.153.133",7002));
        nodes.add(new HostAndPort("192.168.153.133",7003));
        nodes.add(new HostAndPort("192.168.153.133",7004));
        nodes.add(new HostAndPort("192.168.153.133",7005));
        nodes.add(new HostAndPort("192.168.153.133",7006));
        JedisCluster jedisCluster=new JedisCluster(nodes);
        System.out.println(jedisCluster.hdel("CART:"));
        jedisCluster.close();
    }
}
