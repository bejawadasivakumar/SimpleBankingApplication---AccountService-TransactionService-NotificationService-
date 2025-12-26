package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_notifications")
public class EmailNotification {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String accountNumber;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String fullName;
	
	@Column(nullable = false)
	private String phoneNo;
	
	@Column(columnDefinition = "TEXT")
	private String message;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType type;
	
	private LocalDateTime sentAt;

	public EmailNotification() {
		
	}

	public EmailNotification(String accountNumber, String email, String fullName, String phoneNo, String message,
			NotificationStatus status, NotificationType type, LocalDateTime sentAt) {
		
		this.accountNumber = accountNumber;
		this.email = email;
		this.fullName = fullName;
		this.phoneNo = phoneNo;
		this.message = message;
		this.status = status;
		this.type = type;
		this.sentAt = sentAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
}

