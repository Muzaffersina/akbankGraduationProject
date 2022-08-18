package com.msa.bankingsystem.dataAccess.bank;

import org.apache.ibatis.annotations.Mapper;

import com.msa.bankingsystem.models.Bank;
@Mapper
public interface IBankRepository {

	void save(Bank bank);
	
	Bank getByBankName(String name);

}
