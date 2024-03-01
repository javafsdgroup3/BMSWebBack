package com.tcs.bms.service;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

	   private final JavaMailSender mailSender;

	    @Autowired
	    public EmailServiceImpl(JavaMailSender mailSender) {
	        this.mailSender = mailSender;
	    }

	    @Override
	    @Async
	    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
	        CompletableFuture<Void> future = new CompletableFuture<>();

	        try {
	        	
	        	// SMTP server configuration
	            Properties props = new Properties();
	            props.put("mail.smtp.host", "smtp.gmail.com");
	            props.put("mail.smtp.port", "587");
	            props.put("mail.smtp.auth", "true");
	            props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

	            // Email account credentials
//	            String username = "bms.fsdteam@gmail.com";
//	            String password = "jjhgnaosavhbxnzw";
	            String username = "javafsdgroup3@gmail.com";
	            String password = "lxlizvlbabzxaowb";
	            // Create session with authentication
	            Session session = Session.getInstance(props, new Authenticator() {
	                @Override
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });

	            // Create and send email message
	            MimeMessage message = new MimeMessage(session);
	            
	            message.setFrom(new InternetAddress(username));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	            message.setSubject(subject);
	            message.setContent(text, "text/html"); // Specify content type as HTML
	            //message.setText(text);

	            Transport.send(message); // Send the email
	            
	            
				/*
				 * MimeMessage message = mailSender.createMimeMessage(); MimeMessageHelper
				 * helper = new MimeMessageHelper(message, true); helper.setTo(to); // No need
				 * to set the "from" address; it is automatically set by Spring Boot based on
				 * your properties helper.setSubject(subject); helper.setText(text, true); //
				 * Set the second parameter to true to send HTML content
				 * mailSender.send(message);
				 */

	            future.complete(null); // Indicate that the email sending is successful
	        } catch (MessagingException e) {
	            e.printStackTrace();
	            future.completeExceptionally(e); // Indicate that the email sending failed
	        }

	        return future;
	    }
	    
	    
	    public String getOtpLoginEmailTemplate(String name, String accountNumber, String otp) {
	        
	        String emailTemplate = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
	                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
	                + "<div style=\"border-bottom:1px solid #eee\">"
	                + "<a href=\"http://localhost:4200/\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\"> BMS - Banking Management System ( TCS FSD | GROUP-3 ) </a>"
	                + "</div>"
	                + "<p style=\"font-size:1.1em\">Hi, " + name + "</p>"
	                + "<p style=\"font-size:0.9em;\">Account Number: " + accountNumber + "</p>"
	                + "<p>Thank you for choosing BMS Banking. Use the following OTP to complete your Log In procedures. OTP is valid for 5 minutes</p>"
	                + "<h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + otp + "</h2>"
	                + "<p style=\"font-size:0.9em;\">Regards,<br />TCS - BMS | FSD GROUP-3 </p>"
	                + "<hr style=\"border:none;border-top:1px solid #eee\" />"
	                + "<p>BMS Banking..!</p>"
	                + "<p>TCS - Chennai</p>"
	                + "<p>Contact us - <a href=\"mailto:Javafsdgroup3.com?subject=Attention BMS Team - Request from Customer &body= Team , Please help on the OTP validation issue (or) other concerns. \">Feel Free to Mail us..!</a>\n"
	                + " </p>"
	                + "</div>"
	                + "</div>";

	        return emailTemplate;
	    }

	    
	    public void sendEmailWithAttachment(String to, String subject, String text, String attachmentFilePath) {
	        try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setTo(to);
	            helper.setSubject(subject);
	            helper.setText(text, true); // Set the second parameter to true to send HTML content

	            // Add an attachment to the email
	            File attachmentFile = new File(attachmentFilePath);
	            helper.addAttachment(attachmentFile.getName(), attachmentFile);

	            mailSender.send(message);
	        } catch (MessagingException  e) {
	            e.printStackTrace();
	        }
	    }

}
