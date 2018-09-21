package com.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Bonjour
 *
 */
public class CookieUtil {
	
	HttpServletRequest request;
	HttpServletResponse response;
	String domain;
	String path;
	
	public CookieUtil(HttpServletRequest request, HttpServletResponse response) {
		this(request, response, null, null);
	}
	
	public CookieUtil(HttpServletRequest request, HttpServletResponse response, String path) {
		this(request, response, null, path);
	}
	
	public CookieUtil(HttpServletRequest request, HttpServletResponse response, String domain, String path) {
		this.request = request;
		this.response = response;
		if(domain != null) 
			this.domain = domain;
		if(path != null)
			this.path = path;
	}
	
	public String getCookie(String cookieName) {
		return this.getCookie(cookieName, null, null);
	}
	
	public String getCookie(String cookieName, String path) {
		return this.getCookie(cookieName, null, path);
	}
	
	public String getCookie(String cookieName, String domain, String path) {
		Cookie[] cookies = this.request.getCookies();
		String result = null;
		if(cookies  != null && cookieName != null) {
			for(Cookie cookie : cookies) {
				if(cookieName.equals(HttpUtil.validate(cookie.getName()) )
						&& (domain == null || domain.equals(HttpUtil.validate(cookie.getDomain())))
						&& (path == null || path.equals(HttpUtil.validate(cookie.getPath())))) {
					result = HttpUtil.validate(cookie.getValue());
					break;
				}
			}
		}
		return result;
	}
	
	public void setCookie(String cookieName, String cookieValue){
		this.setCookie(cookieName, cookieValue, this.domain, this.path);
	}
	
	public void setCookie(String cookieName, String cookieValue, Integer expiry){
		this.setCookie(cookieName, cookieValue, this.domain, this.path, expiry);
	}
	
	public void setCookie(String cookieName, String cookieValue, String domain, String path){
		this.setCookie(cookieName, cookieValue, domain, path, null);
	}
	
	public void setCookie(String cookieName, String cookieValue, String domain, String path, Integer expiry){
		Cookie cookie = new Cookie(cookieName, cookieValue);
		if(domain != null) 
			cookie.setDomain(domain);
		if(path != null) 
			cookie.setPath(path);
		if(expiry != null) 
			cookie.setMaxAge(expiry);
		cookie.setSecure(false);
		this.response.addCookie(cookie);
	}
	
}
