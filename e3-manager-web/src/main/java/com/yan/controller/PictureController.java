package com.yan.controller;

import com.yan.common.utils.FastDFSClient;
import com.yan.common.utils.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {
    @RequestMapping(value = "/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile) {
        //把图片上传到文件服务器
        FastDFSClient fastDFSClient= null;
        try {
            fastDFSClient = new FastDFSClient("D:\\openSource&jar\\e3\\e3-manager-web\\src\\main\\resources\\fdfs-clent\\fdfs-client.properties");
            //取文件扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //得到图片的地址和文件名
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            url="http://192.168.153.133/"+url;
            System.out.println(url);
            Map result=new HashMap<>();
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);

        } catch (Exception e) {
            e.printStackTrace();
            Map result=new HashMap<>();
            result.put("error",0);
            result.put("url","图片上传失败");
            return JsonUtils.objectToJson(result);
        }

    }
}
