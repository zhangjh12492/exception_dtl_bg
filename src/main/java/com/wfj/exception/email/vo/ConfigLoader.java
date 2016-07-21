package com.wfj.exception.email.vo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * 邮件发送配置信息加载类
 * 
 * @date 2014年4月26日 下午2:52:06
 * @author 
 * @Description:
 * @project mailUtil
 */
public class ConfigLoader {
    //日志记录对象
    // 配置文件路径
    // 邮件发送SMTP主机
    private static String server;
    // 发件人邮箱地址
    private static String sender;
    // 发件人邮箱用户名
    private static String username;
    // 发件人邮箱密码
    private static String password;
    // 发件人显示昵称
    private static String nickname;
    
 
    public static String getServer() {
        return server;
    }
 
    public static String getSender() {
        return sender;
    }
 
    public static String getUsername() {
        return username;
    }
 
    public static String getPassword() {
        return password;
    }
 
    public static String getNickname() {
        return nickname;
    }
 
 
}