package com.msa.bankingsystem.services.account;

import java.util.List;

import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.models.Account;
import com.msa.bankingsystem.services.requests.CreateAccountRequest;
import com.msa.bankingsystem.services.requests.CreateDepositRequest;
import com.msa.bankingsystem.services.requests.CreateTransferRequest;
import com.msa.bankingsystem.services.requests.CreateWithDrawRequest;

public interface IAccountService {

	DataResult<Account> create(CreateAccountRequest createAccountRequest);
	
	DataResult<Account> delete(String accountNumber);

	DataResult<Account> getByAccountNumber(String accountNumber);	
	
	DataResult<List<Account>> getByUserId(int userId);	

	DataResult<Account> deposit(String accountNumber, CreateDepositRequest createDepositRequest);
	
	DataResult<Account> withDraw(String accountNumber, CreateWithDrawRequest createWithDrawRequest);

	DataResult<Account> transferBetweenAccounts(String senderAccountNumber, CreateTransferRequest createTransferRequest);

	DataResult<List<Account>> getAll();
}
