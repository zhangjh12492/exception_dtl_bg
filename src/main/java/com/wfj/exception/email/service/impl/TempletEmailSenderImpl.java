package com.wfj.exception.email.service.impl;

import java.util.Map;

import javax.inject.Qualifier;

import org.springframework.beans.factory.annotation.Autowired;

import com.wfj.exception.email.service.EmailSender;
import com.wfj.exception.email.service.TempletEmailSender;
import com.wfj.exception.email.util.VelocityParserUtil;
import com.wfj.exception.email.vo.Mail;

/** 
 * 邮件发送器默认实现 
 */
public class TempletEmailSenderImpl implements TempletEmailSender {
	@Autowired
//	@Qualifier("emailSenderImpl_commons")
	private EmailSender emailSender;

	@Override
	public void sendEmailByTemplet(String from, String to, String subject, String templet, Map<String, Object> paramMap) throws Exception {
		sendEmailByTemplet(from, new String[] { to }, subject, templet, paramMap);

	}

	@Override
	public void sendEmailByTemplet(String from, String[] to, String subject, String templet, Map<String, Object> paramMap) throws Exception {
		// 解析模板  
		String content;
		try {
			content = VelocityParserUtil.getInstance().parseVelocityTemplate(templet, paramMap);
		} catch (Throwable t) {
			throw new Exception(t);
		}
		emailSender.sendEmail(from, to, subject, content);

	}

	@Override
	public void sendEmailByTemplet(Mail mail, String templet, Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		
	}
}