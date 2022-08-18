package com.msa.bankingsystem.core.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.msa.bankingsystem.models.Account;
import com.msa.bankingsystem.services.dtos.GetListAccountDto;

@Component
public class AccountDtoMapper {

	public GetListAccountDto accountToAccountDto(Account account) {
		return new GetListAccountDto().builder()
				.accountNumber(account.getAccountNumber())				
				.bankId(account.getBank().getId())
				.bankName(account.getBank().getName())
				.userId(account.getUser().getId())			
				.balance(account.getBalance())
				.type(account.getType()).build();
	}

	public List<GetListAccountDto> listAccountToListAccountDto(List<Account> accounts) {

		List<GetListAccountDto> dtos = new ArrayList<>();
		for (Account account : accounts) {
			dtos.add(accountToAccountDto(account));
		}
		return dtos;
	}
}
