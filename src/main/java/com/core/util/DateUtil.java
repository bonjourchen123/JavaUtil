package com.core.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 若使用 java8 以上版本建議使用新的 LocalDateTime 系列 api
 * 
 * @author Bonjour
 *
 */
@Deprecated
public class DateUtil {
	
	/**
     * 預設日期格式 - 日期部分
     */
    public static final String FORMAT_Date = "yyyy/MM/dd";
    
    /**
     * 預設日期格式 - 時間部分
     */
    public static final String FORMAT_Time = "HH:mm:ss";
    
    /**
     * 預設日期格式 - 時間部分 只有時、分
     */
    public static final String FORMAT_TIME_HHMM = "HH:mm";
    
    /**
     * 預設日期格式 - 日期+時間
     */
    public static final String FORMAT_DateTime = "yyyy/MM/dd HH:mm:ss";
    
    /**
     * 預設日期格式 - 日期+時間 沒有分號
     */
    public static final String FORMAT_DATETIME_2 = "yyyyMMddHHmmss";
    
    /**
     * 預設日期格式 - 日期+時間部分 只有時、分
     */
    public static final String FORMAT_DateTime_HHMM = "yyyy/MM/dd HH:mm";
   
    private DateUtil() {
        
    }
    
    /**
     * 將日期物件轉成字串 (將套用預設格式)
     * 
     * @param date : 日期物件
     * @return
     */
    public static String format(Date date) {
        return format(date, FORMAT_DateTime);
    }

    /**
     * 將日期物件轉成字串
     * 
     * @param date : 日期物件
     * @param format : 格式 (傳入 null 的話, 將套用預設格式)
     * @return
     */
    public static String format(Date date, String format) {
        String result = null;
        
        try {
            if (date != null) {
                SimpleDateFormat simple = null;
                if (format != null) {
                    simple = new SimpleDateFormat(format);
                } else {
                    simple = new SimpleDateFormat(FORMAT_DateTime);
                }
                result = simple.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 將日期物件轉成字串 (將套用預設格式)
     * 
     * @param date : 日期物件
     * @return
     */
    public static String formatWithDefaultValue(Date date, String defaultValue) {
        return formatWithDefaultValue(date, FORMAT_DateTime, defaultValue);
    }

    /**
     * 將日期物件轉成字串
     * 
     * @param date : 日期物件
     * @param format : 格式 (傳入 null 的話, 將套用預設格式)
     * @return
     */
    public static String formatWithDefaultValue(Date date, String format, String defaultValue) {
        String result = null;
        
        result = format(date, format);
        if (result == null) {
            result = defaultValue;
        }

        return result;
    }
    
    /**
     * 把日期格式的字串轉成日期物件 (將套用預設格式來做轉換)
     * 
     * @param date : 日期字串
     * @return
     */
    public static Date parse(String date) {
        return parse(date, FORMAT_DateTime);
    }

    /**
     * 把日期格式的字串轉成日期物件
     * 
     * @param date : 日期字串
     * @param format : 日期字串的格式 (傳入 null 的話, 將套用預設格式來做轉換)
     * @return
     */
    public static Date parse(String date, String format) {
        Date result = null;

        try {
            if (date != null && !date.trim().equalsIgnoreCase("") ) {
                SimpleDateFormat simple = null;
                if (format != null) {
                    simple = new SimpleDateFormat(format);
                } else {
                    simple = new SimpleDateFormat(FORMAT_DateTime);
                }
                result = simple.parse(date);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 將日期加一天以利查詢
     * 
     * @param date : 日期字串
     * @param format : 日期字串的格式 (傳入 null 的話, 將套用預設格式來做轉換)
     * @return
     */   
    public static Date reportParseDay(String date, String format) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(parse(date,format));
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.SECOND,-1);
    	return cal.getTime() ;
    };
    
    /**
     * 檢查兩日期的相差值, 是否在一定的誤差之內
     * 
     * @param date1 : 要檢查的日期之一
     * @param date2 : 要檢查的日期之二
     * @param diffMs : 可以接受的誤差範圍 (毫秒)
     * @return
     */
    public static boolean equalByDiff(Date date1, Date date2, long diffMs) {
        boolean result = false;
        
        if (date1 != null && date2 != null) {
            long ms1 = date1.getTime();
            long ms2 = date2.getTime();
            if (Math.abs(ms1 - ms2) <= diffMs) {
                result = true;
            }
        }
        
        return result;
    }
    
    /**
     * 檢查兩日期的相差值, 是否在一定的誤差之內
     * 
     * @param date1 : 要檢查的日期之一
     * @param date2 : 要檢查的日期之二
     * @param diffDay : 可以接受的誤差範圍 (天)
     * @return
     */
    public static boolean equalByDiffDay(Date date1, Date date2, long diffDay) {
    	return equalByDiff(date1, date2, diffDay*86400*100);
    }

    /**
     * 檢查日期一是否比日期二早 ( 在一定的誤差之內 )
     * 
     * @param date1 : 要檢查的日期之一
     * @param date2 : 要檢查的日期之二
     * @param diffMs : 可以接受的誤差範圍 (毫秒)
     * @return
     */
    public static boolean beforeByDiff(Date date1, Date date2, long diffMs) {
        boolean result = false;
        
        if (date1 != null && date2 != null) {
            if (equalByDiff(date1, date2, diffMs)) {
                result = true;
            } else {
                long ms1 = date1.getTime();
                long ms2 = date2.getTime();
                long diff = ms1 - ms2;
                if (diff <= 0) {
                    result = true;
                } else {
                    if (diff - diffMs <= 0) {
                        result = true;
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * 檢查日期一是否比日期二晚, 是否在一定的誤差之內
     * 
     * @param date1 : 要檢查的日期之一
     * @param date2 : 要檢查的日期之二
     * @param diffMs : 可以接受的誤差範圍 (毫秒)
     * @return
     */
    public static boolean afterByDiff(Date date1, Date date2, long diffMs) {
        boolean result = false;
        
        if (date1 != null && date2 != null) {
            if (equalByDiff(date1, date2, diffMs)) {
                result = true;
            } else {
                long ms1 = date1.getTime();
                long ms2 = date2.getTime();
                long diff = ms1 - ms2;
                if (diff >= 0) {
                    result = true;
                } else {
                    if (diff + diffMs >= 0) {
                        result = true;
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Timestamp 轉 Date
     * 
     * @param timestamp
     * @return
     */
    public static Date TimestampToDate(Timestamp timestamp) {
    	Date result = null;
    	if(timestamp != null) {
    		result = new Date(timestamp.getTime());
    	}
    	return result;
    }
    
    /**
     * 判斷所給的source 是不是在start & end區間
     * 
     * @param startDate
     * @param endDate
     * @param sourceDate
     * @return
     */
    public static boolean isInterval(Date startDate, Date endDate, Date sourceDate) {
    	boolean res = false;
    	if(startDate != null && endDate != null && sourceDate != null) {
    		if(sourceDate.after(startDate) && sourceDate.before(endDate)) {
    			res = true;
    		}
    	}
    	
    	return res;
    }
    
    /**
     * 判斷sourceDate 是否在 targetDate 之後
     * 
     * @param sourceDate
     * @param targetDate
     * @return
     */
    public static boolean after(Date sourceDate , Date targetDate) {
        boolean res = false;
        if(sourceDate != null && targetDate != null) {
            res = sourceDate.after(targetDate);
        }
        return res;
    }
    
    /**
     * 判斷sourceDate 是否在 targetDate 之前
     * 
     * @param sourceDate
     * @param targetDate
     * @return
     */
    public static boolean before(Date sourceDate , Date targetDate) {
        boolean res = false;
        if(sourceDate != null && targetDate != null) {
            res = sourceDate.before(targetDate);
        }
        return res;
    }
}
