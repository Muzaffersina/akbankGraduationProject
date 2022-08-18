package com.msa.bankingsystem.core.mapper;

import org.springframework.stereotype.Component;

import com.msa.bankingsystem.models.Bank;
import com.msa.bankingsystem.services.dtos.GetListBankDto;

@Component
public class BankDtoMapper {

	public GetListBankDto bankToBankDto(Bank bank) {
		return new GetListBankDto().builder().name(bank.getName()).build();
	}
}
