package com.example.demo.mapper;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Configuration;

import com.example.demo.dtos.TransactionDto;
import com.example.demo.dtos.TransactionResponseDto;
import com.example.demo.model.TransactionDetails;

@Configuration
public class TransactionMapper {
	
	public TransactionDetails toEntity(TransactionDto dto) {
		
		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setAccountnumber(dto.getAccountnumber());
		transactionDetails.setAmount(dto.getAmount());
		transactionDetails.setType(dto.getType());
		transactionDetails.setTimestamp(LocalDateTime.now());
		return transactionDetails;
	}
	public TransactionDto toDto(TransactionDetails transactionDetails) {
		TransactionDto dto = new TransactionDto();
		dto.setAccountnumber(transactionDetails.getAccountnumber());
		dto.setAmount(transactionDetails.getAmount());
		dto.setType(transactionDetails.getType());
		return dto;
	}
	
public TransactionResponseDto toResponseDto(TransactionDetails transaction) {
		
		TransactionResponseDto responseDto = new TransactionResponseDto();
		responseDto.setAccountnumber(transaction.getAccountnumber());
		responseDto.setAmount(transaction.getAmount());
		responseDto.setType(transaction.getType());
		responseDto.setTimestamp(transaction.getTimestamp());
		
		return responseDto;
}

}
