package com.core.util;

/**
 * 
 * @author Bonjour
 *
 */
public class XlsColumn {

	private String header;
	
	private String mapping;
	
	
	public XlsColumn() {}

	/**
     * 用於描述 excel 對應物件
     * 
	 * @param header excel 欄位 title 名稱
	 * @param mapping Collection 欄位 mapping 至 header
	 */
	public XlsColumn(String header, String mapping) {
		this.header = header;
		this.mapping = mapping;
	}


	public String getHeader() {
		return header;
	}


	public void setHeader(String header) {
		this.header = header;
	}


	public String getMapping() {
		return mapping;
	}


	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
}
