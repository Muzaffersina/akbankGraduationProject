package com.msa.bankingsystem.services.bank;

import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.models.Bank;
import com.msa.bankingsystem.services.requests.CreateBankRequest;

public interface IBankService {
	
	DataResult<Bank> createBank(CreateBankRequest createBankRequest);
}
