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
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailOTPHelper {
	
    public boolean sendEmail(String email, String otp){
    	
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
            msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
            msg.setSubject("OTP Authentication");
            msg.setSentDate(new Date());


            MimeBodyPart messageBody = new MimeBodyPart();
            

            messageBody.setContent("Your OTP for email verification is: " + otp,"text/html");


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody);
            
//            MimeBodyPart attach = new MimeBodyPart();
//            attach.attachFile("/Users/suyash9698/downloads/pop.png");
//            multipart.addBodyPart(attach);
            

            msg.setContent(multipart);
            Transport.send(msg);
            
            return true; // Email sent successfully


        } catch (Exception e) {
            e.printStackTrace();
            return false; // Email sending failed
        }
        
        
        
    }
    
    
       private static final int OTP_LENGTH = 6;
       private static final String OTP_CHARACTERS = "0123456789";

        public static String generateOTP() {
            SecureRandom random = new SecureRandom();
            StringBuilder otp = new StringBuilder(OTP_LENGTH);
            for (int i = 0; i < OTP_LENGTH; i++) {
                otp.append(OTP_CHARACTERS.charAt(random.nextInt(OTP_CHARACTERS.length())));
            }
            return otp.toString();
        }
    
    
    
    
}
