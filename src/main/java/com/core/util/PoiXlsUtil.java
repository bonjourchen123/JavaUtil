package com.core.util;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author Bonjour
 *
 */
public class PoiXlsUtil {
	
	/**
     * 使用 Collection<LinkedHashMap<String, Object>> 生成 excel 至 OutputStream <br>
     * 預設使用 Map 的 key 當作 header
     *                            
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 */
	public static void toXls(OutputStream os, Collection<LinkedHashMap<String, Object>> dataCollection) {
		toXls(os, dataCollection, "", 0, 0, 0);
	}
	
	
	/**
     * 使用 Collection<LinkedHashMap<String, Object>> 生成 excel 至 OutputStream <br>
     * 並自訂資料開始位置向上偏移量, 向左偏移量及 header 名稱
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param topShift 資料開始位置向上偏移量
	 * @param leftShift 資料開始位置向左偏移量
	 * @param headers header 名稱, 必須與 Map 中 Key 數量一致 
	 */
	public static void toXls(OutputStream os, Collection<LinkedHashMap<String, Object>> dataCollection, int topShift, int leftShift, String ... headers) {
		toXls(os, dataCollection, "", 0, topShift, leftShift, headers);
	}
	
	
	/**
     * 使用 Collection<LinkedHashMap<String, Object>> 生成 excel 至 OutputStream <br>
     * 並自訂資料開始位置向上偏移量, 向左偏移量及需要的欄位 key mapping 與 header 名稱
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param topShift 資料開始位置向上偏移量
	 * @param leftShift 資料開始位置向左偏移量
	 * @param xlsCols 設定 key mapping 與 header 名稱
	 */
	public static void toXls(OutputStream os, Collection<Map<String, Object>> dataCollection, int topShift, int leftShift, XlsColumn ... xlsCols) {
		toXls(os, dataCollection, null, "", 0, topShift, leftShift, xlsCols);
	}
	
	
	/**
     * 使用 Collection<LinkedHashMap<String, Object>> 生成 excel 至 OutputStream <br>
     * 並使用 excel 樣板及自訂資料開始 sheet 的 index, 位置向上偏移量, 向左偏移量
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param templatePath excel 樣板 Resource 位置
	 * @param sheetIndex 資料存放 sheet 的 index
	 * @param topShift 資料開始位置向上偏移量
	 * @param leftShift 資料開始位置向左偏移量
	 */
	public static void toXls(OutputStream os, Collection<LinkedHashMap<String, Object>> dataCollection, String templatePath, int sheetIndex, int topShift, int leftShift) {
		toXls(os, dataCollection, templatePath, sheetIndex, topShift, leftShift, new String[0]);
	}
	
	
	/**
     * 使用 Collection<LinkedHashMap<String, Object>> 生成 excel 至 OutputStream 的底層 method
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param templatePath excel 樣板 Resource 位置
	 * @param sheetIndex 資料存放 sheet 的 index
	 * @param topShift 資料開始位置向上偏移量
	 * @param leftShift 資料開始位置向左偏移量
	 * @param headers header 名稱, 必須與 Map 中 Key 數量一致 
	 */
	private static void toXls(OutputStream os, Collection<LinkedHashMap<String, Object>> dataCollection, String templatePath, int sheetIndex, int topShift, int leftShift, String ... headers) {
		if(dataCollection != null && dataCollection.size() > 0) {
			for(LinkedHashMap<String, Object> data : dataCollection) {
				int mapKeyCount = 0;
				XlsColumn[] xlsCols = new XlsColumn[data.keySet().size()];
				if((headers != null && headers.length == 0) || (headers != null && headers.length == data.keySet().size())) {
					for(String key : data.keySet()) {
						xlsCols[mapKeyCount] = new XlsColumn((headers.length != 0) ? headers[mapKeyCount] : key, key);
						mapKeyCount++;
					}
					toXls(os, dataCollection, null, templatePath, sheetIndex, topShift, leftShift, xlsCols);
				}
				break;
			}		
		}
	}

	
	/**
     * 使用 Collection<T> 生成 excel 至 OutputStream <br>
     * 並自訂需要的欄位 data mapping 與 header 名稱
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param clazz 樣板 T 的 Class 型態
	 * @param xlsCols 設定 data mapping 與 header 名稱
	 */
	public static <T> void  toXls(OutputStream os, Collection<T> dataCollection, Class<T> clazz, XlsColumn ... xlsCols) {
		toXls(os, dataCollection, clazz, 0, 0, xlsCols);
	}
	
	
	/**
     * 使用 Collection<T> 生成 excel 至 OutputStream <br>
     * 並自訂資料開始位置向上偏移量, 向左偏移量及需要的欄位 data mapping 與 header 名稱
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param clazz 樣板 T 的 Class 型態
	 * @param topShift 資料開始位置向上偏移量
	 * @param leftShift 資料開始位置向左偏移量
	 * @param xlsCols 設定 data mapping 與 header 名稱
	 */
	public static <T> void  toXls(OutputStream os, Collection<T> dataCollection, Class<T> clazz, int topShift, int leftShift, XlsColumn ... xlsCols) {
		toXls(os, dataCollection, clazz, null, 0, topShift, leftShift, xlsCols);
	}
	
	
	/**
     * 使用 Collection<T> 生成 excel 至 OutputStream <br>
     * 並使用 excel 樣板及自訂資料開始 sheet 的 index, 位置向上偏移量, 向左偏移量, 設定 data mapping
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param clazz 樣板 T 的 Class 型態
	 * @param templatePath excel 樣板 Resource 位置
	 * @param sheetIndex 資料存放 sheet 的 index
	 * @param topShift 資料開始位置向上偏移量
	 * @param leftShift 資料開始位置向左偏移量
	 * @param dataMapping 設定 Object 資料 data mapping
	 */
	public static <T> void  toXls(OutputStream os, Collection<T> dataCollection, Class<T> clazz, String templatePath, int sheetIndex, int topShift, int leftShift, String ... dataMapping) {
		if(dataMapping != null && dataMapping.length != 0) {
			XlsColumn[] xlsCols = new XlsColumn[dataMapping.length];
			for(int i = 0, tmpSize = dataMapping.length; i < tmpSize; i++) {
				xlsCols[i] = new XlsColumn(null, dataMapping[i]);
			}
			toXls(os, dataCollection, clazz, templatePath, sheetIndex, topShift, leftShift, xlsCols);
		}
	}
	
	
	/**
     * 使用 Collection<T> 生成 excel 至 OutputStream <br>
     * 並使用 excel 樣板及自訂資料開始 sheet 的 index, 位置向上偏移量, 向左偏移量及需要的欄位 data mapping 與 header 名稱
     * 
	 * @param os 生成 excel 目標 OutputStream
	 * @param dataCollection 生成 excel 的資料 Collection
	 * @param clazz 樣板 T 的 Class 型態
	 * @param templatePath excel 樣板 Resource 位置
	 * @param sheetIndex 資料存放 sheet 的 index
	 * @param topShift 資料開始位置向上偏移量
	 * @param leftShift 資料開始位置向左偏移量
	 * @param xlsCols 設定 data mapping 與 header 名稱
	 */
	private static <T> void  toXls(OutputStream os, Collection<T> dataCollection, Class<T> clazz, String templatePath, int sheetIndex, int topShift, int leftShift, XlsColumn ... xlsCols) {
		if(dataCollection != null && xlsCols != null && xlsCols.length != 0) {
			try {
				boolean hasTemplate = (templatePath != null && !"".equals(templatePath.trim()));
				Workbook wb = (hasTemplate) ? new XSSFWorkbook(new ClassPathResource(templatePath).getInputStream()) : new XSSFWorkbook();
				Sheet sheet = (hasTemplate) ? wb.getSheetAt(sheetIndex) : wb.createSheet();
				int originRowCount = 0 + topShift;
				int rowCount = originRowCount;
				
				if(!hasTemplate) { // 生成 header
					Row row = sheet.createRow(rowCount);
					int cellCount = 0 + leftShift;
					for(XlsColumn xlsCol : xlsCols) {
						Cell cell = row.createCell(cellCount);
						cell.setCellValue(String.valueOf(xlsCol.getHeader()));
						cellCount++;
					}
					rowCount++;
				}
				
				for(Object datas : dataCollection) {
					
					Row row = (hasTemplate) ?  sheet.getRow(rowCount) : sheet.createRow(rowCount);
					row = (row != null) ? row : sheet.createRow(rowCount);
					int cellCount = 0 + leftShift;
					
					for(XlsColumn xlsCol : xlsCols) {
						
						Object cellValue = null; 
						
						if(datas instanceof Map) {
							cellValue = ((Map<?, ?>) datas).get(xlsCol.getMapping());
						}
						else {
							Field field = datas.getClass().getDeclaredField(xlsCol.getMapping());
							if(field != null) {
								field.setAccessible(true);
								cellValue = field.get(datas);
							}
						}
						Cell cell = (hasTemplate) ? row.getCell(cellCount) : row.createCell(cellCount);
						cell = (cell != null) ? cell : row.createCell(cellCount);
						cell.setCellValue(String.valueOf(cellValue));
						cellCount++;
					}	
					rowCount++;
				}
				
				wb.write(os);
				os.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
