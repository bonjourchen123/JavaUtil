package com.core.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Bonjour
 *
 */
public class SQLUtil {
	
	private static final String br = "\r\n";
	private static final String tab = "\t";
	private final static String ASC = "ASC";
	private final static String DESC = "DESC";
	
	/**
	 * 回傳一個order by的sql語句
	 * 
	 * @param key
	 * @param bOrder
	 * @return
	 */
	public static String getOrderByString(String key, boolean bOrder) {
		StringBuffer sb = new StringBuffer("");
		if(key!=null && key.length() > 0) {
			sb.append(String.format(" ORDER BY %s %s", key,getOrderString(bOrder)));
		}
		
		return sb.toString();
	}

	private static Object getOrderString(boolean bOrder) {
		return bOrder?SQLUtil.ASC:SQLUtil.DESC;
	}
	
	/**
	 * 切割list 每1千1筆
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<List<T>> cuttingSQLIn(List<T> list) {
		List<List<T>> res = new ArrayList<List<T>>();
		if(list!=null && list.size() > 0) {
			for(int i = 0, tmpSize = list.size(); i < tmpSize; i = i+1000) {
				res.add(list.subList(i, (i+1000 < tmpSize)?i+1000:tmpSize));
			}
		}
		return res;
		
	}
	
    /**
     * 對多個key做多個like。
     * 
     * @param sqlStr :sqlStr
     * @param keys :keys
     * @param likes :likes
     * @return
     */
    public static String appendKeysLikes(String sqlStr,List<String> likes,String ... key) {
        List<String> keys = new ArrayList<String> ();
        if(key!=null && key.length>0){
            for(String k : key){
                keys.add(k); 
            } 
        }
        return appendKeysLikes(sqlStr, keys,likes);
    }
	
    /**
     * 對多個key做多個like。
     * 
     * @param sqlStr :sqlStr
     * @param keys :keys
     * @param likes :likes
     * @return
     */
	public static String appendKeysLikes(String sqlStr,List<String> keys,List<String> likes) {
	    StringBuffer sb = new StringBuffer();
	    if(sqlStr!=null){
	        sb.append(sqlStr);
	    }
        if(keys!=null && keys.size() > 0){
            boolean isFirst = true;
            for(String key : keys){
                if(isFirst){
                    sb.append(" AND ( ");
                    sb.append(getKeyLikes(key,likes));
                    isFirst = false;
                }else{
                    sb.append(" OR ");
                    sb.append(getKeyLikes(key,likes));
                } 
            } 
            sb.append(" ) ");
        }else{
            sb.append(" 1 = 0 ");
        }
        return sb.toString();
    }
    
    /**
     * 對key做多個like。
     * 
     * @param key :key
     * @param likes :likes
     * @return
     */
	public static String getKeyLikes(String key ,List<String> likes) {
        StringBuffer sb = new StringBuffer();
        if(likes!=null && likes.size() > 0){
            boolean isFirst = true;
            for(String like : likes){
                if(isFirst){
                    sb.append(" ( "+key+" LIKE '"+like+"' ");
                    isFirst = false;
                }else{
                    sb.append(" OR "+key+" LIKE '"+like+"' ");
                } 
            } 
            sb.append(" ) ");
        }else{
            sb.append(" 1 = 0 ");
        }
        return sb.toString();
    }
	
    /**
     * 取得批次insert大量entities的sql。
     * 
     * INSERT ALL
     *   INTO mytable (column1, column2, column3) VALUES ('val1.1', 'val1.2', 'val1.3')
     *   INTO mytable (column1, column2, column3) VALUES ('val2.1', 'val2.2', 'val2.3')
     *   INTO mytable (column1, column2, column3) VALUES ('val3.1', 'val3.2', 'val3.3')
     * SELECT * FROM dual;
     * 
     * @param entities :entities
     * @return
     * @throws Exception 
     */
    public static <T> String getBatchInsertSql(List<T> entities) throws Exception{
        StringBuffer sb = new StringBuffer();
        if(entities!=null && entities.size()>0){
            String tableName = parseTableName(entities.get(0).getClass());
            String columnString = parseColumnString(entities.get(0).getClass());
            if(tableName!=null && !tableName.isEmpty() && columnString!=null && !columnString.isEmpty()){
                sb.append("INSERT ALL ");//開始
                for(T entity : entities){
                    sb.append(" INTO "+tableName+" "+columnString+" VALUES "+parseColumnValues(entity)); 
                } 
                sb.append(" SELECT * FROM dual ");//結束
            }
        }
        return sb.toString();
    }
    
    /**
     * 取得entity的table名。
     * 
     * @param clazz :clazz
     * @return
     */
    private static String parseTableName(Class<?> clazz){
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if(tableAnnotation!=null){
            String tableName = tableAnnotation.name();
            return tableName;
        }else{
            return null; 
        }        
    }
    

    /**
     * 取得entity的columns。如:(column1, column2, column3)
     * 
     * @param clazz :clazz
     * @return
     */
    private static String parseColumnString(Class<?> clazz){
        StringBuffer sb = new StringBuffer();
        boolean isFirst = true;
        Method[] methods = clazz.getMethods();
        
        if(methods!=null && methods.length>0){
            for(Method method : methods){
                Column columnAnnotation = method.getAnnotation(Column.class);
                if(columnAnnotation!=null && columnAnnotation.name()!=null && !columnAnnotation.name().isEmpty()){
                    String columnName = columnAnnotation.name();
                    if(isFirst){
                        sb.append("( "+columnName);
                        isFirst = false;
                    }else{
                        sb.append(" , "+columnName);
                    }
                }
            }
        }
        if(!sb.toString().isEmpty()){
            sb.append(" )"); 
        }
        return sb.toString();      
    }
    
    /**
     * 取得bean的values。如:('val1.1', 'val1.2', 'val1.3')
     * 
     * @param clazz :clazz
     * @return
     */
    private static <T> String parseColumnValues(T bean) throws Exception{
        StringBuffer sb = new StringBuffer();
        boolean isFirst = true;
        Method[] methods = bean.getClass().getMethods();
        if(methods!=null && methods.length>0){
            for(Method method : methods){
                Column columnAnnotation = method.getAnnotation(Column.class);
                if(columnAnnotation!=null && columnAnnotation.name()!=null && !columnAnnotation.name().isEmpty()){
                    if(isFirst){
                        sb.append("( "+parseValueString(bean,method));
                        isFirst = false;
                    }else{
                        sb.append(" , "+parseValueString(bean,method));
                    }
                }
            }
        }
        if(!sb.toString().isEmpty()){
            sb.append(" )"); 
        }
        return sb.toString();      
    }
    
    /**
     * 取得bean的values時，不同values的格式不同，如string要單引號包著，date要格式化等等。
     * 
     * @param bean :bean
     * @param method :method
     * @return
     */
    @SuppressWarnings("deprecation")
	private static String parseValueString(Object bean,Method method) throws Exception{
        Object returnObj = method.invoke(bean);
        if(returnObj!=null){
            if(returnObj instanceof String){
                return "'" + BaseUtil.getStr(returnObj)+"'"; //字串幫加引號
            }else if(returnObj instanceof Date){
                Date returnDate = (Date)returnObj;
                return "TO_DATE('"+DateUtil.format(returnDate, DateUtil.FORMAT_DATETIME_2) + "','yyyymmddhh24miss')"; // 日期幫format
            }else{
                return BaseUtil.getStr(returnObj); //其他直接string 有例外用到再加吧。  
            }   
        }else{
            return "null"; //null 就插null進db。
        }

    }
    
    public static String toPrettySQL(StringBuffer sb) {
		return toPrettySQL(sb.toString());
	}

	public static String toPrettySQL(StringBuilder sb) {
		return toPrettySQL(sb.toString());
	}

	public static String toPrettySQL(String sql) {
		sql = StringUtils.replaceOnce(sql, "select ", "select" + br + tab);
		sql = StringUtils.replaceOnce(sql, "from", br + "from");
		sql = StringUtils.replace(sql, "join", br + "join");
		sql = StringUtils.replace(sql, " on ", br + tab + "on ");
		sql = StringUtils.replace(sql, "where", br + "where");
		return sql;
	}

	/**
	 * 將傳入的陣列 par_strs 串成一個字串, 並在每個陣列元素之間插入 symbol <br>
	 * <br>例: <br> 
	 * <table border="1">
	 *      <tr><th>參數名稱</th><th>傳入值</th></tr>
	 *      <tr><th>strs</th><th>{"table.aaa","table.bbb","table.ccc"}</th></tr>
	 *      <tr><th>symbol</th><th>","</th></tr>
	 *      <tr><th>quotation</th><th>true</th></tr>
	 *      <tr><th>removedot</th><th>true</th></tr>
	 * </table>
	 * 會回傳 'aaa', 'bbb', 'ccc'<br>
	 * 
	 * @param par_strs : 要合併的字串的陣列
	 * @param symbol : 要在陣列元素中插入的符號
	 * @param quotation : true 的話, 會在每個陣列元素加上單引號(')
	 * @param removedot : true 的話, 會把每個陣列元素句點(.)前的字串移除
	 * @return
	 */
	public static String StringArrayToStringWithSymbol(Object[] par_strs, String symbol, boolean quotation, boolean removedot){
		if( par_strs == null || par_strs.length == 0 ){
			return "";
		}
		
		String[] strs = new String[par_strs.length];
		for( int i=0 ; i<par_strs.length ; i++ ){
			strs[i] = par_strs[i].toString();
		}
		
		if( removedot ){
			for( int i=0 ; i<strs.length ; i++ ){
				int dot_i = strs[i].lastIndexOf(".");
				if( dot_i != -1 ){
					strs[i] = strs[i].substring(dot_i+1);
				}
			}
		}
		
		StringBuffer result = new StringBuffer("");
		if(quotation){
			result.append("'");
		}
		result.append(strs[0]);
		if(quotation){
			result.append("'");
		}
		
		for( int i=1 ; i<strs.length ; i++ ){
			result.append(symbol).append(" ");
			if(quotation){
				result.append("'");
			}
			result.append(strs[i]);
			if(quotation){
				result.append("'");
			}
		}
		
		return result.toString();
	}
	
	/**
	 *  將傳入的List 串成一個字串, 並在每個元素之間插入 symbol <br>
	 * <br>例: <br> 
	 * <table border="1">
	 *      <tr><th>參數名稱</th><th>傳入值</th></tr>
	 *      <tr><th>strs</th><th>{"table.aaa","table.bbb","table.ccc"}</th></tr>
	 *      <tr><th>symbol</th><th>","</th></tr>
	 *      <tr><th>quotation</th><th>true</th></tr>
	 *      <tr><th>removedot</th><th>true</th></tr>
	 * </table>
	 * 會回傳 'aaa', 'bbb', 'ccc'<br>
	 * 
	 * @param strs : 要合併的字串的陣列
	 * @param symbol : 要在陣列元素中插入的符號
	 * @param quotation : true 的話, 會在每個陣列元素加上單引號(')
	 * @param removedot : true 的話, 會把每個陣列元素句點(.)前的字串移除
	 * @return
	 */
	public static String StringArrayToStringWithSymbol(List<String> strs, String symbol, boolean quotation, boolean removedot){
		if( strs != null && strs.size() > 0 ){
			return StringArrayToStringWithSymbol(strs.toArray(new String[strs.size()]), symbol, quotation, removedot);
		}
		return "";
	}
	
	// hibernate 用
//	public static String toSql(Criteria criteria) {
//		try {
//			CriteriaImpl c = (CriteriaImpl) criteria;
//			SessionImpl s = (SessionImpl) c.getSession();
//			SessionFactoryImplementor factory = (SessionFactoryImplementor) s.getSessionFactory();
//			String[] implementors = factory.getImplementors(c.getEntityOrClassName());
//			CriteriaLoader loader = new CriteriaLoader((OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
//					factory, c, implementors[0], s.getLoadQueryInfluencers());
//			Field f = OuterJoinLoader.class.getDeclaredField("sql");
//			f.setAccessible(true);
//			return (String) f.get(loader);
//		} catch ( Exception e ) {
//			throw new RuntimeException(e);
//		}
//	}
}
