package com.lovingmo.spdemo01.controller;

import com.lovingmo.spdemo01.pojo.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@RestController
public class ZimgController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/up")
    public String upload() throws IOException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\huaer.jpg");
        String filename = file.getName();
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        System.out.println(ext);
        URL url = new URL("http://xxxxx:4869/update");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", ext);
        connection.setDoOutput(true);
        connection.connect();
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(getFileBytes(file));

        InputStream response = connection.getInputStream();
        int len = -1;
        String ret = "";
        byte[] b = new byte[100];
        while ((len=response.read(b))!=-1){
            ret += new String(b, 0 ,len);
        }
        System.out.println(ret);
        return "ok";
    }

    /**
     * 这个方式有问题
     * */
    @RequestMapping("/up1")
    public String up1() throws MalformedURLException {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        File file = new File("C:\\Users\\Administrator\\Desktop\\huaer.jpg");
        String filename = file.getName();
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        form.add("filename",filename);
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        String s = restTemplate.postForObject("http://xxxxx:4869/update", files, String.class);
        System.out.println(s);
        return "ok1";
    }

    @RequestMapping("/up2")
    public Util.ImgResult up2(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        String ext = filename.substring(filename.lastIndexOf(".") + 1);
        Util.ImgResult imgResult = Util.ImgResultupload(file, ext);
        return imgResult;
    }

    private byte[] getFileBytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = in.read(b)) != -1){
            bos.write(b, 0 , len);
        }
        return bos.toByteArray();
    }

}
