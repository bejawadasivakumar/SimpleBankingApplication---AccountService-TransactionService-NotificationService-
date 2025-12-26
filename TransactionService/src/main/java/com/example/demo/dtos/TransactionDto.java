package com.example.demo.dtos;

public class TransactionDto {
	
	private String accountnumber;
	private String type;// type means deposit or withdraw
	private double amount;
	
	public TransactionDto() {
	
	}

	public TransactionDto(String accountnumber, String type, double amount) {
		super();
		this.accountnumber = accountnumber;
		this.type = type;
		this.amount = amount;
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

	

}
