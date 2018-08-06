package com.liuguanghui.littlereader.util;

import android.icu.text.SymbolTable;
import android.system.OsConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by liuguanghui on 2018/5/23.
 */

public class HttpClientUtil {


    public static String httpPost(String path , Map<String,String> param)  {
        String result = null;
        //1. 得到连接对象
        URL url = null;
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            // 4). 设置请求方式,连接超时, 读取数据超时
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 5). 连接服务器
            connection.connect();

            if(param!=null){
                //得到输出流, 写请求体:name=Tom1&age=11
                os = connection.getOutputStream();
                String data = "";

                for(Map.Entry<String, String>  entry : param.entrySet()){
                    data += "&"+entry.getKey()+"="+entry.getValue();
                }
                data = data.replaceFirst("&","");

                os.write(data.getBytes("utf-8"));
            }

            //发请求并读取服务器返回的数据
            int responseCode = connection.getResponseCode();

            if(responseCode==200) {
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                baos.close();
                is.close();
                result = baos.toString();
            } else {
                //也可以抛出运行时异常
            }

        } catch (Exception e) {

            if(os!=null){
                try {
                    os.flush();
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            if(connection!=null){
                connection.disconnect();
            }
            e.printStackTrace();
        }

        return result;
    }

}
