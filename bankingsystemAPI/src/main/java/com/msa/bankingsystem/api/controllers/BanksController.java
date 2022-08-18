package com.msa.bankingsystem.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msa.bankingsystem.core.mapper.BankDtoMapper;
import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.models.Bank;
import com.msa.bankingsystem.services.bank.IBankService;
import com.msa.bankingsystem.services.dtos.GetListBankDto;
import com.msa.bankingsystem.services.requests.CreateBankRequest;

@CrossOrigin(origins = "http://localhost:4200" )
@RestController
@RequestMapping(path = "/api")
public class BanksController {

	private IBankService iBankService;
	private BankDtoMapper bankDtoMapper;

	@Autowired
	public BanksController(IBankService iBankService, BankDtoMapper bankDtoMapper) {
		this.iBankService = iBankService;
		this.bankDtoMapper = bankDtoMapper;
	}

	@PostMapping(path = "/bank")
	private ResponseEntity<DataResult<GetListBankDto>> createBank(
			@RequestBody @Valid CreateBankRequest createBankRequest) {
		DataResult<GetListBankDto> bankDto;
		DataResult<Bank> bank = this.iBankService.createBank(createBankRequest);

		if (!bank.isSuccess()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((DataResult) bank);
		}
		bankDto = new DataResult<GetListBankDto>(this.bankDtoMapper.bankToBankDto(bank.getData()), true,
				bank.getMessage());

		return ResponseEntity.created(null).body(bankDto);
	}
	
}
