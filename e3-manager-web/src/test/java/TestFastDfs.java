//import com.yan.common.utils.FastDFSClient;
//import org.csource.common.MyException;
//import org.csource.fastdfs.*;
//import org.junit.Test;
//
//import java.io.IOException;
//
//
//public class TestFastDfs {
//
//    /**
//     * 若测试报异常连接不上主机
//     * 则可以关闭服务器上的防火墙 stop iptables.service
//     *
//     */
//    @Test
//    public void test() throws IOException, MyException {
//        //1.创建配置文件。
//        //2.加载配置文件
//        ClientGlobal.init("D:\\openSource&jar\\e3\\e3-manager-web\\src\\main\\resources\\fdfs-clent\\fdfs-client.properties");
//        //3.创建TrackerClient对象
//        TrackerClient trackerClient=new TrackerClient();
//        //4.通过TrackerClient获取TrackerServer
//        TrackerServer trackerServer = trackerClient.getConnection();
//        StorageServer storageServer=null;
//        StorageClient storageClient=new StorageClient(trackerServer,storageServer);
//        //5.使用storageClient上传文件
//        String[] strings = storageClient.upload_file("C:\\Users\\Administrator\\Pictures\\xiao.jpg", "jpg", null);
//        for(String string:strings){
//            System.out.println(string);
//        }
//
//    }
//
//    @Test
//    public void testFdfsUtils() throws Exception {
//        FastDFSClient fastDFSClient=new FastDFSClient("D:\\openSource&jar\\e3\\e3-manager-web\\src\\main\\resources\\fdfs-clent\\fdfs-client.properties");
//        String s = fastDFSClient.uploadFile("C:\\Users\\Administrator\\Pictures\\g.jpg");
//        System.out.println(s);
//    }
//
//    @Test
//    public void t(){
//        String a="xiao.jpg";
//        System.out.println(a.lastIndexOf(".")+1);
//        System.out.println(a.lastIndexOf("g"));
//    }
//}
