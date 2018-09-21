package com.core.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author Bonjour
 *
 */
public class PDFUtil {

	/**
	 * PDF檔轉BASE64字串
	 * 
	 * @param imagePath
	 * @param pdfPath
	 * @return
	 */
	public static String filePDF2Base64Encode(String imagePath, String pdfPath) {
		String base64 = "";
		try {
			image2PDF(imagePath, pdfPath);
			File file = new File(pdfPath);
	        byte bytes[] = loadFile(file);
	        BASE64Encoder encoder = new BASE64Encoder();
	        base64 = encoder.encode(bytes);
		} catch(Exception e) {
            e.printStackTrace();
        }
		return base64;
	}

	/**
	 * 圖片轉PDF
	 * 
	 * @param imagePath
	 * @param pdfPath
	 */
	private static void image2PDF(String imagePath, String pdfPath) {
		Rectangle rectPageSize = new Rectangle(PageSize.A4);
		Document document = new Document(rectPageSize, 0, 0, 0, 0);
		FileOutputStream fop = null;
		File pdfFile;
		String imageType = "png";

	    try {
	        pdfFile = new File(pdfPath);
			fop = new FileOutputStream(pdfFile);

			if (!pdfFile.exists()) {
				pdfFile.createNewFile();
			}
	    	PdfWriter.getInstance(document, fop);
	        document.open();

	        File fnew = new File(imagePath);
	        BufferedImage originalImage=ImageIO.read(fnew);
	        ByteArrayOutputStream baos=new ByteArrayOutputStream();
	        ImageIO.write(originalImage, imageType, baos );
	        byte[] imageInByte=baos.toByteArray();

	        Image image1 = Image.getInstance(imageInByte);
	        document.add(image1);

	    } catch(Exception e){
	      e.printStackTrace();
	    }  finally {
			try {
				if(document != null) {
					document.close();
				}

				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length <= 0x7fffffffL);

		byte bytes[] = new byte[(int) length];
		int offset = 0;
		for (int numRead = 0; offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0; offset += numRead) {
		}
		if (offset < bytes.length) {
			throw new IOException((new StringBuilder())
					.append("Could not completely read file ")
					.append(file.getName()).toString());
		} else {
			is.close();
			return bytes;
		}
	}
}
