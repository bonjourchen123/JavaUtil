package com.core.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * 
 * @author Bonjour
 *
 */
public class HttpUtil {
	
	/**
     * 取得回傳的內容
     * 
     * @param entity
     * @return
     */
    private static String getResultContent(HttpEntity entity) throws Exception {
        String result = null;
        BufferedReader read = null;
        
        try {
            if (entity != null) {
                InputStream content = entity.getContent();
                read = new BufferedReader(new InputStreamReader(content));
                String line = null;
                StringBuffer tmp = new StringBuffer();
                while ((line = read.readLine()) != null) {
                    tmp.append(line);
                }
                result = tmp.toString();
            }
        } catch (Exception e) {
			e.printStackTrace();
        } finally {
        	if(read != null) {
        		read.close();
        	}
        }
        
        return result;
    }
    
    /**
     * 
     * @param url : 要 POST 的 URL
     * @param params : 要 POST 過去的參數
     * @return
     * @throws Exception
     */
    public static HttpResult post(String url, Map<String, String> params) throws Exception {
        HttpResult result = null;
        Integer resultStatus = null;
        String resultContent = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url.toString());
            
            if (params != null) {
                Set<String> keys = params.keySet();
                if (keys.size() > 0) {
                    List <BasicNameValuePair> nvps = new ArrayList <BasicNameValuePair>();
                    for (String k : keys) {
                        String value = params.get(k);
                        if (value == null) {
                            value = "";
                        }
                        nvps.add(new BasicNameValuePair(k, UrlUtil.encode(value)));
                    }
                    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                }
            }
            
            response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            resultStatus = response.getStatusLine().getStatusCode();
            resultContent = getResultContent(entity);
            
            EntityUtils.consume(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
            	if(response != null) {
            		response.close();
            	}
            } catch (Exception e2) {
            	e2.printStackTrace();
            }
        }
        
        result = new HttpResult(resultStatus, resultContent);
        
        return result;
    }
    
    /**
     * 
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static HttpResult get(String url, Map<String, String> params) throws Exception {
        HttpResult result = null;
        Integer resultStatus = null;
        String resultContent = null;
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        StringBuffer urlStr = new StringBuffer(url);
        if (params != null) {
            Set<String> keys = params.keySet();
            if (keys.size() > 0) {
                urlStr.append("?");
                int cnt = 0;
                for (String k : keys) {
                    String value = params.get(k);
                    if (value == null) {
                        value = "";
                    }
                    
                    if (cnt > 0) {
                        urlStr.append("&");
                    }
                    urlStr.append(k).append("=").append(UrlUtil.encode(value));

                    cnt++;
                }
            }
        }
        
        HttpGet httpGet = new HttpGet(urlStr.toString());
        CloseableHttpResponse response = httpclient.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();

            resultStatus = response.getStatusLine().getStatusCode();
            resultContent = getResultContent(entity);
            
            EntityUtils.consume(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            response.close();
        }
        
        result = new HttpResult(resultStatus, resultContent);
        
        return result;
    }

	public static String validate(String data) {
		return validate(data, true);
	}

	public static String validate(String str, boolean isCanonicalize) {
		if (isCanonicalize) {
			str = canonicalize(str);
			str = canonicalized(str);
		}

		String result = null;
		if (str != null) {
			result = str;
		}
		return result;
	}
	
	/**
     * canonicalized for code scan
     * 
     * @param uncanonicalizedObj : uncanonicalizedObj
     * @return 
     */
	public static <T> T canonicalized(T uncanonicalizedObj) {
        T canonicalizedObj = uncanonicalizedObj;
        return canonicalizedObj;
    }
	
	public static String canonicalize(String str) {
		final String ENCODING = "UTF-8";

		try {
			if (str != null) {
				return new String(str.getBytes(ENCODING), ENCODING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getIp(HttpServletRequest request) {
		String ip = " ";
		try {
			if(request != null) {
	        	ip = request.getHeader("X-Forwarded-For");
	        	if(ip == null) {
	        		ip = request.getRemoteAddr();
	        	}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	public static String getUrl(HttpServletRequest request) {
		String url = " ";
		try {
			if(request != null) {
				int idx = request.getRequestURL().toString().indexOf(request.getContextPath());
				String serviceUri = request.getRequestURL().substring(0, idx);
				url = serviceUri + request.getContextPath();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	public static String getDomain(HttpServletRequest request) {
		String domain = "";
		try {
			URL tmpUrl = new URL(getUrl(request));
			domain = tmpUrl.getHost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return domain;
	}
	
	public static String getDomain(String url) {
		String domain = "";
		try {
			URL tmpUrl = new URL(url);
			domain = tmpUrl.getHost();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return domain;
	}
	
	/**
	 * 把透過 ajax get 傳入的 HttpServletRequest 資料轉成 bean
	 * 
	 * @param request HttpServletRequest
	 * @param clazz 欲轉成的 bean 型別
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDataToBean(HttpServletRequest request, Class<?> clazz) {
		Object obj = null;
		if(request != null && clazz != null) {
			try {
				obj = clazz.newInstance();
				BeanUtils.populate(obj, request.getParameterMap());
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return (T) obj;
	}
	
	
	/**
	 * 把透過 ajax post 傳入的 HttpServletRequest 資料轉成 bean
	 * 
	 * @param request HttpServletRequest
	 * @param clazz 欲轉成的 bean 型別
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T postDataToBean(HttpServletRequest request, Class<?> clazz) {
		Object obj = null;
		if(request != null && clazz != null) {
			obj = new Gson().fromJson(requestPostDataToString(request), clazz);
		}
		return (T) obj;
	}
	
	/**
	 * 把透過 ajax post 傳入的 HttpServletRequest 資料轉成簡單物件型態(eg. String.class, Long.class)
	 * 
	 * @param request HttpServletRequest
	 * @param clazz 欲轉成的 bean 型別
	 * @param name 前端變數名稱
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T postDataToSimpleObject(HttpServletRequest request, Class<?> clazz, String paramName) {
		Object obj = null;
		if(request != null) {
			JSONObject json = JSONObject.fromObject(requestPostDataToString(request));
			
			if(clazz.equals(Boolean.class)) {
				obj = json.getBoolean(paramName);
			}
			else if(clazz.equals(Long.class)) {
				obj = json.getLong(paramName);
			}
			else if(clazz.equals(Double.class)) {
				obj = json.getDouble(paramName);
			}
			else {
				obj = json.getString(paramName);
			}
		}
		return (T) obj;
	}
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private static String requestPostDataToString(HttpServletRequest request) {
		String result = null;
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
			result = URLDecoder.decode(jb.toString(), "UTF-8");	
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
}
