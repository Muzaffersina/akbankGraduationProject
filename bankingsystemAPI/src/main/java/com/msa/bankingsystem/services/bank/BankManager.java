package com.msa.bankingsystem.services.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.core.results.ErrorDataResult;
import com.msa.bankingsystem.core.results.SuccessDataResult;
import com.msa.bankingsystem.dataAccess.bank.IBankRepository;
import com.msa.bankingsystem.models.Bank;
import com.msa.bankingsystem.services.message.Messages;
import com.msa.bankingsystem.services.requests.CreateBankRequest;

@Service
public class BankManager implements IBankService {

	private IBankRepository iBankRepository;

	@Autowired
	public BankManager(IBankRepository iBankRepository) {
		this.iBankRepository = iBankRepository;
	}

	@Override
	public DataResult<Bank> createBank(CreateBankRequest createBankRequest) {

		if (checkBankExists(createBankRequest.getName())) {
			return new ErrorDataResult<Bank>(null, Messages.BANKALREADYEXİTS+ createBankRequest.getName());
		}

		Bank bank = Bank.builder().name(createBankRequest.getName()).build();

		this.iBankRepository.save(bank);
		return new SuccessDataResult<Bank>(bank, Messages.CREATEDBANK);

	}

	private boolean checkBankExists(String name) {

		Bank bank = this.iBankRepository.getByBankName(name);

		if (bank == null) {
			return false;
		}
		return true;
	}
}
