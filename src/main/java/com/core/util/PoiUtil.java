package com.core.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author Bonjour
 *
 */
public class PoiUtil {	
	private static Map<Row, Integer> cellCount = new HashMap<Row, Integer>();
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final Map<String,String> TRANS_FOR_EXCEL = new HashMap<String,String>();
	private static final String TRANS_TYPE_DATE = "DATE";
	private static final String TRANS_TYPE_NUMBER = "NUMBER";
	
	static {
		TRANS_FOR_EXCEL.put("date", TRANS_TYPE_DATE);
		TRANS_FOR_EXCEL.put("Date", TRANS_TYPE_DATE);
		TRANS_FOR_EXCEL.put("int", TRANS_TYPE_NUMBER);
		TRANS_FOR_EXCEL.put("Integer", TRANS_TYPE_NUMBER);
		TRANS_FOR_EXCEL.put("long", TRANS_TYPE_NUMBER);
		TRANS_FOR_EXCEL.put("Long", TRANS_TYPE_NUMBER);
	}
	
	public static String getCellValue(Cell cell) {
		if (cell == null) return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
		    return cell.getStringCellValue();

		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();

		case Cell.CELL_TYPE_NUMERIC:
		    return sdf.format(cell.getDateCellValue());

		case Cell.CELL_TYPE_BLANK:
			return "";

		case Cell.CELL_TYPE_BOOLEAN:
			return Boolean.toString(cell.getBooleanCellValue());
		    
		default :
			return null;
		}
	}
	
	public static String getCellString(Cell cell) {
		return cell == null ? null : cell.toString();
	}
	
	public static Long getCellLong(Cell cell) throws Exception {
		if (cell == null) return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			if ("".equals(cell.getStringCellValue())) return null;
		    return Long.parseLong(cell.getStringCellValue());

		case Cell.CELL_TYPE_FORMULA:
			return null;

		case Cell.CELL_TYPE_NUMERIC:
		    return cell.getDateCellValue().getTime();

		case Cell.CELL_TYPE_BLANK:
			return null;

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? 1 : 0L;
		    
		default :
			return null;
		}
	}
	
	public static Boolean getCellBoolean(Cell cell) throws Exception {
		return "是".equals(getCellString(cell)) || "true".equals(getCellString(cell));
	}
	
	public static Boolean getCellBoolean(Cell cell, String trueString) throws Exception {
		return trueString.equals(getCellString(cell));
	}
	
	public static Date getCellDate(Cell cell) {
		try {
			return sdf.parse(getCellValue(cell));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String str(Long in) {
		return in == null ? "" : in.toString();
	}
	
	public static String str(Boolean in) {
		return in != null && in ? "是" : "否";
	}
	
	public static String str(Date in) {
		return in == null? "" : sdf.format(in);
	}

	public static void writeStringToRow(Row row, String[] strs) {
		for (String str : strs) {
			if (str != null) writeStringToCell(row, str);
		}
	}

	/**
	 * 
	 * @param row 寫入的行
	 * @param str
	 */
	public static void writeStringToCell(Row row, String str) {
		Integer cnt = cellCount.get(row);
		if(cnt == null) cnt = 0;
		row.createCell(cnt++).setCellValue(str);
		cellCount.put(row, cnt);
	}
	
	/**
	 * 取得Excel的樣板
	 * 
	 * @param templatePath
	 * @param templateName
	 * @return
	 * @throws IOException
	 */
	public static Workbook getExcelTemplate(String templatePath, String templateName) throws IOException{
		ClassPathResource r = new ClassPathResource(templatePath + templateName + ".xlsx");
        Workbook wb = new XSSFWorkbook(r.getInputStream());
		return wb;
	}
	
	/**
	 * 將一個 List 寫入 excel 中指定的位置 For List
	 * 
	 * @param listData  要寫入 excel 中的 List
	 * @param sheet  目的 sheet
	 * @param startRow  起始的 row（0 based）
	 * @param startColumn  起始的欄位（0 based）
	 */
	public static void writeLists(List<List<Object>> listData, Sheet sheet, int startRow, int startColumn) {
		if (listData == null || listData.isEmpty()) {
			return;
		}
		
		for (List<Object> data : listData) {
			writeToRow(data.toArray(new Object[1]), sheet, startRow++, startColumn);
		}
	}
	
	/**
	 * 將一個 List 寫入 excel 中指定的位置 For List
	 * 
	 * @param listData  要寫入 excel 中的 List
	 * @param sheet  目的 sheet
	 * @param startRow  起始的 row（0 based）
	 * @param startColumn  起始的欄位（0 based）
	 * @param style 資料欄的格式
	 */
	public static void writeLists(List<List<Object>> listData, Sheet sheet, int startRow, int startColumn, CellStyle style) {
		if (listData == null || listData.isEmpty()) {
			return;
		}
		
		// 預設都使用邊框
		addBorder(style);
		
		for (List<Object> data : listData) {
			writeToRow(data.toArray(new Object[1]), sheet, startRow++, startColumn, style);
		}
	}
	
	/**
	 * 將一個 List<> 寫入 excel 中指定的位置 for Map
	 * 
	 * @param listMap  要寫入 excel 中的 List
	 * @param sheet  目的 sheet
	 * @param startRow  起始的 row（0 based）
	 * @param startColumn  起始的欄位（0 based）
	 * @param needTitle  是否輸出 Map 的 key
	 * @param seq  印資料時輸出 key 值的順序
	 */
	public static void writeMaps(List<Map<String, Object>> listMap, Sheet sheet, int startRow, int startColumn, boolean needTitle, List<String> seq) {
		if (listMap == null || listMap.isEmpty()) {
			return;
		}

		if (needTitle) {
			if (seq != null && !seq.isEmpty()) {
				int colNum = startColumn;
				for (String title : seq) {
					if (listMap.get(0).containsKey(title)) {
						writeToCell(title, sheet, startRow, colNum++);
					}
				}
				startRow++;
			} else {
				writeToRow(listMap.get(0).keySet().toArray(new String[1]), sheet, startRow++, startColumn);
			}
		}
		
		for (Map<String,Object> data : listMap) {
			if (seq != null && !seq.isEmpty()) {
				int colNum = startColumn;
				for (String title : seq) {
					if (data.containsKey(title)) {
						writeToCell(data.get(title), sheet, startRow, colNum++);
					}
				}
				startRow++;
			} else {
				writeToRow(data.values().toArray(new Object[1]), sheet, startRow++, startColumn);
			}
		}
	}
	
	/**
	 * 將一個 List<> 寫入 excel 中指定的位置 for Map
	 * 
	 * @param listMap  要寫入 excel 中的 List
	 * @param sheet  目的 sheet
	 * @param startRow  起始的 row（0 based）
	 * @param startColumn  起始的欄位（0 based）
	 * @param needTitle  是否輸出 Map 的 key
	 * @param seq  印資料時輸出 key 值的順序
	 * @param style 資料欄的格式
	 */
	public static void writeMaps(List<Map<String, Object>> listMap, Sheet sheet, int startRow, int startColumn, boolean needTitle, List<String> seq, CellStyle style) {
		if (listMap == null || listMap.isEmpty()) {
			return;
		}
		
		// 預設使用邊框
		addBorder(style);
		
		if (needTitle) {
			if (seq != null && !seq.isEmpty()) {
				int colNum = startColumn;
				for (String title : seq) {
					if (listMap.get(0).containsKey(title)) {
						writeToCell(title, sheet, startRow, colNum);
						changeStyleToCell(getRow(sheet, startRow), colNum++, style);
					}
				}
				startRow++;
			} else {
				writeToRow(listMap.get(0).keySet().toArray(new String[1]), sheet, startRow++, startColumn, style);
			}
		}
		
		for (Map<String,Object> data : listMap) {
			if (seq != null && !seq.isEmpty()) {
				int colNum = startColumn;
				for (String title : seq) {
					if (data.containsKey(title)) {
						writeToCell(data.get(title), sheet, startRow, colNum);
						changeStyleToCell(getRow(sheet, startRow), colNum++, style);
					}
				}
				startRow++;
			} else {
				writeToRow(data.values().toArray(new Object[1]), sheet, startRow++, startColumn, style);
			}
		}
	}
	
	/**
	 * 將一個 List 寫入 excel 中指定的位置 For Entity
	 * 
	 * @param listEntity  要寫入 excel 中的 List
	 * @param sheet  目的 sheet
	 * @param startRow  起始的 row（0 based）
	 * @param startColumn  起始的欄位（0 based）
	 * @param needTitle  是否輸出 Map 的 key
	 * @param seq  印資料時輸出 key 值的順序
	 */
	public static <T>void writeObjs(List<T> listEntity, Sheet sheet, int startRow, int startColumn, boolean needTitle, List<String> seq) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for(Object entity : listEntity){
			Map<String, Object> mapEntity =  MapUtil.objToMap(entity);
			listMap.add(mapEntity);
		}
		writeMaps(listMap,sheet,startRow,startColumn,needTitle,seq);
	}
	
	/**
	 * 將一個 List 寫入 excel 中指定的位置 For Entity
	 * 
	 * @param listEntity  要寫入 excel 中的 List
	 * @param sheet  目的 sheet
	 * @param startRow  起始的 row（0 based）
	 * @param startColumn  起始的欄位（0 based）
	 * @param needTitle  是否輸出 Map 的 key
	 * @param seq  印資料時輸出 key 值的順序
	 * @param style 資料欄的格式
	 */
	public static <T>void writeObjs(List<T> listEntity, Sheet sheet, int startRow, int startColumn, boolean needTitle, List<String> seq, CellStyle style) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for(Object entity : listEntity){
			Map<String, Object> mapEntity =  MapUtil.objToMap(entity);
			listMap.add(mapEntity);
		}
		writeMaps(listMap,sheet,startRow,startColumn,needTitle,seq,style);
	}
	
	
	/**
	 * 將資料寫入 excel 中指定的位置 
	 *     Example. writeToCell("TEST", aSheet, "A", 1);
	 * 
	 * @param data  要寫入 excel 的資料
	 * @param sheet  目的 sheet
	 * @param columnName  目標的 columnName  Ex."B","AC"
	 * @param rowNum  目標的rowNum（1 based）
	 */
	public static void writeToCell(Object data, Sheet sheet, String columnName, int rowNum) {		
		int fInt = CellReference.convertColStringToIndex(columnName);
		writeToCell(data, sheet, rowNum - 1, fInt);
	}
	
	/**
	 * 將資料寫入 excel 中指定的位置 
	 *     Example. writeToCell("TEST", aSheet, "A1");
	 *     
	 * @param data  要寫入 excel 的資料
	 * @param sheet  目的 sheet
	 * @param positionString  目標的欄位名稱 Ex. "B11","AF7"
	 */
	public static void writeToCell(Object data, Sheet sheet, String positionString) {
		String filterF = "^[A-Za-z]*";
		String filterL = "\\d*$";
		
		String f = positionString.replaceAll(filterL, "");
		String l = positionString.replaceAll(filterF, "");
		
		if (positionString == null || !positionString.equalsIgnoreCase(f+l)) {
			throw new RuntimeException("輸入的位置字串\"" + positionString + "\"不是正確的Excel位置字串");
		}
		writeToCell(data, sheet, f, Integer.valueOf(l));
	}
	
	private static void writeToRow(Object[] strs, Sheet sheet, int startRowNum, int startCellNum) {
		Row row = getRow(sheet, startRowNum);
		for (Object str : strs) {
			if (str != null) writeToCell(row, startCellNum++, str);
		}
	}
	
	private static void writeToRow(Object[] strs, Sheet sheet, int startRowNum, int startCellNum, CellStyle style) {
		Row row = getRow(sheet, startRowNum);
		for (Object str : strs) {
			if (str != null) writeToCell(row, startCellNum, str);
			if (style != null) changeStyleToCell(row, startCellNum, style);
			startCellNum++;
		}
	}
	
	private static void writeToCell(Row row, int cellNum, Object obj) {
		Cell c = getCell(row, cellNum);
		String classTypeName = "";
		if(obj != null){
			classTypeName = obj.getClass().getSimpleName();
		}
		if (TRANS_TYPE_NUMBER.equals(TRANS_FOR_EXCEL.get(classTypeName))) {
			double value = BaseUtil.getDouble(obj);
			c.setCellValue(value);
		} else if (TRANS_TYPE_DATE.equals(TRANS_FOR_EXCEL.get(classTypeName))) {
			// 目前採用與字串相同處理方法
			String str = BaseUtil.getStr(obj);
			c.setCellValue(str);
		} else {
			//
			String str = BaseUtil.getStr(obj);
			c.setCellValue(str);	
		}
	}
	
	
	/**
	 * 變更 style
	 * @param sheet
	 * @param rowNum 目標的row（0 based）
	 * @param cellNum 目標的column（0 based）
	 * @param style
	 */
	public static void changeStyleToCell(Sheet sheet, int rowNum, int cellNum, CellStyle style) {
		changeStyleToCell(getRow(sheet, rowNum), cellNum, style);
	}
	
	private static void changeStyleToCell(Row row, int cellNum, CellStyle style) {
		Cell c = getCell(row, cellNum);
		c.setCellStyle(style);
	}
	
	/**
	 * 將資料寫入 excel 中指定的位置
	 * 
	 * @param data  要寫入 excel 的資料
	 * @param sheet  目的 sheet
	 * @param rowNum  目標的row（0 based）
	 * @param cellNum  目標的column（0 based）
	 */
	public static void writeToCell(Object data, Sheet sheet, int rowNum, int cellNum) {
		writeToCell(getRow(sheet, rowNum), cellNum, data);
	}
	
	/**
	 * 將資料寫入 excel 中指定的位置
	 * 
	 * @param data  要寫入 excel 的資料
	 * @param sheet  目的 sheet
	 * @param rowNum  目標的row（0 based）
	 * @param cellNum  目標的column（0 based）
	 * @param style  修改的樣式
	 */
	public static void writeToCell(Object data, Sheet sheet, int rowNum, int cellNum, CellStyle style) {
		writeToCell(getRow(sheet, rowNum), cellNum, data);
		addBorder(style);
		changeStyleToCell(getRow(sheet, rowNum), cellNum, style);
	}
	
	/**
	 * 將 sheet 中指定區間的 row 隱藏
	 * 
	 * @param sheet
	 * @param startRow
	 * @param hideRowCount
	 */
	public static void setRowHide(Sheet sheet, int startRow, int hideRowCount) {
		for (int i = startRow ; i < hideRowCount ; i++) {
			sheet.getRow(i).setZeroHeight(true);
		}
	}
	
	private static Cell getCell(Row row, int cellNum) {
		Cell cell = row.getCell(cellNum);
		if (cell == null) {
			cell = row.createCell(cellNum);
		}
		return cell;
	}

	private static Row getRow(Sheet sheet, int startRow) {
		Row row = sheet.getRow(startRow);
		if(row == null){
			row = sheet.createRow(startRow);
		}
		return row;
	}

	/**
	 * 增加邊框效果
	 * 
	 * @param style
	 */
	private static void addBorder(CellStyle style){
		if(style != null){
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		}
	}
}
