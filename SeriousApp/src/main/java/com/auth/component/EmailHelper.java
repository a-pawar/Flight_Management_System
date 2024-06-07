package com.auth.component;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailHelper {
    public void sendEmail(String email,String userId){
        try{
        	String filename = "boarding_pass_" + userId + ".pdf";
        	String filePath = "/Users/suyash9698/downloads/" + filename;
        	System.out.println(filePath);
            Properties props = new Properties();
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.host","smtp.gmail.com");
            props.put("mail.smtp.port","587");

            Session session = Session.getInstance(props,new Authenticator()
            {
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication("coder.pawar@gmail.com","security key");
                }
            });

            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress("coder.pawar@gmail.com",false));
            msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
            msg.setSubject("Boarding Pass");
            msg.setSentDate(new Date());


            MimeBodyPart messageBody = new MimeBodyPart();
            
            String htmlContent = getHtmlContent("src/main/resources/templates/boarding_pass.html");

            messageBody.setContent("Thankyou For Flying With Us. Your Flight Ticket With Us Is Ready!!!","text/html");


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody);
            
            MimeBodyPart attach = new MimeBodyPart();
            attach.attachFile(filePath);
            multipart.addBodyPart(attach);
            

            msg.setContent(multipart);
            Transport.send(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private String getHtmlContent(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
