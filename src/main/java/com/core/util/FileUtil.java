package com.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 
 * @author Bonjour
 *
 */
public class FileUtil {
	
	/**
     * 檔案上傳的大小限制 (MB)
     */
    private static final long CONFIG_uploadSizeInMB = 2;
    
    /**
     * 是否可以上傳沒有副檔名的檔案
     */
    private static final boolean CONFIG_allowNoExt = true;
    
    /**
     * 不可上傳的副檔名
     */
    private static List<String> CONFIG_deniedExt;
    
    static {
        CONFIG_deniedExt = new ArrayList<String>();
        CONFIG_deniedExt.add("exe");
        CONFIG_deniedExt.add("cmd");
        CONFIG_deniedExt.add("bat");
    }
    
    private FileUtil() {
        
    }

    /**
     * 上傳檔案檢核
     * 
     * @param file : 要檢核的檔案
     * @param filename : 檔名
     * @param throwException : 是否丟出 Exception
     * @return
     */
    public static String validateFileWhenUpload(File file, String filename, boolean throwException) throws Exception {
        String result = null;
        
        if (file == null) {
            result = "請選擇檔案";
            if (throwException) {
                throw new Exception(result);
            }
        } else {
            if (filename == null) {
                filename = file.getName();
            }

            // 檢查副檔名
            String[] strs = StringUtil.splitFilenameAndExt(filename);
            if (strs[1] == null) {
                if (!CONFIG_allowNoExt) {
                    result = "請勿上傳沒有副檔名的檔案 : " + filename;
                    if (throwException) {
                        throw new Exception(result);
                    }
                }
            } else {
                if (CONFIG_deniedExt.contains(strs[1].toLowerCase())) {
                    result = "請勿上傳此檔案類型 : " + filename;
                    if (throwException) {
                        throw new Exception(result);
                    }
                }
            }

            // 檢查檔案大小
            byte[] content = IOUtils.toByteArray(new FileInputStream(file));
            if (content == null || content.length == 0) {
                // 檔案內容是空的
                
                result = "請勿上傳空的檔案 : " + filename;
                if (throwException) {
                    throw new Exception(result);
                }
            }
            
            long size = file.length();
            // 換算成 MB
            double sizeMB = size/1024.0/1024.0;
            if (sizeMB > CONFIG_uploadSizeInMB) {
                // 超過上傳檔案大小
                
                result = "請勿上傳超過 " + CONFIG_uploadSizeInMB + " MB 的檔案 :" + filename;
                if (throwException) {
                    throw new Exception(result);
                }
            }
        }
        
        return result;
    }
    
    /**
	 * Write file.
	 *
	 * @param file the file
	 * @param data the data
	 * @param encoding the encoding
	 * @throws Exception the exception
	 */
	public static void writeFile(File file, String data, String encoding) {
		try {
			FileUtils.writeStringToFile(file, data, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
