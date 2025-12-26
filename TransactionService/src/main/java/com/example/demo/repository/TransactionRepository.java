package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TransactionDetails;


public interface TransactionRepository extends JpaRepository<TransactionDetails,Long>{

	 List<TransactionDetails> findByAccountnumberOrderByTimestampDesc(String accountNumber);
}
