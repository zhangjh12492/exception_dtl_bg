package com.wfj.exception.common;


public class SysConstants {
	
	
	/*连接超时时间秒*/
	public static final Integer CONN_TIMEOUT_SECOND = 10;
	/*连接超时时间毫秒*/
	public static final Integer CONN_TIMEOUT_MS = 10 * 1000;

	/**/
	public static final String HBASE_NETTY_MAP_METHOD_NAME="methodName"; 
	
	public static final String HBASE_NETTY_MAP_DATA="data";
	
	public static final String sockeServerUrl="http://172.16.3.202:8080/webSocketPro/exceptionSocketPro";	//展示页面的websocket路径
	public static final String PROCESS_SEND_USER_BY_EW="process_send_user_by_ew";	//reduce处理后发送邮件或短信的接口

	public static final String MAIL_SERVER="mail.server";	// 邮件发送SMTP主机
	
	public static final String MAIL_SENDER="mail.sender";	// 发件人邮箱地址
	
	public static final String MAIL_NICKNAME="mail.nickname";// 发件人显示昵称
	
	public static final String MAIL_USERNAME="mail.username";// 发件人邮箱用户名
	
	public static final String MAIL_PASSWORD="mail.password";// 发件人邮箱密码
	
	
	public static final Integer busiWarnEarlyCount=10;	//业务警告数量承受量
	public static final Integer busiErrEarlyCount=5;	//业务异常数量承受量
	
	public static final Integer sysWarnEarlyCount=10;	//系统警告数量承受量
	public static final Integer sysErrEarlyCount=5;		//系统异常数量承受量
	
	public static final String ARITHMETIC="001";	//算术异常
	public static final String ARRAYSTORE ="002";	//数组类型不兼容
	public static final String CLASSCAST ="003";	//类型强制转换异常
	public static final String EMPTYSTACK ="004";	//堆栈为空
	public static final String SECURITY ="005";	//权限异常
	public static final String ILLEGALARGUMENT ="006";		//方法传递了一个不合法或不正确的参数
	public static final String WEBSERVICE ="007";	//WebService异常
	public static final String NULLPOINTER ="008";		//空指针
	public static final String NOSUCHMECHANISM ="009";	//
	public static final String NOSUCHELEMENT ="010";	//
	public static final String IMAGINGOP ="011";		//
	public static final String UNSUPPORTEDOPERATION ="012";		//
	public static final String INDEXOUTOFBOUNDS="013";		//	
	public static final String RUNTIME="200";	//
	public static final String IOException="210";	//
	public static final String EXCEPTION="220";	//
	public static final String OTHEREXCEPTION="230";	//
}
