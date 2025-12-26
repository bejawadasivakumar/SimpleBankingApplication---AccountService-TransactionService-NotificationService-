package com.example.demo.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Model.AccountDetails;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Utility.GenerateAccountNumberService;
import com.example.demo.dtos.AccountDto;
import com.example.demo.dtos.AccountEmailNotificationEvent;
import com.example.demo.dtos.AccountResponseDto;
import com.example.demo.producer.AccountEmailNotificationProducer;

@Service
public class AccountService {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private GenerateAccountNumberService generateAccountNumberService;
	
	@Autowired
	private AccountEmailNotificationProducer accountEmailNotificationProducer;
	
	
	//Creating the Account
	public AccountDetails createAccount(AccountDto dto) {
		try {
		AccountDetails account = new AccountDetails();
		account.setFullname(dto.getFullname());
		account.setEmail(dto.getEmail());
		account.setPhone(dto.getPhone());
		account.setAccountnumber(generateAccountNumberService.generateUniqueAccountNo());
		account.setBalance(0.0);
		AccountDetails savedAccount = accountRepository.save(account);
		logger.info("Account created successfully with account number: {}", savedAccount.getAccountnumber());
		
		//Event
		AccountEmailNotificationEvent event = new AccountEmailNotificationEvent();
		event.setAccountNumber(savedAccount.getAccountnumber());
		event.setFullName(savedAccount.getFullname());
		event.setEmail(savedAccount.getEmail());
		event.setPhone(savedAccount.getPhone());
		event.setBalance(savedAccount.getBalance());
		accountEmailNotificationProducer.sendAccountEmail(event);
		
		logger.info("Calling the serivce to Publish the account creation for email {}", savedAccount.getEmail());
		
		return savedAccount;
	}
	catch(Exception e) {
		logger.error("Error creating account", e);
		throw e;
	}
	}

	//Fetching the details
	public List<AccountDetails> getAll(){
		try {
			List<AccountDetails> accounts = accountRepository.findAll();
			logger.info("Retrieved {} accounts from database", accounts.size());
		return accounts;
		}
		catch(Exception e) {
			logger.error("Error fetching all accounts", e);
			throw e;
		}
	}
	
	// Finding the AccountDetails by accountNumber
	public AccountResponseDto getAccountDetailsByAccountnumber(String accountNumber) {
		try {
		AccountDetails repoDetails = accountRepository.findByAccountnumber(accountNumber);
		AccountResponseDto responseDto = new AccountResponseDto();
		responseDto.setAccountnumber(repoDetails.getAccountnumber());
		responseDto.setEmail(repoDetails.getEmail());
		responseDto.setFullname(repoDetails.getFullname());
		responseDto.setBalance(repoDetails.getBalance());
		responseDto.setPhone(repoDetails.getPhone());
		if(repoDetails != null) {
			logger.info("Account found for account number: {}", accountNumber);
		return responseDto;
		}
		logger.warn("No account found for account number: {}", accountNumber);
		return null;
	    }
		catch(Exception e) {
			logger.error("Error fetching account details for account number: {}", accountNumber,e);
			throw e;
		}
		
	}
	
	//find the current Balance by Account number
	public double currentBalanceByAccountNumber(String accountNumber) {
		try {
		AccountDetails account = accountRepository.findByAccountnumber(accountNumber);
		if(account != null) {
	    logger.info("Current balance for account {}: {}", accountNumber, account.getBalance());
		return account.getBalance();
		}
		else {
			logger.warn("Account not found when fetching balance: {}", accountNumber);
			return 0.0;
		}
		}
		catch(Exception e) {
			logger.error("Error fetching balance for account: {}", accountNumber, e);
			throw e;
		}
		
	}
	
	//updating the balance of specific accountNumber
	public boolean updateBalance(String accountNumber, double amount)
	{
		try {
			AccountDetails account = accountRepository.findByAccountnumber(accountNumber);
			if(account != null) {
				account.setBalance(amount);
				accountRepository.save(account);
				logger.info("Balance updated successfully");
				return true;
			}
			else {
				logger.warn("Account not found when fetching balance");
				return false;
			}
		}
		catch(Exception e) {
			logger.error("Error fetching balance for account: {}", accountNumber);
			throw e;
		}
	}
	
	// delete the Account details in the DB through accountNumber
	public void deleteAccount(String accountNumber) {
		try {
		AccountDetails accountDetails = accountRepository.findByAccountnumber(accountNumber);
		if(accountDetails != null) {
		accountRepository.delete(accountDetails);
		logger.info("Account deleted successfully: {}", accountNumber);
		}
		else {
			logger.warn("Account not found for deletion: {}", accountNumber);
		}
		}
		catch(Exception e) {
			logger.error("Error deleting account: {}", accountNumber, e);
			throw e;
		}
	}
}
