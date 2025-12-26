package com.example.demo.dtos;

import java.time.LocalDateTime;

public class TransactionResponseDto {
	
	private String accountnumber;
	private String type;// type means deposit or withdraw
	private double amount;
	private LocalDateTime timestamp;
	
	public TransactionResponseDto() {
		
	}

	public TransactionResponseDto(String accountnumber, String type, double amount, LocalDateTime timestamp) {
		super();
		this.accountnumber = accountnumber;
		this.type = type;
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
