package com.eboji.commons.util.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONUtil {
	private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	
	/**
	 * 将JSON格式字符串转换为指定的对应类对象
	 * @param jsonString	JSON格式字符串
	 * @param clazz			类类型
	 * @return	T
	 */
	public static <T> T string2Bean(String jsonString, Class<T> clazz) {
		T ret = null;
		try {
			Object obj = JSON.parse(jsonString);
			JSON json = (JSON)JSON.toJSON(obj);
			ret = (T)JSONObject.toJavaObject(json, clazz);
		} catch (Exception e) {
			logger.error("JSON transfer failed, error message: " + e.getMessage());
		}
		
		return ret;
	}
	
	/**
	 * 将对象转换成JSON字符串
	 * @param object	对象
	 * @return	{@link String}
	 */
	public static String bean2String(Object object) {
		return JSONObject.toJSONString(object);
	}
	
	/**
	 * 将对象转换成JSON字符串
	 * @param object	对象
	 * @param prettyFormat 是否格式美化,(true:是,false:否)
	 * @return	{@link String}
	 */
	public static <T> String bean2String(T object, boolean prettyFormat) {
		return JSONObject.toJSONString(object, prettyFormat);
	}
}