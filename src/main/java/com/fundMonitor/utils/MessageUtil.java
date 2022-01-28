package com.fundMonitor.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author 11369
 * @date 2016/6/8
 */
@Slf4j
public class MessageUtil {
    private static String account = "ybao_james@126.com";
    private static String password = "1722@ecnu";
    private static String userid = "7635";
    private static String httpUrl = "http://sms.kingtto.com:9999/sms.aspx";

    /**
     * :请求接口
     * :参数
     *
     * @return 返回结果
     */
    public static String request(String mobile, String content) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        String httpArg = null;
        try {
            httpArg = "mobile=" + mobile + "&content=" + URLEncoder.encode(content, "UTF-8") + "&action=send" + "&account=" + account + "&password=" + password + "&userid=" + userid;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String sendUrl = httpUrl + "?" + httpArg;
        try {
            URL url = new URL(sendUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信发送到手机号{},结果: {}.", mobile, result);
        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(request("19921892267", "【送温暖】 您有一条审批消息333。"));
    }
}
