package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.TransactionResponseDto;
import com.example.demo.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;

	@GetMapping("/mini-statement/{accountNumber}")
	public ResponseEntity<?> getTransactions(@PathVariable String accountNumber) {
		List<TransactionResponseDto> transactions = transactionService.getTransactions(accountNumber);
	    
	    if (transactions.isEmpty()) {
	        return new ResponseEntity<>("Transaction History not found with the given AccountNumber", HttpStatus.NOT_FOUND);
	    }
	    return ResponseEntity.ok(transactions);
	    /*
	       Why the transaction list is returned in descending order:
	       transactionRepo.findByAccountnumberOrderByTimestampDesc(accountNumber);
           This method name follows Spring Data JPA's method naming convention, and it tells Spring to:
           Fetch all transactions for a given account number, ordered by the timeStamp field in 
           descending order (i.e., most recent transactions first).
	     */
	}
	@PostMapping("/depositOrWithdraw")
	public ResponseEntity<?> depositAndWithdraw(@RequestParam String accountNumber,@RequestParam double amount, @RequestParam String type){
		boolean success = transactionService.saveTransaction(accountNumber, amount, type);
		double balance = transactionService.getBalance(accountNumber);
		if(success) {
			        if (type.equalsIgnoreCase("deposit")) {//.equalsIgnoreCase used to handle case-insensitive matching of the transaction type.
			            return new ResponseEntity<>("Deposit: Transaction Successful !!! \nCurrent Balance: " + balance, HttpStatus.OK);
			        }
			        else if (type.equalsIgnoreCase("withdraw")) {
			            return new ResponseEntity<>("Withdraw: Transaction Successful !!! \nCurrent Balance: " + balance, HttpStatus.OK);
			        }
			    }

			    return new ResponseEntity<>("Transaction failed: insufficient balance or invalid type", HttpStatus.BAD_REQUEST);
	}
}
