package com.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Bonjour
 *
 */
public class SimpleDataWrapperUtil {
	
	/**
     * 把物件包裝成更簡單的資料格式
     * 
     * @param list : 原本的資料物件 List
     * @param includeColumns : 要 include 的欄位 (其他欄位將會 set 成 null)
     * @return
     */
    public static <T> List<T> doSimpleWrap(List<T> list, String ... includeColumns) throws Exception {
        List<T> result = null;
        
        if (list != null) {
            result = list;
            if (includeColumns != null && includeColumns.length > 0) {
                result = new ArrayList<T>();
                for (T o : list) {
                    T oWrapped = doSimpleWrap(o, includeColumns);
                    result.add(oWrapped);
                }
            }
        }
        
        return result;
    }

    /**
     * 把物件包裝成更簡單的資料格式
     * 
     * @param obj : 原本的資料物件
     * @param includeColumns : 要 include 的欄位 (其他欄位將會 set 成 null)
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T doSimpleWrap(T obj, String ... includeColumns) throws Exception {
        T result = null;
        
        if (obj != null) {
            result = obj;
            if (includeColumns != null && includeColumns.length > 0) {
                try {
                    List<String> columnList = Arrays.asList(includeColumns);
                    
                    Class<T> clazz = (Class<T>)obj.getClass();
                    result = clazz.newInstance();
                    BeanInfo info = Introspector.getBeanInfo(clazz);
                    PropertyDescriptor[] pds = info.getPropertyDescriptors();
                    if (pds != null && pds.length > 0) {
                        Object[] empty = new Object[]{};
                        for (PropertyDescriptor p : pds) {
                            String pName = p.getName();
                            Method read = p.getReadMethod();
                            Method write = p.getWriteMethod();
                            Object value = null;
                            try {
                                if (read != null && write != null) {
                                    if (columnList.contains(pName)) {
                                        value = read.invoke(obj, empty);
                                        write.invoke(result, value);
                                    }
                                }
                            } catch (Exception e) {
                                throw new Exception("doSimpleWrap() failed at : class=" + clazz.getSimpleName() + ", column=" + pName + ", value=" + value, e);
                            }
                        }
                    }
                    
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
        }
        
        return result;
    }
}
