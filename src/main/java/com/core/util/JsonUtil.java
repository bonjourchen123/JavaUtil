package com.core.util;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * 
 * @author Bonjour
 *
 */
public class JsonUtil {

	/**
	 * 把物件 obj 轉成 json格式
	 * 
	 * @param obj
	 * @return json格式
	 */
	public static String objToJson(Object obj){
		String result = null;
		try {
			Gson gson = new Gson();
			result = gson.toJson(obj);
			if(StringUtils.isBlank(result)){
				result = "null";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String objToJsonByJSONObject(Object obj){
		String result = null;
		try {
			if(obj == null){
				result = "null";
			} else {
				result = JSONObject.fromObject(obj).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
