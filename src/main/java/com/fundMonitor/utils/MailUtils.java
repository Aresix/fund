package com.fundMonitor.utils;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author sqmy
 * @create 2022-01-28 13:45
 */
public class MailUtils {
    private static String id = "hejinotify"; //发送账户
    private static String pwd = "FUYFCOJBZXCZTSUV"; //账户密码
    private static String form = "hejinotify@163.com"; //账户的地址
    private static String smtp = "smtp.163.com"; //smtp地址
    private static Properties props = null;
    private static Session session = null;
    private static Boolean outDebug = true;//输出调试信息
    static
    {
        props=new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        session=Session.getInstance(props);
        session.setDebug(outDebug);
    }
    public static void main(String[] args) {
        //675500969@qq.com
        boolean isok = MailUtils.send("testtitle", "testcontent", "hejinotify", "1016407515@qq.com");
        System.out.println(isok);
    }
    /**
     *
     * @param title 主题
     * @param content 内容
     * @param fromName 发件人姓名
     * @param toAddress 收件人地址
     * @return 是否成功
     */
    public static Boolean send(String title,String content,String fromName,String[]toAddress) {
        if(toAddress==null || toAddress.length==0){return false;}
        Message msg=new MimeMessage(session);
        try {
            //发送的邮箱地址
            msg.setFrom(new InternetAddress(form,fromName));
            msg.setSubject(title);
            msg.setContent(content,"text/html;charset=utf-8;");
            Transport transport=session.getTransport();
            //设置服务器以及账号和密码
            transport.connect(smtp,25,id,pwd);
            Address[] add = new Address[toAddress.length];
            //发送到的邮箱地址
            for (int i = 0; i < toAddress.length; i++) {
                add[i] = new InternetAddress(toAddress[i]);
            }
            transport.sendMessage(msg,add);
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Boolean send(String title,String content,String fromName,String toAddress){
        String[] add = new String[]{toAddress};
        return send( title,content,fromName,add);
    }

}