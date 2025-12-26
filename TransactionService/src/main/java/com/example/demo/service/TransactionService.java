package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.AccountClient;
import com.example.demo.dtos.AccountResponseDto;
import com.example.demo.dtos.TransactionDto;
import com.example.demo.dtos.TransactionEmailNotificationEvent;
import com.example.demo.dtos.TransactionResponseDto;
import com.example.demo.exceptionHandling.InsufficientBalanceException;
import com.example.demo.exceptionHandling.InvalidAccountException;
import com.example.demo.exceptionHandling.TransactionProcessingException;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.TransactionDetails;
import com.example.demo.producer.TransactionEmailNotificationProducer;
import com.example.demo.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
	
	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private TransactionEmailNotificationProducer emailNotificationProducer;
	
	@Autowired
	private TransactionMapper transactionMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
	
		
		//mini-statement
		public List<TransactionResponseDto> getTransactions(String accountNumber){
			
			List<TransactionDetails> transactionDetailsByAccount = transactionRepository.findByAccountnumberOrderByTimestampDesc(accountNumber);
			List<TransactionResponseDto> responseDto = transactionDetailsByAccount.stream()
	                                                   .map(transactionMapper::toResponseDto)
	                                                   .collect(Collectors.toList());
			logger.info("Mini-statement fetched successfully | accountNumber={}, transactionCount={}",
		            accountNumber, transactionDetailsByAccount.size());
			
			return responseDto;
		}
		
		// Transaction (deposit / withdraw)
		@Transactional
		public boolean saveTransaction(String accountNumber, double amount, String type) {

		    double oldBalance = 0;

		    try {
		        boolean validAccount = accountClient.isValidAccount(accountNumber);
		        if (!validAccount) {
		        	logger.error("Transaction failed: Invalid account | accountNumber={}", accountNumber);
		            throw new InvalidAccountException("Invalid account number: " + accountNumber);
		        }

		        oldBalance = accountClient.getBalance(accountNumber);
		        AccountResponseDto responseDto = accountClient.accountDetails(accountNumber);

		        if (type.equalsIgnoreCase("deposit")) {

		            double newBalance = oldBalance + amount;
		            accountClient.updateBalance(accountNumber, newBalance);

		            TransactionDto dto = new TransactionDto(accountNumber, type, amount);
		            TransactionDetails savedTransaction = transactionRepository.save(transactionMapper.toEntity(dto));
		            logger.info("Transaction recorded in DB | accountNumber={}, type=deposit, amount={}", accountNumber,amount);
		            
		            //event
		            String accountNo = savedTransaction.getAccountnumber();
		            TransactionEmailNotificationEvent event = new TransactionEmailNotificationEvent();
		            event.setAccountNumber("XXXXXXX" + accountNo.substring(accountNo.length() - 4));
		            event.setAmount(savedTransaction.getAmount());
		            event.setEmail(responseDto.getEmail());
		            event.setPhoneNo(responseDto.getPhone());
		            event.setFullName(responseDto.getFullname());
		            event.setTransactionType(savedTransaction.getType().toUpperCase());
		            event.setTimeStamp(savedTransaction.getTimestamp());
		            event.setDescription("Deposit to account");
		            event.setCurrentBalance(responseDto.getBalance());
		            emailNotificationProducer.sendTransactionEmail(event);
		            logger.info("Published event to the producer for deposit to account");
		            
		            return true;
		        }

		        if (type.equalsIgnoreCase("withdraw")) {

		            if (oldBalance < amount) {
		            	logger.error("Transaction failed: Insufficient balance | accountNumber={}, requestedAmount={}, currentBalance={}",accountNumber, amount, oldBalance);
		                throw new InsufficientBalanceException("Insufficient balance for accountNumber: " + accountNumber);
		            }

		            double newBalance = oldBalance - amount;
		            accountClient.updateBalance(accountNumber, newBalance);

		            TransactionDto dto = new TransactionDto(accountNumber, type, amount);
		            TransactionDetails savedTransaction = transactionRepository.save(transactionMapper.toEntity(dto));
		            logger.info("Transaction recorded in DB | accountNumber={}, type=withdraw, amount={}", accountNumber,amount);
		            
		            //event
		            String accountNo = savedTransaction.getAccountnumber();
		            TransactionEmailNotificationEvent event = new TransactionEmailNotificationEvent();
		            event.setAccountNumber("XXXXXXX" + accountNo.substring(accountNo.length() - 4));
		            event.setAmount(savedTransaction.getAmount());
		            event.setEmail(responseDto.getEmail());
		            event.setPhoneNo(responseDto.getPhone());
		            event.setFullName(responseDto.getFullname());
		            event.setTransactionType(savedTransaction.getType().toUpperCase());
		            event.setTimeStamp(savedTransaction.getTimestamp());
		            event.setDescription("Withdrawal from account");
		            event.setCurrentBalance(responseDto.getBalance());
		            emailNotificationProducer.sendTransactionEmail(event);
		            logger.info("Published the event to the producer for withdrawal from account");
		            
		            return true;
		        }

		        logger.error("Transaction failed: Invalid type | accountNumber={}, type={}", accountNumber, type);
		        throw new TransactionProcessingException("Invalid transaction type: " + type, null);

		    } catch (RuntimeException e) {

		        logger.error("Transaction failed, compensating balance | accountNumber={}", accountNumber, e);

		        // 🔁 Compensation
		        try {
		            accountClient.updateBalance(accountNumber, oldBalance);
		        } catch (Exception ex) {
		            logger.error("Balance compensation failed | accountNumber={}", accountNumber, ex);
		        }
		    throw e;
		    }
		   
		}
		
		public double getBalance(String accountNumber) {
			return accountClient.getBalance(accountNumber);
		}
}
/*
Problem Scenario (Without Compensation)

Your transaction flow is:

Get current balance from Account Service

Update balance in Account Service

Save transaction in Transaction Service DB

Now imagine this sequence:

✔ Balance updated in Account Service
❌ Transaction save failed (DB down / exception)


👉 Result:

Account balance is already changed

Transaction record is missing

System is inconsistent ❌

This is very dangerous in banking systems.

✅ What Compensation Does

Compensation = manual rollback for distributed systems

You are doing this:

Save old balance (oldBalance)

If anything fails:

Restore the balance back to old value

accountClient.updateBalance(accountNumber, oldBalance);
 */

