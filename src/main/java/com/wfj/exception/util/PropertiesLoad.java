package com.wfj.exception.util;

import java.util.Properties;

public class PropertiesLoad {

	private static Properties pros=new Properties();	//存储config.properties中的所有配置项
	/**
	 * 初始化properties
	 * @Title: init
	 * @author Administrator
	 * @param pro
	 * @return void
	 * @throws
	 * @date 2015-6-24 下午7:01:51
	 */
	public static void init(Properties pro){
		pros=pro;
	}
	
	public static String getProperties(String key){
		return pros.getProperty(key);
	}
	/**
	 * 添加properties
	 * @Title: putProperties
	 * @author Administrator
	 * @param key
	 * @param value
	 * @return void
	 * @throws
	 * @date 2015-6-24 下午7:02:04
	 */
	public static void putProperties(String key,String value){
		pros.put(key, value);
	}
}
