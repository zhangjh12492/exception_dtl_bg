package com.wfj.exception.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringHandlerBean {
	private SpringHandlerBean(){}
	private static ApplicationContext act=null;

	public static Object getContext(String name){
		return act.getBean(name);
	}

	public static void init(){
		if(act==null){
			act=new ClassPathXmlApplicationContext(new String[]{"spring/springApplication.xml"});
		}
	}
}
