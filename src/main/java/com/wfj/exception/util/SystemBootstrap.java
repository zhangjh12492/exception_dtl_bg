package com.wfj.exception.util;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author 
 *
 */
public class SystemBootstrap implements InitializingBean{
	/**
	 * CONFIG_FILE_PATH 系统变量配置文件路径
	 */
	private static final String CONFIG_FILE_PATH = "/config.properties";

	@Override
	public void afterPropertiesSet() {
		InputStream inputStream = null;
		Properties properties = new Properties();
		try {
			inputStream = SystemBootstrap.class.getResourceAsStream(CONFIG_FILE_PATH);
			properties.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		PropertiesLoad.init(properties);
	}
}
