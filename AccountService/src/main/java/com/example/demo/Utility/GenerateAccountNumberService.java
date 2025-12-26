package com.example.demo.Utility;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.Repository.AccountRepository;
@Component
public class GenerateAccountNumberService {
	
	@Autowired
	private AccountRepository accountRepository;
	//Generating the AccountNumber
		public String generateUniqueAccountNo() {
			String accountNo;
			do {
				accountNo = String.format("%011d", new Random().nextLong() % 1_000_000_00000L);
				if (accountNo.startsWith("-")) {
					accountNo = accountNo.substring(1);
				}
			} while (accountRepository.existsByAccountnumber(accountNo));
			return accountNo;
		}

}
