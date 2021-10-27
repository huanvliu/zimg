package com.lovingmo.spdemo01.pojo;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.*;

import java.io.IOException;

public class Util {
    public static final Logger log = LoggerFactory.getLogger(Util.class);
    public static final String host = "http://xxxxx:4869/update";
    public static ImgResult ImgResultupload(MultipartFile multipartFile, String ext) throws IOException {

        HttpRequest request = HttpRequest

                .post(host)

                .body(multipartFile.getBytes(), ext);

        HttpResponse response = request.send()

                .acceptEncoding(Charsets.UTF_8.name())

                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        ImgResult result = JSON.parseObject(response.bodyText(), ImgResult.class);

        log.info("文件上传结果===>{}", result);

        return result;
    }

    public static class ImgError {

        private int code;

        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    ;

     public static  class ImgResult {
        private boolean ret;

        private ImgError error;

        private ImgInfo info;

        public boolean isRet() {
            return ret;
        }

        public void setRet(boolean ret) {
            this.ret = ret;
        }

        public ImgError getError() {
            return error;
        }

        public void setError(ImgError error) {
            this.error = error;
        }

        public ImgInfo getInfo() {
            return info;
        }

        public void setInfo(ImgInfo info) {
            this.info = info;
        }
    };

     public static class ImgInfo {

        private String md5;

        private long size;

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }

}
