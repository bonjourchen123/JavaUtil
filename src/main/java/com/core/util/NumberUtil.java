package com.core.util;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 
 * @author Bonjour
 *
 */
public class NumberUtil {
	
    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
    	    "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
    	    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
    	    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
    	    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
	
	/**
	 * 判斷兩個Long 是不是一樣
	 * 
	 * @param l1
	 * @param l2
	 * @return
	 */
    public static boolean isEquals(Long l1, Long l2) {
    	boolean result = false;
    	if(l1 != null && l2 != null){
    		if(l1.longValue() == l2.longValue()){
    			result = true;
    		}
    	}
    	if(l1 == null && l2 == null){
			result = true;
    	}
    	return result;
    }
	
	/**
     * 判斷是否為整數
     * 
     * @param s
     * @param radix
     * @return
     */
    public static boolean isInteger(String s, int radix) {
    	boolean result = false;
        Scanner sc = null;
		try {
			sc = new Scanner(s.trim());
			if (! sc.hasNextInt(radix))
			    return false;
			sc.nextInt(radix);
			result = ! sc.hasNext();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			sc.close();
		}
        return result;
    }
    
    /**
     * 轉換整數為布林，只要是不等於零都視為true
     * 
     * @param i
     * @return
     */
    public static boolean naturalNumberToBoolean(int i) {
    	return i != 0;
    }
    public static boolean naturalNumberToBoolean(Integer i) {    	
    	return i == null ? false : i != 0;
    }
    
    /**
     * 轉換長整數為布林，只要是不等於零都視為true
     * 
     * @param l
     * @return
     */
    public static boolean naturalNumberToBoolean(long l) {
    	return l != 0;
    }
    public static boolean naturalNumberToBoolean(Long l) {
    	return l == null ? false : l != 0;
    }
    
    /**
     * 轉換雙精浮點數為布林，只要是不等於零都視為true
     * 
     * @param d
     * @return
     */
    public static boolean naturalNumberToBoolean(double d) {
    	return d != 0;
    }
    
    public static boolean naturalNumberToBoolean(Double d) {
    	return d == null ? false : d != 0;
    }
    
    /**
     * 轉換單精浮點數為布林，只要是不等於零都視為true
     * 
     * @param f
     * @return
     */
    public static boolean naturalNumberToBoolean(float f) {
    	return f != 0;
    }
    
    public static boolean naturalNumberToBoolean(Float f) {
    	return f == null ? false : f != 0;
    }
    
    /**
     * 轉換布林為長整數
     * 
     * @param b
     * @return
     */
    public static long booleanToLong(boolean b) {
    	return b ? 1L : 0L;
    }
    
    /**
     * 轉換布林為整數
     * 
     * @param b
     * @return
     */
    public static int booleanToInteger(boolean b) {
    	return b ? 1 : 0;
    }
    
    /**
     * 把小於零的數轉換成零
     */
    public static int toPositiveNumber(int i) {
    	return i < 0 ? 0 : i;
    }
    
    /**
     * 把小於零的數轉換成零
     */
    public static long toPositiveNumber(long l) {
    	return l < 0 ? 0 : l;
    }
    
    /**
     * 把小於零的數轉換成零
     */
    public static float toPositiveNumber(float f) {
    	return f < 0 ? 0 : f;
    }
    
    /**
     * 把小於零的數轉換成零
     */
    public static double toPositiveNumber(double d) {
    	return d < 0 ? 0 : d;
    }
    
	public static boolean isFloat(String s) {
	    return DOUBLE_PATTERN.matcher(s).matches();
	}
}
