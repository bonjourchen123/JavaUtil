package com.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Bonjour
 *
 */
public class BeanUtil {
	
	/**
	 * 把某個 Bean 轉換到某個 Bean 忽略大小寫
	 *
	 * @param objIn
	 * @param objOut
	 * @param caseSensitive 
	 *
	 */
	public static void beanToBean(Object objIn, Object objOut,boolean caseSensitive) {
		if(objIn!=null && objOut!=null) {
			Object[] empty = {};
			try {
				
				BeanInfo biIn = Introspector.getBeanInfo(objIn.getClass());
				BeanInfo biOut = Introspector.getBeanInfo(objOut.getClass());
				PropertyDescriptor[] pdsIn = biIn.getPropertyDescriptors();
				PropertyDescriptor[] pdsOut = biOut.getPropertyDescriptors();
				
				for(PropertyDescriptor pIn : pdsIn) {
					String name = pIn.getName();
					for(PropertyDescriptor pOut : pdsOut) {		
						if(caseSensitive ? name.equals(pOut.getName()) : name.equalsIgnoreCase(pOut.getName())) {
							Method readIn = pIn.getReadMethod();
							Method writeOut = pOut.getWriteMethod();
							if(readIn !=null && writeOut!=null) {
								Object value = readIn.invoke(objIn, empty);
								if(value != null){
									writeOut.invoke(objOut, value);
									break;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 把某個 Bean 轉換到某個 Bean
	 *
	 * @param objIn
	 * @param objOut
	 *
	 */
	public static void beanToBean(Object objIn, Object objOut) {	
		beanToBean(objIn,objOut,true);
	}
	
	/**
	 * @return true if obj is null or obj is empty
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		boolean result = true;
		
		if ( obj instanceof Collection ) {
			result = (isNull(obj) || ((Collection) obj).isEmpty());
		} else if ( obj instanceof Map ) {
			result = (isNull(obj) || ((Map) obj).isEmpty());
		} else if ( obj instanceof Object[] ) {
			result = ArrayUtils.isEmpty((Object[]) obj);
		} else if ( obj instanceof String ) {
			result = StringUtils.isEmpty((String) obj);
		} else {
			result = isNull(obj);
		}
		
		return result;
	}
	
	/**
	 * @return true if obj is not null and obj is not empty
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
	
	/**
	 * @return true if obj is null
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}
	
	/**
	 * @return true if obj is not null
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}
	
}
