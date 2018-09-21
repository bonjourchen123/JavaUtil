package com.core.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * @author Bonjour
 *
 */
public class UrlUtil {
	
	private static final String CHAR_SET = "UTF-8";
	
	/**
     * 
     * @param url
     * @return
     */
    public static String encode(String url) {
        return encode(url, CHAR_SET);
    }

    public static String encode(String url, String charSet) {
        String result = null;
        
        try {
            if (url != null) {
                String usingCharSet = null;
                if (charSet != null) {
                    usingCharSet = charSet;
                } else {
                    usingCharSet = CHAR_SET;
                }
                
                result = URLEncoder.encode(url, usingCharSet);
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return result;
    }
    
    public static String decode(String url) {
        return decode(url, CHAR_SET);
    }

    public static String decode(String url, String charSet) {
        String result = null;
        
        try {
            if (url != null) {
                String usingCharSet = null;
                if (charSet != null) {
                    usingCharSet = charSet;
                } else {
                    usingCharSet = CHAR_SET;
                }
                
                result = URLDecoder.decode(url, usingCharSet);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return result;
    }
    
}
