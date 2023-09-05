package ind.sq.study.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;

public class Mail {
    public static void main(String[] args) throws MessagingException, FileNotFoundException {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "smtp.mxhichina.com");
        properties.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties);
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("ac-notice@maycur.com"));
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("sunqin@maycur.com"));
        msg.setSubject("测试邮件", "UTF-8");
        msg.setText("测试内容");
        msg.saveChanges();
        var transport = session.getTransport();
        transport.connect("ac-notice@maycur.com", "Maycur@2022");
        transport.sendMessage(msg, msg.getAllRecipients());

    }
}
