package com.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * 
 * @author Bonjour
 *
 */
public class FtpUtil {
	
	public static void uploadFtp(String host, int port, String username, String password,
            String path, String filename, File file) {
		FTPClient ftp = new FTPClient();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ftp.connect(host, port);
			ftp.login(username, password);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, fis);
			ftp.logout();
		} catch(Exception e) {
            e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(ftp.isConnected()) {
				try{
					ftp.disconnect();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
}
