package com.thinkingdata.lib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class PropertyUtil {
	private Properties properties = new Properties();
	private String filePath;

	public PropertyUtil(String filePath) {
		this.filePath = filePath;
	}


	public String getValue(String key) {
		InputStream in = null;
		try {
			in = Class.forName(PropertyUtil.class.getName()).getResourceAsStream(filePath);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}

}
