package com.core.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 
 * @author Bonjour
 *
 */
public class SendMailUtil {
	
	/**
     * 
     * @param host      發信伺服器
     * @param receiver  收信者
     * @param sender    寄信者
     * @param subject   主旨
     * @param mess      內文
     * @param userName  帳號
     * @param password  密碼
     */
    public static int sendMail(String receiverAddress, String receiverName, String subject, String mess, String filePath,Boolean isDeubg){
        
        boolean sessionDebug = false;
        int errorCode = 2;
        
        String host = null;
        String userName = null;
        String password = null;
        String senderAddress = null;
        String senderName = null;

        // Get system properties
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host",host); //指定SMTP server
        prop.put("mail.transport.protocol","smtp"); //設定傳送協定
        prop.put("mail.smtp.auth","true"); //設定是否須smtp驗證

        // 產生新的Session
        javax.mail.Session mailsess = Session.getDefaultInstance(prop);
        mailsess.setDebug(sessionDebug); //是否在控制台顯示debug訊息

        MimeMessage msg = new MimeMessage(mailsess);
        
        // 設定郵件
        try{
            msg.setFrom(new InternetAddress(senderAddress, senderName, "Big5")); // 設定傳送郵件的發信人
            InternetAddress[] address= {new InternetAddress(receiverAddress, receiverName, "Big5")}; // 設定傳送郵件的收件者
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject,"Big5"); //設定主旨
            
            // 建立多內容 Multpart 郵件物件
            Multipart multipart = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();

            // 建立html格式內容
            mbp.setContent(mess, "text/html;charset=big5");
            // 將html格式內容加入 Multpart 郵件物件
            multipart.addBodyPart(mbp);         
            
            if (filePath != null) {
                // 建立附件
                mbp = new MimeBodyPart();
                DataSource fds = new FileDataSource(filePath);
                mbp.setDataHandler(new DataHandler(fds));
                mbp.setFileName(fds.getName());

                // 將附加檔案內容加入 Multpart 郵件物件
                multipart.addBodyPart(mbp);
            }
            
            // 對 Multpart 郵件物件設定訊息
            msg.setContent(multipart);

            //發送郵件
            Transport transport = mailsess.getTransport("smtp"); //只支持IMAP、 SMTP和 POP3
            transport.connect(host, userName, password); //以smtp的方式登入mail server
            transport.sendMessage(msg,msg.getAllRecipients());
            transport.close();
        }catch(AddressException e){
        	e.printStackTrace();
        }catch(MessagingException e){
        	e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return errorCode;
    }
}
