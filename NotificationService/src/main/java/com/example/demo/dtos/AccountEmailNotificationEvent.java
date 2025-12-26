package com.example.demo.dtos;

public class AccountEmailNotificationEvent {
	
	private String accountNumber;
	private String fullName;
	private String email;
	private String phone;
	private double balance;
	
	public AccountEmailNotificationEvent() {
		
	}

	public AccountEmailNotificationEvent(String accountNumber, String fullName, String email, String phone,
			double balance) {
		super();
		this.accountNumber = accountNumber;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
