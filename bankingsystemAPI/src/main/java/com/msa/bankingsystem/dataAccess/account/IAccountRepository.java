package com.msa.bankingsystem.dataAccess.account;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.msa.bankingsystem.models.Account;

@Mapper
public interface IAccountRepository {

	void save(Account account);

	boolean updateDeletedColumn(String accountNumber ,LocalDateTime lastUpdateDate);

	boolean updateBalance(String accountNumber, double amount,LocalDateTime lastUpdateDate);

	Account getByAccountNumber(String accountNumber);
	
	Account getByAccountNumberWithUserAndBank(String  accountNumber);
	
	List<Account> getByUserIdNumberWithUserAndBank(int userId);
	
	Account getByAccounId(int id);
	
	List<Account> getByUserId(String userId);

	List<Account> getAll();

	
}
