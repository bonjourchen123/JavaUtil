package com.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author Bonjour
 *
 */
public class StringUtil {
	
	/**
     * 把str轉成excel可以接受的sheet name，即去掉: \ / ? * [ ]，換成所想要的分隔號符號，預設是|。
     * 
     * @param str : 傳入字串
     * @return
     */
    public static String replaceSheetName(String str) {
        return replaceSheetName(str,"|");
    }
    
    /**
     * 把str轉成excel可以接受的sheet name，即去掉: \ / ? * [ ]，換成所想要的分隔號符號。
     * 
     * @param str : 傳入字串
     * @param defaultChar : 將不合法符號取代成此符號。
     * @return
     */
    public static String replaceSheetName(String str,String defaultChar) {
        return BaseUtil.getStr(str).replaceAll("[\\\\/\\*?\\[\\]:]", BaseUtil.getStr(defaultChar));
    }
    
    /**
     * 把字串內的換行符號 \n 取代成 ASCII 換行符號 10
     * 
     * @param str : 傳入字串
     * @return
     */
    public static String replaceNewLine_ASCII(String str) {
        String result = str;
        
        if (result != null) {
            result = result.replace("\r\n", ""+(char)10+"");
            result = result.replace("\n", ""+(char)10+"");
        }
        
        return result;
    }

    /**
     * 把字串內的換行符號 \n 取代成 HTML 換行符號 < br>
     * 
     * @param str : 傳入字串
     * @return
     */
    public static String replaceNewLine_HTML(String str) {
        String result = str;
        
        if (result != null) {
            result = result.replace("\r\n", "<br/>");
            result = result.replace("\n", "<br/>");
        }
        
        return result;
    }
    
    /**
     * 取得字串用某字元分段後，第幾段字串。當你取index大於段數或index小於0或regex是null，將回空字串(不是null)。
     * 
     * @param input : 傳入字串
     * @param regex : 分段字串
     * @param index : 第幾段字串，0開始
     * @return
     */
    public static String getSplitStr(String input,String regex,int index){
        String result = "";
        String content = BaseUtil.getStr(input);
        String regex_noNull = BaseUtil.getStr(regex);
        String[] tokens = content.split(regex_noNull);
        if(tokens!=null && index >= 0 && index < tokens.length){
            result = tokens[index];
        }
        return result;
    }
    
    /**
     * 判斷字串是否為 null 或是空字串
     * 
     * @param str : 字串
     * @return
     */
    public static boolean isEmpty(String str) {
        boolean result = false;
        
        if (str == null || "".equals(str.trim())) {
            result = true;
        }
        
        return result;
    }
    
    /**
     * 把檔名分割成兩字串 : <br>
     * str[0] = 檔名 <br>
     * str[1] = 副檔名
     * 
     * @param filename : 檔名
     * @return
     */
    public static String[] splitFilenameAndExt(String filename) {
        String[] result = new String[2];
        
        if (filename != null) {
            int index = filename.lastIndexOf(".");
            if (index != -1) {
                if (filename.length() > index+1) {
                    // 有附檔名
                    result[0] = filename.substring(0, index);
                    result[1] = filename.substring(index+1);
                } else {
                    // 檔名最後是 .
                    result[0] = filename;
                    result[1] = null;
                }
            } else {
                // 沒有附檔名
                result[0] = filename;
                result[1] = null;
            }
        }
        
        return result;
    }
    
    public static String trimToString(Object obj) {
		String result = "";
		
		if ( obj == null ) return result;
		
		if ( obj instanceof Collection ) {
			result = StringUtils.trimToEmpty(obj.toString());
		} else if ( obj instanceof Map ) {
			result = StringUtils.trimToEmpty(obj.toString());
		} else if ( obj instanceof Object[] ) {
			result = StringUtils.trimToEmpty(ArrayUtils.toString(obj));
		} else if ( obj instanceof String ) {
			result = StringUtils.trimToEmpty((String) obj);
		} else {
			result = "";
		}
		return result;
	}
	
	public static boolean isOnlyASCII(String str) {
		if ( BeanUtil.isEmpty(str) ) return true;
		Pattern p = Pattern.compile("[-\\w\\s]*");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public static String toString(Object obj) {
		return ReflectionToStringBuilder.toString(obj, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public static String parseNumber(String str) {
		String result = null;
		
		if ( BeanUtil.isNotEmpty(str) && StringUtil.isDigitsOnly(str) ) {
			result = StringUtils.replace(StringUtils.trim(str), ",", "");
		}
		
		return result;
	}
	
	/**
     * 擷取字串裡的數字
     *
     * @param s     要擷取的字串
     * @return String   擷取結果
     */
	public static String getDigitsOnly(String s) {
		StringBuffer result = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				result.append(c);
			}
		}
		return result.toString();
	}

	/**
	 * @return
	 */
	public static boolean isDigitsOnly(String s) {
		String regex = "^0(\\.0*)?$|^(-?0?\\.0*[1-9]\\d*)$|^(-?[1-9]((\\d{0,2},(\\d{3},)*\\d{3})|(\\d)*)(\\.\\d*)?)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);		
		return m.matches();
	}
	
	/**
	 * 將指定pattern的字串replace
	 * 
	 * @param str
	 * @param leftPattern
	 * @param rightPattern
	 * @param keyValues
	 * @return
	 */
	public static String replace(String str, String leftPattern, String rightPattern, Map<String, Object> keyValues) {
		for (String key : keyValues.keySet()) {
			if(BeanUtil.isNotEmpty(keyValues.get(key))) {
				str = StringUtils.replace(str, leftPattern + key + rightPattern, keyValues.get(key).toString());
			}
		}
		return str;
	}
	
	public static String toUpperCase(String str) {
		if ( !BeanUtil.isEmpty(str) ) {
			return StringUtils.trim(str).toUpperCase();
		} else {
			return "";
		}
	}
	
	public static String toLowerCase(String str) {
		if ( !BeanUtil.isEmpty(str) ) {
			return StringUtils.trim(str).toLowerCase();
		} else {
			return "";
		}
	}
	
	/**
	 * 將字串理指定範圍的字串轉大寫
	 * 
	 * @param str
	 * @param indexBegin
	 * @param indexEnd
	 * @return
	 */
	public static String toUpperCase(String str, int indexBegin, int indexEnd) {
		StringBuilder sb = new StringBuilder();
		if(BeanUtil.isNotEmpty(str) 
				&& indexBegin >= 0 && indexBegin < str.length()
				&& indexEnd >= indexBegin && indexEnd < str.length()) {
			
			String rangeStr = str.substring(indexBegin, indexEnd + 1);
			sb.append(str).replace(indexBegin, indexEnd + 1, rangeStr.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 * 將字串理指定範圍的字串轉小寫
	 * 
	 * @param str
	 * @param indexBegin
	 * @param indexEnd
	 * @return
	 */
	public static String toLowerCase(String str, int indexBegin, int indexEnd) {
		StringBuilder sb = new StringBuilder();
		if(BeanUtil.isNotEmpty(str) 
				&& indexBegin >= 0 && indexBegin < str.length()
				&& indexEnd >= indexBegin && indexEnd < str.length()) {
			
			String rangeStr = str.substring(indexBegin, indexEnd + 1);
			sb.append(str).replace(indexBegin, indexEnd + 1, rangeStr.toLowerCase());
		}
		return sb.toString();
	}
	
	public static boolean isPositiveNum(String str) {
		if(BeanUtil.isNotEmpty(str)) {
			String pattern = "^[0-9]*[1-9][0-9]*$";
			return str.matches(pattern);
		}
		return false;
	}
	
	public static String concats(Object ...objs) {
		if (objs == null) return null;
		
		StringBuffer buf = new StringBuffer();
		for (Object obj : objs) {
			if (obj == null) continue;
			buf.append(obj.toString());
		}
		
		return buf.toString();
	}
    
	/**
	 * 把字串內的換行符號 \n 取代成 HTML 換行符號 < br>
	 * 
	 * @param str
	 *            : 傳入字串
	 * @return
	 */
	public static String replaceNewLine2HtmlBr(String str) {
		String result = str;

		if (result != null) {
			result = result.replace("\r\n", "<br/>");
			result = result.replace("\n", "<br/>");
		}

		return result;
	}
	
	/**
	 * 將資料庫或設定檔中的{OOOO}值取代為真正的值
	 * ex: 輸入 /mobile/eservice/{loginType}/it/n , [{key : loginType , value : public}]
	 * 	         將回傳  /mobile/eservice/public/it/n
	 * 
	 * @param String
	 * @param Map<String,String>
	 * 
	 * @return String
	 * **/
	public static String replaceConstantValue(String constantString, Map<String,String> replaceMap){
		String returnString = constantString;
		
		for(String key : replaceMap.keySet()){
			returnString = returnString.replace("{"+key+"}", replaceMap.get(key));
		}
		
		return returnString;
	} 
	
	
	/**
	 * 將資料庫或設定檔中的{OOOO}值取代為真正的值
	 * ex: 輸入 /mobile/eservice/{loginType}/it/n , [{key : loginType , value : public}]
	 * 	         將回傳  /mobile/eservice/public/it/n
	 * 
	 * @param String
	 * @param Map<String,String>
	 * 
	 * @return String
	 * **/
	public static String replaceConstantValue(String constantString, String key, String value){
		String returnString = constantString;
		
		returnString = returnString.replace("{"+key+"}", value);
		
		return returnString;
	} 

	/**
     * 生成指定位數的隨機正整數字串
     * 
     * @param digit
     * @return
     */
    public static String getRandomNum(int digit){
        if(digit <= 1){
            return Integer.toString((int) (Math.random() * 10d));
        }else{
            return Integer.toString((int) (Math.random() * 10d)) + getRandomNum(digit - 1);
        }
    }
    
    /**
	 * 將DB字元(可能含有null)轉為字串
	 * 
	 * @param value
	 * @return
	 */
	public static String convertString(String value) {
		String str = "";
		if (value != null) {
			String temp = value.trim();
			if (temp.equalsIgnoreCase("null"))
				temp = "";
			str = temp;
		}
		return str;
	}

	/**
	 * 將DB字元(可能含有null)轉為數字
	 * 
	 * @param value
	 * @return
	 */
	public static int convertInt(Object value) {
		int index = 0;
		if (value != null) {
			String temp = value.toString();
			if (temp != null && !temp.trim().equalsIgnoreCase("null")) {
				if (Integer.parseInt(temp) > 0) {
					index = Integer.parseInt(temp);
				}
			}
		}
		return index;
	}

	/**
	 * 填入參數至系統訊息中, 例:請輸入 {0}！
	 * 
	 * @param msg
	 * @param values
	 * @return
	 */
	public static String replaceSysMsg(String msg, String[] values) {
		for (int i = 0; i < values.length; i++) {
			msg = msg.replace("{" + i + "}", values[i]);
		}
		return msg;
	}

	/**
	 * 將double轉為String並加入千分號
	 * 
	 * @param data
	 * @return
	 */
	public static String inserComma(double data) {
		if (data == 0.0) {
			return "";
		} else {
			DecimalFormat df = new DecimalFormat();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setGroupingSeparator(',');
			df.setDecimalFormatSymbols(dfs);
			return df.format(new BigDecimal(data));
		}
	}

	/**
	 * Transform double to String(小數後面0位, 四捨五入)
	 * 
	 * @param money
	 * @return
	 */
	public static String double2String(double money) {
		BigDecimal bd = new BigDecimal(money);
		bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);// 小數後面0位, 四捨五入
		return bd.toString();
	}

	/**
	 * 把字符串中的每一碼都轉換成數字
	 */
	public static int[] toInt(String str) {
		int[] a = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			a[i] = Integer.parseInt(str.substring(i, i + 1));
		}

		return a;
	}
	
	/**
	 * 將字串List轉為逗號分隔的字串, 如str1,str2,str3
	 * 
	 * @param arr
	 * @return
	 */
	public static String arrayToString(List<String> arr) {
		String result = "";
		int i = 0;
		for (String str : arr) {
			if (i > 0) {
				result += "," + str;
			} else {
				result += str;
			}
			i++;
		}
		return result;
	}

	/**
	 * 將資料(可能含有null)轉為空字串
	 * 
	 * @param inputObj
	 * @return
	 */
	public static String nullToString_NoEncoding(Object inputObj) {
		if (inputObj == null) {
			return "";
		}
		else {
			return ( (String) inputObj).trim();
		}
	}

	/**
	 * 將DB物件(可能含有null)轉為字串
	 * 
	 * @param obj
	 * @return
	 */
	public static String objToStr(Object obj){
		return obj==null?"":obj.toString().trim();		
	}
	
	/**
	 * 將DB物件(可能含有null)轉為double
	 * 
	 * @param obj
	 * @return
	 */
	public static Double objToDouble(Object obj){
		return obj==null?0:Double.parseDouble(obj.toString().trim());
	}
	
	/**
	 * 將DB物件(可能含有null)轉為int
	 * 
	 * @param obj
	 * @return
	 */
	public static int objToInt(Object obj){
		return obj==null?0:Integer.parseInt(obj.toString().trim());
	}
	
	/**
	 * 取得字串長度
	 * @param str
	 * @return
	 */
	public static int length(String str){
		return ((str == null) ? 0 : str.length());
	}
	
	/**
	 * startsWith
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(String str, String prefix) {
		return startsWith(str, prefix, false);
	}
	
	/**
	 * startsWith
	 * @param str
	 * @param prefix
	 * @param ignoreCase
	 * @return
	 */
	private static boolean startsWith(String str, String prefix,
			boolean ignoreCase) {
		if ((str == null) || (prefix == null)) {
			return ((str == null) && (prefix == null));
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}
	
	/**
	 * endsWith
	 * @param str
	 * @param suffix
	 * @return
	 */
	public static boolean endsWith(String str, String suffix) {
		return endsWith(str, suffix, false);
	}
	
	/**
	 * endsWith
	 * @param str
	 * @param suffix
	 * @param ignoreCase
	 * @return
	 */
	private static boolean endsWith(String str, String suffix,
			boolean ignoreCase) {
		if ((str == null) || (suffix == null)) {
			return ((str == null) && (suffix == null));
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0,
				suffix.length());
	}

    /**
     * 去空白
     * 
     * @param str
     * @return
     */
    public static String trimAll(String str) {
        return trimAll(str, "");
    }

    /**
     * 去空白
     * 
     * @param str
     * @param suffix
     * @return
     */
    public static String trimAll(String str, String suffix) {
        if (null != str) {
            return str.replaceAll("\\s+", suffix);
        } else {
            return "";
        }
    }
    
    /**
     * Get month string from year-month (ex: "2013/12" > "12")
     * 
     * @param yearMonth
     * @return
     */
    public static String getMonthFromDateString(String yearMonth) {
        String month = "";
        if (yearMonth != null && !yearMonth.equals("") && yearMonth.indexOf("/") > 0) {
            month = yearMonth.substring(yearMonth.indexOf("/")+1, yearMonth.length());
        } else {
            month = yearMonth;
        }
        return month;
    }

    /**
     * null字串  改成null
     * 
     * @param value
     * @return
     */
    public static String nullString(String value) {
        if (value == null || "null".equals(value))
            return null;
        else
            return value;
    }
    
    /**
     * 字串是否為數字
     * @param value
     * @return
     */
    public static boolean isNumber(String value){
        try{
            Integer.parseInt(value);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
	public static String StringToNcr(String value) {
		StringBuilder sb = new StringBuilder();
		try {
			for(char tmp : value.toCharArray()) {
				String newStr = new String(String.valueOf(tmp).getBytes("BIG5"), "BIG5");
				if("?".equals(newStr) && !"?".equals(tmp)) {
					sb.append(StringEscapeUtils.escapeHtml(String.valueOf(tmp)));
				}
				else {
					sb.append(tmp);
				}
			}
		} catch(Exception ex) {
		}
		return sb.toString();
	}
	
	public static String NcrToString(String value) {
		return StringEscapeUtils.unescapeHtml(value);
	}
    
}
