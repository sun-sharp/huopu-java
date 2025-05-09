package com.wx.genealogy.common.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public final class JavaMailUntil {
    private JavaMailUntil() {}

    public static Session createSession() {

        //	账号信息
        /**
         * IMAP/SMTP服务:PFUPPOIKZCEKPMDX
         */
        String username = "kt_zby@163.com";//	邮箱发送账号
//        String password = "PFUPPOIKZCEKPMDX";//	邮箱授权码
        String password = "SJQAVUZWAJNHTVUJ";//	邮箱授权码

        //	创建一个配置文件，并保存
        Properties props = new Properties();

        //	SMTP服务器连接信息
        //  126——smtp.126.com
        //  163——smtp.163.com
        //  qqsmtp.qq.com"
        props.put("mail.smtp.host", "smtp.163.com");//	SMTP主机名

        //  126——25
        //  163——465
        props.put("mail.smtp.port", "25");//	主机端口号
        props.put("mail.smtp.auth", "true");//	是否需要用户认证
        props.put("mail.smtp.starttls.enale", "true");//	启用TlS加密


        Session session = Session.getInstance(props,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(username,password);
            }
        });

        //  控制台打印调试信息
        session.setDebug(true);
        return session;

    }

}