package com.wfj.exception.email.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.wfj.exception.email.service.EmailSender;
import com.wfj.exception.email.util.BasePropertyID;
import com.wfj.exception.email.util.ConfigService;
import com.wfj.exception.email.vo.Mail;
import com.wfj.exception.email.vo.MailAttachment;
/*
**  
* JAVA MAIL实现  
*/  
public class EmailSenderImpl implements EmailSender/*,InitializingBean*/{
   /** 
    * Logger for this class 
    */  
   private static final Logger logger = Logger.getLogger(EmailSenderImpl.class);  
 
   @Autowired  
   private ConfigService configService;  
     
   private JavaMailSenderImpl sender; // 实际的发送实现  
     
 
   @Override  
   public void sendEmail(String from, String to, String subject, String mailBody) throws Exception {  
       sendEmail(from, new String[]{to}, subject, mailBody);  
   }  
     
 
 
   @Override  
   public void sendEmail(String from, String[] to, String subject, String mailBody) throws Exception {  
       // 构造MAIL对象  
       Mail mail = new Mail();  
       mail.setFrom(from);  
       mail.setTo(to);  
       mail.setSubject(subject);  
       mail.setContent(mailBody);  
       sendEmail(mail);  
         
   }  
 
   @Override  
   public void sendEmail(Mail mail) throws Exception {  
       // 检查必要参数  
       if (mail == null ){  
           throw new Exception("mail can not be null.");  
       }  
       if (ArrayUtils.isEmpty(mail.getTo())){  
           throw new Exception("收件人不能为空");  
       }  
       MimeMessageHelper helper = null;  
       try {  
           helper = new MimeMessageHelper(sender.createMimeMessage(), true, "UTF-8");  
 
           // 发件人  
           if (mail.getFrom() != null) {  
               if (mail.getFromName() == null) {  
                   helper.setFrom(mail.getFrom());  
               } else {  
                   helper.setFrom(mail.getFrom(), mail.getFromName());  
               }  
 
           }  
           // 收件人  
           helper.setTo(mail.getTo());  
 
           // 抄送人  
           if (mail.getCc() != null) {  
               helper.setCc(mail.getCc());  
           }  
 
           // 密送人  
           if (mail.getBcc() != null) {  
               helper.setBcc(mail.getBcc());  
           }  
 
           // 邮件主题  
           helper.setSubject(mail.getSubject());  
 
           // 邮件内容  
           helper.setText(mail.getContent(), mail.isHtmlFormat());  
 
           // 附件  
           if (mail.getAttachments() != null) {  
               for ( MailAttachment attachment : mail.getAttachments()) {  
                   helper.addAttachment(attachment.getFileName(),attachment.getFile());  
               }   
           }  
 
           // 发送时间  
           helper.setSentDate(new Date());  
       } catch (UnsupportedEncodingException e) {  
           logger.error("sendEmail(Mail)", e);  
 
           throw new Exception(e) ;  
       } catch (MessagingException e) {  
           logger.error("sendEmail(Mail)", e);  
 
           throw new Exception(e) ;  
       }  
         
       // 发送  
       try {  
           sender.send(helper.getMimeMessage());  
       } catch (MailException e) {  
           logger.error("sendEmail(Mail)", e);  
 
           throw new Exception(e) ;  
       }  
         
   }  
     
//   @Override
   public void afterPropertiesSet() throws Exception {  
       sender = new JavaMailSenderImpl();  
         
       // configService读出参数  
       Properties pros = new Properties();  
 
       pros.setProperty("mail.smtp.user", configService.getConfig(BasePropertyID.MAIL_SMTP_USER_ID));  
       pros.setProperty("mail.smtp.host", configService.getConfig(BasePropertyID.MAIL_SMTP_HOST_ID));  
       pros.setProperty("mail.smtp.port", configService.getConfig(BasePropertyID.MAIL_SMTP_PORT_ID));  
       pros.setProperty("mail.smtp.connectiontimeout", configService.getConfig(BasePropertyID.MAIL_SMTP_CONNECTIONTIMEOUT_ID));  
       pros.setProperty("mail.smtp.timeout", configService.getConfig(BasePropertyID.MAIL_SMTP_TIMEOUT_ID));  
       pros.setProperty("mail.smtp.from", configService.getConfig(BasePropertyID.MAIL_SMTP_FROM_ID));  
       pros.setProperty("mail.smtp.auth", configService.getConfig(BasePropertyID.MAIL_SMTP_AUTH_ID));  
                 
       sender.setJavaMailProperties(pros);  
       sender.setPassword(configService.getConfig(BasePropertyID.MAIL_SMTP_PASSWORD_ID));  
         
   }  
 
 
 
   public ConfigService getConfigService() {  
       return configService;  
   }  
 
 
 
   public void setConfigService(ConfigService configService) {  
       this.configService = configService;  
   }  
}
