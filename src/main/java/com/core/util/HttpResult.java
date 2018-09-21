package com.core.util;

/**
 * 
 * @author Bonjour
 *
 */
public class HttpResult {

    private Integer status;
    private String result;

    /**
     * 
     * @param status : 狀態碼
     * @param result : 回傳內容
     */
    public HttpResult(Integer status, String result) {
        super();
        this.status = status;
        this.result = result;
    }

    /**
     * @return 狀態碼
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @return 回傳內容
     */
    public String getResult() {
        return result;
    }
}
