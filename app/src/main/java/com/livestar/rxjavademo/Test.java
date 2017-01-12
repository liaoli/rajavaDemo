package com.livestar.rxjavademo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class Test {

    //连接对象
    private static HttpURLConnection conn;

/** */
    /**
     * 根据url连接某地址，并返回返回码.
     * 返回码说明：
     * 0~200为正常情况，其中200为OK
     * 其余都为错误的情况，具体请参见w3
     *
     * @param urlStr 需连接的url字符串
     */
    private int connect(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        conn = (HttpURLConnection) url.openConnection();
        System.out.println("返回码: " + conn.getResponseCode());
//如果定向的地址经过重定向，
//那么conn.getURL().toString()显示的是重定向后的地址
        System.out.println(conn.getURL().toString());
        return conn.getResponseCode();
    }

/** */
    /**
     * 读取网页的内容.
     *
     * @return 返回网页的内容
     */
    private String readContents() throws Exception {
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        in = new BufferedReader(new InputStreamReader(conn
                .getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
            sb.append("\n");
        }
        return sb.toString();
    }

/** */
    /**
     * 中断连接.
     */
    private void disconnect() {
        conn.disconnect();
    }

/** */
    /**
     * 测试方法
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String url="http://user.preview.livestar.com/?ct=image&uid=1002147&key=2dc8650d3d92a4ca4636828d6ee502f3";
        String redictURL = getRedirectUrl(url);
        System.out.println(redictURL);
    }


    /**
     * 获取重定向地址
     * @param path
     * @return
     * @throws Exception
     */
    private static String getRedirectUrl(String path) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(path)
                .openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        return conn.getHeaderField("Location");
    }
}
