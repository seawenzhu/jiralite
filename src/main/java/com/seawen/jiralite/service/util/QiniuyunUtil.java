package com.seawen.jiralite.service.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.seawen.jiralite.service.dto.FileResultDTO;

import java.io.*;

public final class QiniuyunUtil {
    public static final String ak = "A6ijT-qJqTl-TPGwFL4tRrnjDIqtvNMXROlzEwok";
    public static final String sk = "tqnNwROjn-iCVBO2suaLwAfJcKe27Yj0PKunrytZ";
    public static final String bucket = "seawenzhu";
    public static final String outlineLine = "http://owgf0oooy.bkt.clouddn.com/";

    private QiniuyunUtil() {
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/seawenzhu/Pictures/互联网.jpeg");
        InputStream is = new FileInputStream(file);
        QiniuyunUtil.uploadInputStream(is);
    }

    public static FileResultDTO uploadInputStream(InputStream is) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        FileResultDTO result = new FileResultDTO();
        try {
            //            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(ak, sk);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(is, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return result;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }

        return result;
    }
    public static FileResultDTO uploadInputStream(byte[] uploadBytes) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        FileResultDTO result = new FileResultDTO();
        try {
            //            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(ak, sk);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                result.setLink(outlineLine + putRet.key);
                return result;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }

        return result;
    }
}
