package com.yahs.king.robot.utils;

import android.accounts.NetworkErrorException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yahs.king.robot.bean.ChatMessage;
import com.yahs.king.robot.bean.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by king on 2017/2/19.
 */

public class NetUtils {
    private static String URL = "http://www.tuling123.com/openapi/api";
    private static String API_KEY="2cb78593c455403785f739cad7b4aae4";


    public static String post(String content) {
        String result = "";
        HttpURLConnection conn = null;
        String msg = setParams(content);
        try {
            // 创建一个URL对象
            URL mURL = new URL(URL);
            // 调用URL的openConnection()方法,获取HttpURLConnection对象
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("POST");// 设置请求方法为post
            conn.setReadTimeout(5000);// 设置读取超时为5秒
            conn.setConnectTimeout(10000);// 设置连接网络超时为10秒
            conn.setDoOutput(true);// 设置此方法,允许向服务器输出内容
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "token");
            conn.setRequestProperty("tag", "htc_new");

            // post请求的参数
            String data = msg;
            // 获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
            OutputStream out = conn.getOutputStream();// 获得一个输出流,向服务器写数据
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
            if (responseCode == 200) {

                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            } else {
                throw new NetworkErrorException("response status is "+responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();// 关闭连接
            }
        }

        return result;
    }


    public static String setParams(String msg) {

        String content = "";
        content = "{\"key\":\""+API_KEY+"\",\"info\":\""+msg+"\"}";
        JSONObject json = new JSONObject();
        try {
            json.put("key",API_KEY);
            json.put("content",content);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return content;
    }







    //发送一个消息，得到返回的消息
    public static ChatMessage SendMessage(String content){

        ChatMessage chatMessage = new ChatMessage();
        String jsonr = post(content);
        Gson gsons = new Gson();
        Result result = null;
        try {
            result = gsons.fromJson(jsonr, Result.class);
            chatMessage.setMsg(result.getText());


        }catch (JsonSyntaxException e){
            chatMessage.setMsg("服务器繁忙，请稍后再试");
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);


        return chatMessage;
    }




    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 模板代码 必须熟练
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }
}
