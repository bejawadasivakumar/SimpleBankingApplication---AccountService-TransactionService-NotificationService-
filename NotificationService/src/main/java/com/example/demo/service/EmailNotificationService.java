package com.example.demo.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountEmailNotificationEvent;
import com.example.demo.dtos.TransactionEmailNotificationEvent;
import com.example.demo.model.EmailNotification;
import com.example.demo.model.NotificationStatus;
import com.example.demo.model.NotificationType;
import com.example.demo.repository.EmailNotificationRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService {
	
	@Autowired
	private EmailNotificationRepository emailNotificationRepository;
	
	@Autowired
	private JavaMailSender mailSender;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);
	
	public void sendAccountCreatedMail(AccountEmailNotificationEvent event) {
		
		String message = buildAccountCreatedEmailBody(event);
		
		EmailNotification emailNotification = new EmailNotification();
		emailNotification.setAccountNumber(event.getAccountNumber());
		emailNotification.setEmail(event.getEmail());;
		emailNotification.setFullName(event.getFullName());
		emailNotification.setPhoneNo(event.getPhone());
		emailNotification.setType(NotificationType.ACCOUNT_CREATED);
		emailNotification.setStatus(NotificationStatus.PENDING);
		emailNotification.setMessage(message);
		try {
			emailNotification = emailNotificationRepository.save(emailNotification);
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(emailNotification.getEmail());
            helper.setSubject("Welcome to Shiva's Banking System!");
            helper.setText(emailNotification.getMessage(), true);
            
            mailSender.send(mimeMessage);
            
            
            emailNotification.setStatus(NotificationStatus.SUCCESS);
            emailNotification.setSentAt(LocalDateTime.now());
            logger.info("Email sent successfully for account creation to: {}", emailNotification.getEmail());
		}
		catch(Exception e) {
			emailNotification.setStatus(NotificationStatus.FAILED);
			emailNotification.setSentAt(LocalDateTime.now());
			logger.error("Failed to send email to: {}", emailNotification.getEmail(), e);
		}
		finally {
			emailNotificationRepository.save(emailNotification);
		}	
	}
	
	public void sendTransactionCompletedMal(TransactionEmailNotificationEvent event) {
		
		String message = buildTransactionEmailBody(event);
		
	    EmailNotification emailNotification = new EmailNotification();
	    emailNotification.setAccountNumber(event.getAccountNumber());;
	    emailNotification.setEmail(event.getEmail());
	    emailNotification.setFullName(event.getFullName());
	    emailNotification.setPhoneNo(event.getPhoneNo());
	    emailNotification.setType(getNotificationType(event.getTransactionType()));
	    emailNotification.setStatus(NotificationStatus.PENDING);
	    emailNotification.setMessage(message);
	    try {
	    	emailNotification = emailNotificationRepository.save(emailNotification);
	    	MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(emailNotification.getEmail());
            helper.setSubject(event.getTransactionType() + ":Transaction Successful");
            helper.setText(emailNotification.getMessage(), true);
            
            mailSender.send(mimeMessage);
            emailNotification.setStatus(NotificationStatus.SUCCESS);
            emailNotification.setSentAt(LocalDateTime.now());
            logger.info("Email sent successfully for transaction type: {}", emailNotification.getType());
	    }
	    catch(Exception e) {
	    	emailNotification.setStatus(NotificationStatus.FAILED);
	    	emailNotification.setSentAt(LocalDateTime.now());
	    	logger.error("Failed to send email for transaction to: {}", emailNotification.getEmail(), e);
	    }
	    finally {
	    	emailNotificationRepository.save(emailNotification);
	    }
	}
	
	public String buildAccountCreatedEmailBody(AccountEmailNotificationEvent event)
	{
		return String.format("""
	            <html>
	            <body style="font-family: Arial, sans-serif;">
	                <h2 style="color: #2c3e50;">Welcome to Our Banking System!</h2>
	                <p>Dear %s,</p>
	                <p>Your account has been successfully created. Here are your account details:</p>
	                <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px;">
	                    <p><strong>Account Number:</strong> %s</p>
	                    <p><strong>Phone No:</strong> %s</p>
	                    <p><strong>Balance:</strong> %s</p>
	                    <p><strong>Created On:</strong> %s</p>
	                </div>
	                <p>Thank you for choosing our banking services!</p>
	                <p style="color: #7f8c8d; font-size: 12px;">
	                    This is an automated message. Please do not reply to this email.
	                </p>
	            </body>
	            </html>
	            """,
	            event.getFullName(),
	            event.getAccountNumber(),
	            event.getPhone(),
	            event.getBalance(),
	            LocalDateTime.now()
	        );
	}
	
	public  String buildTransactionEmailBody(TransactionEmailNotificationEvent event) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2 style="color: #2c3e50;">Transaction Alert</h2>
                <p>Dear Customer,</p>
                <p>A transaction has been processed on your account:</p>
                <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px;">
                    <p><strong>Account Number:</strong> %s</p>
                    <p><strong>Transaction Type:</strong> %s</p>
                    <p><strong>Amount:</strong> ₹%s</p>
                    <p><strong>Transaction Date:</strong> %s</p>
                    <p><strong>Description:</strong> %s</p>
                    <p><strong>Current Balance:</strong> %s</p>
                </div>
                <p>If you did not authorize this transaction, please contact us immediately.</p>
                <p style="color: #7f8c8d; font-size: 12px;">
                    This is an automated message. Please do not reply to this email.
                </p>
            </body>
            </html>
            """,
            event.getAccountNumber(),
            event.getTransactionType(),
            event.getAmount(),
            event.getTimeStamp(),
            event.getDescription() != null ? event.getDescription() : "N/A",
            event.getTransactionType().equals("DEPOSIT")? event.getCurrentBalance() + event.getAmount() : event.getCurrentBalance() - event.getAmount()
        ); 
	}
	
	
	private NotificationType getNotificationType(String transactionType) {
        return switch (transactionType.toUpperCase()) {
            case "DEPOSIT" -> NotificationType.TRANSACTION_DEPOSIT;
            case "WITHDRAW" -> NotificationType.TRANSACTION_WITHDRAW;
            default -> NotificationType.TRANSACTION_DEPOSIT;
        };
    }
	

}
