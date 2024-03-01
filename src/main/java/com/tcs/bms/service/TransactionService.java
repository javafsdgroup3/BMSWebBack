package com.tcs.bms.service;

import java.util.List;

import com.tcs.bms.dto.TransactionDTO;

public interface TransactionService {

	List<TransactionDTO> getAllTransactionsByAccountNumber(String accountNumber);

}
