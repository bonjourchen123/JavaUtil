package com.core.util;

/**
 * 
 * @author Bonjour
 *
 */
public class BaseUtil {
	
	/**
	 * Convert Object to String
	 * 
	 * @param Object
	 * @return String
	 **/
	public static String getStr(Object obj) {
		String result = "";

		try {
			if (obj != null) {
				result = obj.toString().trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Convert Object to int
	 * 
	 * @param Object
	 * @return int
	 **/
	public static int getInt(Object obj) {
		int result = 0;

		try {
			String str = getStr(obj);

			if (! "".equalsIgnoreCase(str)) {
				result = Integer.valueOf(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Convert Object to long
	 * 
	 * @param Object
	 * @return long
	 **/
	public static long getLong(Object obj) {
		long result = 0;

		try {
			String str = getStr(obj);

			if (! "".equalsIgnoreCase(str)) {
				result = Long.valueOf(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static char getChar(int charNumber) {
		if (charNumber < Character.MIN_VALUE) {
			return Character.MIN_VALUE;
		} else if (charNumber > Character.MAX_VALUE) {
			return Character.MAX_VALUE;
		} else {
			return (char) charNumber;
		}
	}

	public static byte getByte(int charNumber) {
		if (charNumber < Byte.MIN_VALUE) {
			return Byte.MIN_VALUE;
		} else if (charNumber > Byte.MAX_VALUE) {
			return Byte.MAX_VALUE;
		} else {
			return (byte) charNumber;
		}
	}

	public static double getDouble(Object obj) {
		double result = 0;

		try {
			String str = getStr(obj);

			if (! "".equalsIgnoreCase(str)) {
				result = Double.valueOf(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
