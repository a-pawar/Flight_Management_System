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
import java.util.List;
import java.util.Properties;

@Component
public class EmailByAdmin {
    public void sendEmail(List<String> emailList,String subject,String message){
        try{
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
            
            // Convert the list of email addresses to an array of InternetAddress
            InternetAddress[] recipientAddresses = new InternetAddress[emailList.size()];
            for (int i = 0; i < emailList.size(); i++) {
                recipientAddresses[i] = new InternetAddress(emailList.get(i));
            }

            // Set the recipients of the email
            msg.setRecipients(Message.RecipientType.TO, recipientAddresses);
            
            msg.setSubject(subject);
            msg.setSentDate(new Date());


            MimeBodyPart messageBody = new MimeBodyPart();
            

            messageBody.setContent(message,"text/html");


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody);
            
            

            msg.setContent(multipart);
            Transport.send(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
