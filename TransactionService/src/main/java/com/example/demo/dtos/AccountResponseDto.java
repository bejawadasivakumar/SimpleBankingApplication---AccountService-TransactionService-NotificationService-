package com.example.demo.dtos;

public class AccountResponseDto {
	
	private String accountnumber;
	private String fullname;
	private String email;
	private String phone;
	private double balance;
	
	public AccountResponseDto() {
	
	}

	public AccountResponseDto(String accountnumber, String fullname, String email, String phone, double balance) {
		super();
		this.accountnumber = accountnumber;
		this.fullname = fullname;
		this.email = email;
		this.phone = phone;
		this.balance = balance;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
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
