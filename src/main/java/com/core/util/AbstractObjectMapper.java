package com.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Bonjour
 *
 */
public abstract class AbstractObjectMapper<T> implements Comparable<AbstractObjectMapper<?>> {

	/**
     * 取得 Map 內的值
     * 
     * @param map : 
     * @param key : Map 的 key
     * @param mapKeyPrefix : Map 的 key 的 prefix
     * @return
     */
    private Object getMapValue(Map<String, Object> map, String key, String mapKeyPrefix) {
        Object result = null;
        
        if (map != null && key != null) {
            Set<String> mapKeys = map.keySet();
            for (String mapK : mapKeys) {
                String copyK = key;
                if (mapKeyPrefix != null) {
                    // 如果有 prefix, 則前面加上 prefix 來比較
                    copyK = mapKeyPrefix + key;
                }
                // 忽略大小寫來比較
                if (mapK.toLowerCase().equals(copyK.toLowerCase())) {
                    result = map.get(mapK);
                    break;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 回傳 IgnoreProperties 和 additionalIgnoreProperties 的集合
     * 
     * @param additionalIgnoreProperties : 額外的不處理的 Property 名稱 (Object 內的 Property 名稱)
     * @return
     */
    private String[] getAllIgnoreProperties(String[] additionalIgnoreProperties) {
        String[] result = null;
        Set<String> coll = new HashSet<String>();

        String[] ignoreProperties = getIgnoreProperties();
        
        // 把兩組 ignoreKeys 都放進 List
        if (ignoreProperties != null && ignoreProperties.length > 0) {
            coll.addAll(Arrays.asList(ignoreProperties));
        }
        if (additionalIgnoreProperties != null && additionalIgnoreProperties.length > 0) {
            coll.addAll(Arrays.asList(additionalIgnoreProperties));
        }
        if (coll.size() > 0) {
            result = coll.toArray(new String[coll.size()]);
        }
        
        return result;
    }
    
    /**
     * 取得不處理的 Property 名稱 (Object 內的 Property 名稱)  (這些不處理的欄位可能不塞值, 或是要經過特殊處理來轉換正確的值)
     * 
     * @return
     */
    protected abstract String[] getIgnoreProperties();
    
    /**
     * 把 Map 轉換成 Object 物件
     * 
     * @param map : 要轉換的 Map
     * @param clazz : 要把 Map 轉成哪個物件類別
     * @param additionalIgnoreProperties : 額外的不處理的 Property 名稱 (Object 內的 Property 名稱)
     * @param mapKeyPrefix : Map 內的 Key 值的 prefix
     * @return 轉換後的物件
     */
    public T mapToObj(Map<String, Object> map, Class<T> clazz, String[] additionalIgnoreProperties, String mapKeyPrefix) {
        String[] allIgnoreProperties = getAllIgnoreProperties(additionalIgnoreProperties);

        // 先處理不屬於 IgnoreProperties 的欄位
        T result = MapUtil.mapToObj(map, clazz, allIgnoreProperties, true, mapKeyPrefix);
        
        // 再來處理 IgnoreProperties 的欄位
        String[] ignoreProperties = getIgnoreProperties();
        if (ignoreProperties != null && ignoreProperties.length > 0) {
            try {
                if (result == null && clazz != null) {
                    result = clazz.newInstance();
                }
                List<String> pList = Arrays.asList(ignoreProperties);
                BeanInfo info = Introspector.getBeanInfo(result.getClass());
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                if (pds != null && pds.length > 0) {
                    for (PropertyDescriptor p : pds) {
                        String pName = p.getName();         // property 名稱
                        Method read = p.getReadMethod();    // getter 方法
                        Method write = p.getWriteMethod();  // setter 方法
                        if (read != null && write != null && pList.contains(pName)) {
                            // 先取得 Map 內的原始值
                            Object preValue = getMapValue(map, pName, mapKeyPrefix);
                            Object value = null;
                            try {
                                // 呼叫方法來取得處理後的值
                                value = mapToObj_Value(pName, preValue);
                            } catch (Exception e) {
                            	e.printStackTrace();
                            }
                            try {
                                // 把值寫入 Object 內
                                write.invoke(result, value);
                            } catch (Exception e) {
                            	e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        return result;
    }
    
    /**
     * 當處理 IgnoreProperties 的值時, 會呼叫此方法, 把 Map 內的值轉換成 Object 內的 property 值
     * 
     * @param propertyName : Object 內的 property 名稱
     * @param value : Map 內取出的原始值
     * @return 處理後的值
     */
    protected abstract Object mapToObj_Value(String propertyName, Object value) throws Exception;

    /**
     * 把物件轉換成 HashMap
     * 
     * @param obj : 要轉換的物件
     * @param additionalIgnoreProperties : 額外的不處理的 Property 名稱 (Object 內的 Property 名稱)
     * @param mapKeyPrefix : Map 內的 Key 值的 prefix
     * @return 轉換後的 HashMap
     */
    public HashMap<String, Object> objToMap(T obj, String[] additionalIgnoreProperties, String mapKeyPrefix) {
        String[] allIgnoreProperties = getAllIgnoreProperties(additionalIgnoreProperties);

        // 先處理不屬於 IgnoreProperties 的欄位
        HashMap<String, Object> result = MapUtil.objToMap(obj, allIgnoreProperties, true, mapKeyPrefix);

        // 再來處理 IgnoreProperties 的欄位
        String[] ignoreProperties = getIgnoreProperties();
        if (ignoreProperties != null && ignoreProperties.length > 0) {
            if (result == null) {
                result = new HashMap<String, Object>();
            }
            try {
                Object[] empty = new Object[]{};
                List<String> pList = Arrays.asList(ignoreProperties);
                BeanInfo info = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                if (pds != null && pds.length > 0) {
                    for (PropertyDescriptor p : pds) {
                        String pName = p.getName();         // property 名稱
                        Method read = p.getReadMethod();    // getter 方法
                        Method write = p.getWriteMethod();  // setter 方法
                        if (read != null && write != null && pList.contains(pName)) {
                            // 先取得 Object 內的原始值
                            Object preValue = read.invoke(obj, empty);
                            Object value = null;
                            try {
                                // 呼叫方法來取得處理後的值
                                value = objToMap_Value(pName, preValue);
                            } catch (Exception e) {
                            	e.printStackTrace();
                            }
                            // 把值寫入 Map 內
                            if (mapKeyPrefix != null) {
                                result.put(mapKeyPrefix + pName, value);
                            } else {
                                result.put(pName, value);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }

        return result;
    }
    
    /**
     * 當需要處理 IgnoreProperties 欄位的值時, 會呼叫此方法, 把 Object 內的 property 值轉換成  Map 內的值
     * 
     * @param propertyName : Object 內的 property 名稱
     * @param value : Object 內的原始值
     * @return 處理後的值
     */
    protected abstract Object objToMap_Value(String propertyName, Object value) throws Exception;

    /**
     * 檢查 ObjectMapper 是否可以套用在 clazz 上
     * 
     * @param clazz
     * @return true 表示可以套用在 clazz 上
     */
    public abstract boolean isApplicable(Class<?> clazz);

    /**
     * 用來排序用的
     * 
     * @return
     */
    public int getOrder() {
        return 100;
    }
    
    @Override
    public int compareTo(AbstractObjectMapper<?> o) {
        if (o != null) {
            int order1 = getOrder();
            int order2 = o.getOrder();

            return order1 - order2;
        }
        
        return 0;
    }
}
