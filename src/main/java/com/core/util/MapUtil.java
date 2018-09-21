package com.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Bonjour
 *
 */
public class MapUtil {
    private static List<AbstractObjectMapper<?>> mappers;
    
    static {
        // 初始化 ObjectMapper
        mappers = Collections.synchronizedList(new ArrayList<AbstractObjectMapper<?>>());

        // 排序
        Collections.sort(mappers);
    }

    /**
     * 把物件轉成 boolean 值
     * 
     * @param obj : 
     * @param noneZeroNumberEqualTrue : 當 true 時, 非零的數字會轉成 true 
     * @return
     */
    private static boolean parseBoolean(Object obj, boolean noneZeroNumberEqualTrue) {
        if (obj != null) {
            try {
                if (noneZeroNumberEqualTrue) { 
                    try {
                    	if(NumberUtil.isFloat(obj.toString())) {
                    		double d = Double.parseDouble(obj.toString());
                            if (Double.compare(d, 0) == 0) {
                                return false;
                            } else {
                                return true;
                            }
                    	}
                    	else {
                            if("true".equalsIgnoreCase(obj.toString()) || "false".equalsIgnoreCase(obj.toString()) 
                        			|| "1".equalsIgnoreCase(obj.toString()) || "0".equalsIgnoreCase(obj.toString())) {
                            	return Boolean.parseBoolean(obj.toString());
                            }
                    	}
                    } catch (Exception e) {
                        e.printStackTrace();
                        if("true".equalsIgnoreCase(obj.toString()) || "false".equalsIgnoreCase(obj.toString()) 
                    			|| "1".equalsIgnoreCase(obj.toString()) || "0".equalsIgnoreCase(obj.toString())) {
                        	return Boolean.parseBoolean(obj.toString());
                        }
                    }
                } else {
                	if("true".equalsIgnoreCase(obj.toString()) || "false".equalsIgnoreCase(obj.toString()) 
                			|| "1".equalsIgnoreCase(obj.toString()) || "0".equalsIgnoreCase(obj.toString())) {
                		return Boolean.parseBoolean(obj.toString());
                	}
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        return false;
    }
    
    /**
     * 判斷是否忽略 key <br>
     * 如果 key 在 ignore_keys 內, 則表示忽略, 回傳 true <br>
     * 否則回傳 false
     * 
     * @param key : 要判斷的 key
     * @param ignore_keys : 忽略的 key 值清單
     * @return
     */
    private static boolean ignoreKey(String key, String[] ignore_keys) {
        if (key != null && ignore_keys != null) {
            for (String ik : ignore_keys) {
                if (key.toUpperCase().equals(ik.toUpperCase())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 從 map 內尋找跟 k 相符的 key
     * 
     * @param k : 要找的 key 值
     * @param map : 
     * @param mapKeyPrefix : Map 內的 Key 值得 prefix
     * @return map 內跟 k 相符的 key 值
     */
    private static String getMatchKey(String k, Map<String, Object> map, String mapKeyPrefix) {
        String result = null;

        if (k != null && map != null && map.size() > 0) {
            Set<String> mapKeys = map.keySet();

            for (String mapK : mapKeys) {
                String copyK = k;
                if (mapKeyPrefix != null) {
                    copyK = mapKeyPrefix + k;
                }
                if (mapK.toUpperCase().equals(copyK.toUpperCase())) {
                    result = mapK;
                    break;
                }
            }
        }

        return result;
    }
    
    /**
     * 取得特例的 AbstractObjectMapper 物件
     * 
     * @param clazz : 類別
     * @return 如果 clazz 不是特例, 會回傳 null
     */
    private static AbstractObjectMapper<?> getMapper(Class<?> clazz) {
        AbstractObjectMapper<?> result = null;
        
        try {
            for (AbstractObjectMapper<?> m : mappers) {
                if (m.isApplicable(clazz)) {
                    result = m.getClass().newInstance();
                    break;
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return result;
    }

    /**
     * 把 Map 轉成物件
     * 
     * @param map : 要轉換的 map
     * @param clazz : 要轉成哪個物件 (指定 class)
     * @return 轉換後的物件
     */
    public static <T> T mapToObj(Map<String, Object> map, Class<T> clazz) {
        return mapToObj(map, clazz, null, false);
    }

    /**
     * 把 Map 轉成物件
     * 
     * @param map : 要轉換的 map
     * @param clazz : 要轉成哪個物件 (指定 class)
     * @param ignore_properties : 不處理的 property 名稱 (Object 內的 property 名稱)
     * @return 轉換後的物件
     */
    public static <T> T mapToObj(Map<String, Object> map, Class<T> clazz, String[] ignore_properties) {
        return mapToObj(map, clazz, ignore_properties, false);
    }
    
    /**
     * 把 Map 轉成物件
     * 
     * @param map : 要轉換的 map
     * @param clazz : 要轉成哪個物件 (指定 class)
     * @param ignore_properties : 不處理的 property 名稱 (Object 內的 property 名稱)
     * @param noMapper : true 表示不使用 ObjMapper
     * @return 轉換後的物件
     */
    public static <T> T mapToObj(Map<String, Object> map, Class<T> clazz, String[] ignore_properties, boolean noMapper) {
        return mapToObj(map, clazz, ignore_properties, noMapper, null);
    }
    
    /**
     * 把 Map 轉成物件
     * 
     * @param map : 要轉換的 map
     * @param clazz : 要轉成哪個物件 (指定 class)
     * @param ignore_properties : 不處理的 property 名稱 (Object 內的 property 名稱)
     * @param noMapper : true 表示不使用 ObjMapper
     * @param mapKeyPrefix : Map 內的 Key 值得 prefix
     * @return 轉換後的物件
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T mapToObj(Map<String, Object> map, Class<T> clazz, 
            String[] ignore_properties, boolean noMapper, String mapKeyPrefix) {
        // 特例的 class
        if (!noMapper) {
            AbstractObjectMapper mapper = getMapper(clazz);
            if (mapper != null) {
                return (T)mapper.mapToObj(map, clazz, ignore_properties, mapKeyPrefix);
            }
        }
        
        T result = null;

        if (map != null && clazz != null) {
            // BeanInfo
            try {
                boolean allValueIsNull = true;
                result = clazz.newInstance();

                // 取得物件還有屬性的資訊
                BeanInfo info = Introspector.getBeanInfo(clazz);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                if (pds != null && pds.length > 0) {
                    for (PropertyDescriptor p : pds) {
                        // 取得屬性的名稱, getter, setter
                        String pName = p.getName();
                        Method read = p.getReadMethod();
                        Method write = p.getWriteMethod();
                        Object value = null;

                        try {
                            // 如果 getter 和 setter 都存在再處理
                            if (read != null && write != null) {
    
                                // 判斷是否不處理
                                if (!ignoreKey(pName, ignore_properties)) {
                                    // 找出符合的 key 值
                                    String k = getMatchKey(pName, map, mapKeyPrefix);
                                    if (k != null) {
                                        allValueIsNull = false;
                                        // 從 map 取出值, 並呼叫 setter 把值寫入
                                        value = map.get(k);
                                        if (Boolean.class.isAssignableFrom(read.getReturnType()) 
                                                || boolean.class.isAssignableFrom(read.getReturnType())) {
                                            if (value != null) {
                                                write.invoke(result, parseBoolean(value, true));
                                            }
                                        } else {
                                            write.invoke(result, value);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                        	e.printStackTrace();
                        }
                    }
                }
                
                if (allValueIsNull) {
                    result = null;
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }

        return result;
    }
    
    /**
     * 把物件 obj 轉成 Map
     * 
     * @param obj : 要轉換的物件
     * @return 轉換後的 Map
     */
    public static HashMap<String, Object> objToMap(Object obj) {
        return objToMap(obj, null, false);
    }

    /**
     * 把物件 obj 轉成 Map
     * 
     * @param obj : 要轉換的物件
     * @param ignore_properties : 不處理的 property 名稱 (Object 內的 property 名稱)
     * @return 轉換後的 Map
     */
    public static HashMap<String, Object> objToMap(Object obj, String[] ignore_properties) {
        return objToMap(obj, ignore_properties, false);
    }

    /**
     * 把物件 obj 轉成 Map
     * 
     * @param obj : 要轉換的物件
     * @param ignore_properties : 不處理的 property 名稱 (Object 內的 property 名稱)
     * @param noMapper : true 表示不使用 ObjMapper
     * @return 轉換後的 Map
     */
    public static HashMap<String, Object> objToMap(Object obj, String[] ignore_properties, boolean noMapper) {
        return objToMap(obj, ignore_properties, noMapper, null);
    }

    /**
     * 把物件 obj 轉成 Map
     * 
     * @param obj : 要轉換的物件
     * @param ignore_properties : 不處理的 property 名稱 (Object 內的 property 名稱)
     * @param noMapper : true 表示不使用 ObjMapper
     * @param mapKeyPrefix : Map 內的 Key 值得 prefix
     * @return 轉換後的 Map
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static HashMap<String, Object> objToMap(Object obj, 
            String[] ignore_properties, boolean noMapper, String mapKeyPrefix) {

        // 特例的 class
        if (!noMapper) {
            if (obj != null) {
                AbstractObjectMapper mapper = getMapper(obj.getClass());
                if (mapper != null) {
                    return mapper.objToMap(obj, ignore_properties, mapKeyPrefix);
                }
            }
        }

        HashMap<String, Object> result = null;

        if (obj != null) {

            // BeanInfo
            try {
                result = new HashMap<String, Object>();
                // 用來當空陣列用
                Object[] empty = {};
                // 取得物件還有屬性的資訊
                BeanInfo info = Introspector.getBeanInfo(obj.getClass());
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                if (pds != null && pds.length > 0) {
                    for (PropertyDescriptor p : pds) {
                        // 取得屬性的名稱, getter, setter
                        String pName = p.getName();
                        Method read = p.getReadMethod();
                        Method write = p.getWriteMethod();
                        try {
                            // 如果 getter 和 setter 都存在再處理
                            if (read != null && write != null) {
                                // 判斷是否不處理
                                if (!ignoreKey(pName, ignore_properties)) {
                                    // 呼叫 setter 取出值, 並放進 map 內
                                    Object value = read.invoke(obj, empty);
                                    if (mapKeyPrefix != null) {
                                        result.put(mapKeyPrefix + pName, value);
                                    } else {
                                        result.put(pName, value);
                                    }
                                }
                            }
                        } catch (Exception e) {
                        	e.printStackTrace();
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
     * 取得類別內某個欄位的 getter 方法
     * 
     * @param clazz : 類別
     * @param propertyName : 欄位名稱
     * @return
     */
    public static Method getReadMethod(Class<?> clazz, String propertyName) {
        Method result = null;
        
        try {
            if (clazz != null && propertyName != null) {
                // 取得 WoEntity 類別資訊
                BeanInfo info = Introspector.getBeanInfo(clazz);
                PropertyDescriptor[] pds = info.getPropertyDescriptors();
                if (pds != null && pds.length > 0) {
                    for (PropertyDescriptor pd : pds) {
                        // 找出 orderByColumn 的 getter 方法
                        String pName = pd.getName();
                        Method read = pd.getReadMethod();
                        if (read != null
                                && pName != null
                                && pName.toUpperCase().equals(
                                        propertyName.toUpperCase())) {
                            result = read;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return result;
    }

    @SuppressWarnings("unchecked")
	public static <C, T> Map<C, T> listToMap(String mapKey, List<T> objList) {
    	Map<C, T> tmpMap = new HashMap<C, T>();
    	if(mapKey != null && objList != null ) {
	    	try {
	    		for(T obj : objList) {
	    			Field field = obj.getClass().getDeclaredField(mapKey);
	    			field.setAccessible(true);
	    			tmpMap.put((C) field.get(obj), obj);
	    		}
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
    	}
    	return tmpMap;
    }
}
