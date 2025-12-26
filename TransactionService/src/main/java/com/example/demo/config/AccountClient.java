package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dtos.AccountResponseDto;

@Component
public class AccountClient {
	
	private static final String ACCOUNT_URL = "http://localhost:9394/api";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Double getBalance(String accountNumber) {
		return restTemplate.getForObject(ACCOUNT_URL + "/getBalance/" + accountNumber, Double.class);// write reqired url
	}

	public void updateBalance(String accountNumber, double amount) {
		restTemplate.put(ACCOUNT_URL + "/updateBalance/" + accountNumber + "?amount=" + amount, null);
	}
	
	public Boolean isValidAccount(String accountNumber) {
        return restTemplate.getForObject(ACCOUNT_URL + "/check/" + accountNumber, Boolean.class);
    }
	
	public AccountResponseDto accountDetails(String accountNumber) {
		return restTemplate.getForObject(ACCOUNT_URL + "/AccountDetails/" + accountNumber, AccountResponseDto.class);
	}
}
