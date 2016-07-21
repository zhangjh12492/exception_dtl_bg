package com.wfj.exception.email.util;


import java.io.UnsupportedEncodingException;
import java.util.Properties;
 
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * 邮件发送实现类
 * 
 * @date 2014年4月26日 上午10:16:34
 * @author 
 * @Description:
 * @project mailUtil
 */
public class MailSender {
	
	private static final Logger log=LoggerFactory.getLogger(MailSender.class);
	
    private MimeMessage mimeMsg; // MIME邮件对象
    private Session session; // 邮件会话对象
    private Properties props; // 系统属性
    private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成
    private String username;// 发件人的用户名
    private String password;// 发件人的密码
    private String nickname;// 发件人的昵称
 
    /**
     * 有参构造器
     * 
     * @param smtp
     */
    public MailSender(String smtp) {
        setSmtpHost(smtp);
        createMimeMessage();
    }
 
    /**
     * 设置邮件发送的SMTP主机
     * 
     * @Date:2014年4月26日 上午10:20:34
     * @author 
     * @param hostName
     *            SMTP 发送主机
     * @Description:
     * @return void
     */
    public void setSmtpHost(String hostName) {
        if (props == null)
            props = System.getProperties();
        props.put("mail.smtp.host", hostName);
        log.debug("set system properties success ：mail.smtp.host= " + hostName);
 
    }
 
    /**
     * 创建邮件对象
     * 
     * @Date:2014年4月26日 上午10:26:34
     * @author 
     * @return
     * @Description:
     * @return boolean
     */
    public void createMimeMessage() {
        // 获得邮件会话对象
        session = Session.getDefaultInstance(props, null);
        // 创建MIME邮件对象
        mimeMsg = new MimeMessage(session);
        mp = new MimeMultipart();
        log.debug(" create session and mimeMessage success");
    }
 
    /**
     * 设置权限鉴定配置
     * 
     * @Date:2014年4月26日 上午10:36:34
     * @author 
     * @param need
     *            是否需要权限
     * @Description:
     * @return void
     */
    public void setNeedAuth(boolean need) {
        if (props == null)
            props = System.getProperties();
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
        log.debug("set smtp auth success：mail.smtp.auth= " + need);
 
    }
 
    /**
     * 设置发送邮件的主题
     * 
     * @Date:2014年4月26日 上午10:26:34
     * @author 
     * @param subject
     *            邮件的主题
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @Description:
     * @return void
     */
    public void setSubject(String subject) throws UnsupportedEncodingException,
            MessagingException {
        mimeMsg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
        log.debug("set mail subject success, subject= " + subject);
 
    }
 
    /**
     * 
     * @Date:2014年4月26日 上午10:28:34
     * @author 
     * @param mailBody
     *            邮件的正文内容
     * @throws MessagingException
     * @Description:
     * @return void
     */
    public void setBody(String mailBody) throws MessagingException {
        BodyPart bp = new MimeBodyPart();
        bp.setContent("" + mailBody, "text/html;charset=utf-8");
        mp.addBodyPart(bp);
        log.debug("set mail body content success,mailBody= " + mailBody);
    }
 
    /**
     * 添加邮件附件
     * 
     * @Date:2014年4月26日 上午10:30:40
     * @author 
     * @param filePath
     *            文件绝对路径
     * @throws MessagingException
     * @Description:
     * @return void
     */
    public void addFileAffix(String filePath) throws MessagingException {
        BodyPart bp = new MimeBodyPart();
        FileDataSource fileds = new FileDataSource(filePath);
        bp.setDataHandler(new DataHandler(fileds));
        bp.setFileName(fileds.getName());
        mp.addBodyPart(bp);
        log.debug("mail add file success,filename= " + filePath);
    }
 
    /**
     * 设置发件人邮箱地址
     * 
     * @Date:2014年4月26日 上午10:35:54
     * @author 
     * @param sender
     *            发件人邮箱地址
     * @throws UnsupportedEncodingException
     * @throws AddressException
     * @throws MessagingException
     * @Description:
     * @return void
     */
    public void setSender(String sender) throws UnsupportedEncodingException,
            AddressException, MessagingException {
        nickname = MimeUtility.encodeText(nickname, "utf-8", "B");
        mimeMsg.setFrom(new InternetAddress(nickname + " <" + sender + ">"));
        log.debug(" set mail sender and nickname success , sender= " + sender
                + ",nickname=" + nickname);
    }
 
    /**
     * 设置收件人邮箱地址
     * 
     * @Date:2014年4月26日 上午10:41:06
     * @author 
     * @param receiver
     *            收件人邮箱地址
     * @throws AddressException
     * @throws MessagingException
     * @Description:
     * @return void
     */
    public void setReceiver(String receiver) throws AddressException,
            MessagingException {
        mimeMsg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(receiver));
        log.debug("set mail receiver success,receiver = " + receiver);
    }
 
    /**
     * 设置抄送人的邮箱地址
     * 
     * @Date:2014年4月26日 上午10:42:14
     * @author 
     * @param copyto
     *            抄送人邮箱地址
     * @throws AddressException
     * @throws MessagingException
     * @Description:
     * @return void
     */
    public void setCopyTo(String copyto) throws AddressException,
            MessagingException {
        mimeMsg.setRecipients(Message.RecipientType.CC,
                InternetAddress.parse(copyto));
        log.debug("set mail copyto receiver success,copyto = " + copyto);
    }
 
    /**
     * 设置发件人用户名密码进行发送邮件操作
     * 
     * @Date:2014年4月26日 上午10:44:01
     * @author 
     * @throws MessagingException
     * @Description:
     * @return void
     */
    public void sendout() throws MessagingException {
    	log.info("开始发送邮件");
        mimeMsg.setContent(mp);
        mimeMsg.saveChanges();
        Session mailSession = Session.getInstance(props, null);
        Transport transport = mailSession.getTransport("smtp");
        transport.connect((String) props.get("mail.smtp.host"), username,
                password);
        transport.sendMessage(mimeMsg,
                mimeMsg.getRecipients(Message.RecipientType.TO));
        log.info("发送邮件结束");
        transport.close();
        log.debug(" send mail success");
    }
 
    /**
     * 注入发件人用户名 ，密码 ，昵称
     * 
     * @Date:2014年4月26日 上午10:50:12
     * @author 
     * @param username
     *            发件人邮箱登录用户名
     * @param password
     *            发件人邮箱密码
     * @param nickname
     *            发件人显示的昵称
     * @Description:
     * @return void
     */
    public void setNamePass(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
 
    }
 
}