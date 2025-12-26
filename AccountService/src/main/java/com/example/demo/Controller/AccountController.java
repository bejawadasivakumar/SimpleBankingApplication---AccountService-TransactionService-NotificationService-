package com.example.demo.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.AccountDetails;
import com.example.demo.Service.AccountService;
import com.example.demo.dtos.AccountDto;
import com.example.demo.dtos.AccountResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api")
@Tag(name ="REST APIs - Banking Application")
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;
	
	//@Autowired
	//private TransactionRepository transactionRepo;
	
	
	@PostMapping("/create")
		public ResponseEntity<?> create( @RequestBody AccountDto dto) {
		try {
			AccountDetails createdAccount = accountService.createAccount(dto);
			logger.info("POST /api/create - Account created successfully with account number: {}", createdAccount.getAccountnumber());
			return ResponseEntity.ok(createdAccount);
		}
		catch(Exception e) {
			logger.error("POST /api/create - Error creating account for email: {}", dto.getEmail(), e);
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating account: " + e.getMessage());
		}
		}
	
	@GetMapping("/getDetails")
	public List<AccountDetails> find(){
		try {
			List<AccountDetails> accounts = accountService.getAll();
			logger.info("GET /api/getDetails - Retrieved {} accounts successfully" , accounts.size());
			return accounts;
		}
		catch(Exception e) {
			logger.error("GET /api/getDetails - Error fetching all accounts", e);
			throw e;
		}
	}
	 
	@GetMapping("/AccountDetails/{accountnumber}")
	public ResponseEntity<?> getDetailsByAccountNumber(@PathVariable String accountnumber) {
		try {
			
	    AccountResponseDto details = accountService.getAccountDetailsByAccountnumber(accountnumber);
	    if (details != null) {
	    	logger.info("GET /api/AccountDetails/{} - Account details found successfully", accountnumber);
	        return ResponseEntity.ok(details);
	    } 
	    else {
				logger.warn("GET /api/AccountDetails/{} - Account not found", accountnumber);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("Account details not found for account number: " + accountnumber);
	    }
	}
		catch(Exception e) {
			logger.error("GET /api/AccountDetails/{} - Error fetching account details", accountnumber, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching account details: " + e.getMessage());
		}
	}
	
	@GetMapping("/getBalance/{accountNumber}")
	public ResponseEntity<Double> getBalance(@PathVariable String accountNumber) {
		try {
	        AccountResponseDto acc = accountService.getAccountDetailsByAccountnumber(accountNumber);

	        if (acc == null) {
	            logger.warn("GET /api/getBalance/{} - Account not found", accountNumber);
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        double currentAmount = accountService.currentBalanceByAccountNumber(accountNumber);

	        logger.info("GET /api/getBalance/{} - Balance retrieved successfully: {}",accountNumber,currentAmount);

	        return new ResponseEntity<>(currentAmount, HttpStatus.OK);

	    } catch (Exception e) {
	        logger.error("GET /api/getBalance/{} - Error fetching balance", accountNumber, e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PutMapping("/updateBalance/{accountNumber}")
	public ResponseEntity<String> updateBalance(@PathVariable String accountNumber, @RequestParam double amount) {
		try {
		boolean updated = accountService.updateBalance(accountNumber, amount);
		if(updated) {
			logger.info("PUT /api/updateBalance/{} - Balance updated successfully. New Balance: {}", 
					accountNumber, accountService.getAccountDetailsByAccountnumber(accountNumber).getBalance());
			return new ResponseEntity<>("Updated Balance: " + accountService.getAccountDetailsByAccountnumber(accountNumber).getBalance(),HttpStatus.OK);
		}
		logger.warn("PUT /api/updateBalance/{} - Failed to update balance. Account may not exist", accountNumber);
		return new ResponseEntity<>("Not Updated Balance for accountNo: " + accountNumber,HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {
			logger.error("PUT /api/updateBalance/{} - Error updating balance", accountNumber, e);
			return new ResponseEntity<>("Error updating balance: " + e.getMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteAccount/{accountNumber}")
	public ResponseEntity<String> delete(@PathVariable String accountNumber){
		try {
		AccountResponseDto account = accountService.getAccountDetailsByAccountnumber(accountNumber);
		if(account != null) {
			accountService.deleteAccount(accountNumber);
			logger.info("DELETE /api/deleteAccount/{} - Account deleted successfully", accountNumber);
		return new ResponseEntity<>("Account deleted successfully!!!", HttpStatus.NO_CONTENT);
		}
		logger.warn("DELETE /api/deleteAccount/{} - Account not found for deletion", accountNumber);
		return new ResponseEntity<>("Account doesn't exist in the Database",HttpStatus.NOT_FOUND);
		}
		catch(Exception e){
			logger.error("DELETE /api/deleteAccount/{} - Error deleting account", accountNumber, e);
			return new ResponseEntity<>("Error deleting account: " + e.getMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/check/{accountNumber}")
	public ResponseEntity<Boolean> isValidAccount(@PathVariable String accountNumber) {
	    try {
	        AccountResponseDto accountDetails =
	                accountService.getAccountDetailsByAccountnumber(accountNumber);

	        if (accountDetails == null) {
	            logger.warn("GET /api/check/{} - Account not found", accountNumber);
	            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	        } else {
	            logger.info("GET /api/check/{} - Account found successfully", accountNumber);
	            return new ResponseEntity<>(true, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	        logger.error("GET /api/check/{} - Error fetching account details", accountNumber, e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
// Expected failures (business rules) → WARN logging level
//Unexpected failures (system errors) → ERROR logging level
