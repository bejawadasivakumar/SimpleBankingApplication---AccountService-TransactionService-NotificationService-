package com.example.demo.dtos;

import java.time.LocalDateTime;

public class TransactionEmailNotificationEvent {
	
	private String accountNumber;
	private String email;
	private String phoneNo;
	private String fullName;
	private String transactionType;// type means deposit or withdraw
	private double amount;
	private LocalDateTime timeStamp;
	private String description;
	private double currentBalance;
	
	public TransactionEmailNotificationEvent() {
		
	}
	
	public TransactionEmailNotificationEvent(String accountNumber, String email, String phoneNo, String fullName,
			String transactionType, double amount, LocalDateTime timeStamp, String description, double currentBalance) {
		super();
		this.accountNumber = accountNumber;
		this.email = email;
		this.phoneNo = phoneNo;
		this.fullName = fullName;
		this.transactionType = transactionType;
		this.amount = amount;
		this.timeStamp = timeStamp;
		this.description = description;
		this.currentBalance = currentBalance;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
}
