package com.msa.bankingsystem.api.controllers;

import java.time.ZoneId;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msa.bankingsystem.core.mapper.AccountDtoMapper;
import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.core.results.Result;
import com.msa.bankingsystem.models.Account;
import com.msa.bankingsystem.services.account.IAccountService;
import com.msa.bankingsystem.services.dtos.GetListAccountDto;
import com.msa.bankingsystem.services.message.Messages;
import com.msa.bankingsystem.services.requests.CreateAccountRequest;
import com.msa.bankingsystem.services.requests.CreateDepositRequest;
import com.msa.bankingsystem.services.requests.CreateTransferRequest;
import com.msa.bankingsystem.services.requests.CreateWithDrawRequest;
import com.msa.bankingsystem.services.webSecurityConfig.MyCustomUser;

@CrossOrigin(origins = "http://localhost:4200" )
@RestController
@RequestMapping(path = "/api")
public class AccountsController {

	private IAccountService iAccountService;
	private AccountDtoMapper accountDtoMapper;
	@Autowired
	public AccountsController(IAccountService iAccountService, AccountDtoMapper accountDtoMapper) {
		this.iAccountService = iAccountService;
		this.accountDtoMapper = accountDtoMapper;
	
	}
	


	@PostMapping(path = "/account")
	private ResponseEntity<DataResult<GetListAccountDto>> createAccount(
			@RequestBody @Valid CreateAccountRequest createAccountRequest) {

		DataResult<Account> account = this.iAccountService.create(createAccountRequest);

		if (!account.isSuccess()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((DataResult) account);

		}
		DataResult<GetListAccountDto> accountDto = new DataResult<GetListAccountDto>(
				this.accountDtoMapper.accountToAccountDto(account.getData()), account.isSuccess(),
				account.getMessage());
		return ResponseEntity.created(null).body(accountDto);
	}

	@GetMapping(path = "/account/{accountNumber}")
	private ResponseEntity<DataResult<GetListAccountDto>> getAccountByAccountId(
			@PathVariable(name = "accountNumber") String accountNumber) {

		DataResult<GetListAccountDto> accountDto;

		if (checkAuthUser(accountNumber)) {

			accountDto = new DataResult<GetListAccountDto>(null, false, Messages.ACCESSDENIED);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accountDto);
		}
		// Used to write lastModified header
		DataResult<Account> account = this.iAccountService.getByAccountNumber(accountNumber);

		if (!account.isSuccess() || account == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((DataResult) account);
		}

		accountDto = new DataResult<GetListAccountDto>(this.accountDtoMapper.accountToAccountDto(account.getData()),
				account.isSuccess(), account.getMessage());

		return ResponseEntity.ok().lastModified(account.getData().getLastUpdateDate().atZone(ZoneId.of("UTC")))
				.body(accountDto);

	}
	
	@GetMapping(path = "/accounts/{id}")
	private ResponseEntity<DataResult<List<GetListAccountDto>>> getAccountByUserId(
			@PathVariable(name = "id") int id) {

		DataResult<List<GetListAccountDto>> accountDto;				
		
		if(checkAuthUserById(id)){
			accountDto = new DataResult<List<GetListAccountDto>>(null, false, Messages.ACCESSDENIED);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accountDto);
		}
		
		// Used to write lastModified header
		DataResult<List<Account>> accounts = this.iAccountService.getByUserId(id);	
	
		
		if (!accounts.isSuccess() || accounts.getData().isEmpty() ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((DataResult)accounts);
		}

		accountDto = new DataResult<List<GetListAccountDto>>(this.accountDtoMapper.listAccountToListAccountDto(accounts.getData()),
				accounts.isSuccess(), accounts.getMessage());

		return ResponseEntity.ok().lastModified(accounts.getData().get(0).getLastUpdateDate().atZone(ZoneId.of("UTC")))
				.body(accountDto);

	}

	@PatchMapping(path = "/account/{accountNumber}")
	private ResponseEntity<DataResult<GetListAccountDto>> deposit(
			@PathVariable(name = "accountNumber") String accountNumber,
			@RequestBody @Valid CreateDepositRequest createDepositRequest) {

		DataResult<GetListAccountDto> accountDto;

		if (checkAuthUser(accountNumber)) {

			accountDto = new DataResult<GetListAccountDto>(null, false, Messages.ACCESSDENIED);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accountDto);
		}

		// Used to write lastModified header
		DataResult<Account> account = this.iAccountService.deposit(accountNumber, createDepositRequest);

		if (!account.isSuccess() || account == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((DataResult) account);
		}

		accountDto = new DataResult<GetListAccountDto>(this.accountDtoMapper.accountToAccountDto(account.getData()),
				account.isSuccess(), account.getMessage());

		return ResponseEntity.ok().lastModified(account.getData().getLastUpdateDate().atZone(ZoneId.of("UTC")))
				.body(accountDto);

	}
	
	@PatchMapping(path = "/account/withDraw/{accountNumber}")
	private ResponseEntity<DataResult<GetListAccountDto>> withDraw(
			@PathVariable(name = "accountNumber") String accountNumber,
			@RequestBody @Valid CreateWithDrawRequest createWithDrawRequest) {

		DataResult<GetListAccountDto> accountDto;

		if (checkAuthUser(accountNumber)) {

			accountDto = new DataResult<GetListAccountDto>(null, false, Messages.ACCESSDENIED);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accountDto);
		}
		
		// Used to write lastModified header
		DataResult<Account> account = this.iAccountService.withDraw(accountNumber, createWithDrawRequest);

		if (!account.isSuccess() || account == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((DataResult) account);
		}

		accountDto = new DataResult<GetListAccountDto>(this.accountDtoMapper.accountToAccountDto(account.getData()),
				account.isSuccess(), account.getMessage());

		return ResponseEntity.ok().lastModified(account.getData().getLastUpdateDate().atZone(ZoneId.of("UTC")))
				.body(accountDto);

	}

	@PutMapping(path = "/account/transfer/{senderAccountNumber}")
	private ResponseEntity<DataResult<GetListAccountDto>> transferBetweenAccounts(
			@PathVariable(name = "senderAccountNumber") String senderAccountNumber,
			@RequestBody @Valid CreateTransferRequest createTransferRequest) {

		DataResult<GetListAccountDto> dataResult;

		if (checkAuthUser(senderAccountNumber)) {
			dataResult = new DataResult<GetListAccountDto>(null, false, Messages.ACCESSDENIED);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dataResult);
		}

		// Used to write lastModified header
		DataResult<Account> account = this.iAccountService.transferBetweenAccounts(senderAccountNumber,
				createTransferRequest);

		if (!account.isSuccess() || account == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((DataResult) account);
		}
		dataResult = new DataResult<GetListAccountDto>(this.accountDtoMapper.accountToAccountDto(account.getData()),
				account.isSuccess(), account.getMessage());

		return ResponseEntity.ok().lastModified(account.getData().getLastUpdateDate().atZone(ZoneId.of("UTC")))
				.body(dataResult);

	}

	@DeleteMapping("/account/{accountNumber}")
	public ResponseEntity<Result> delete(@PathVariable String accountNumber) {

		Result result;	

	
		// Used to write lastModified header
		DataResult<Account> account = this.iAccountService.delete(accountNumber);

		result = new Result(account.isSuccess(), account.getMessage());

		if (!account.isSuccess() || account == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);

		}
		
		return ResponseEntity.ok().lastModified(account.getData().getLastUpdateDate().atZone(ZoneId.of("UTC")))
				.body(result);
	}

	@GetMapping(path = "/accounts")
	private ResponseEntity<DataResult<List<GetListAccountDto>>> getAllAccounts() {

		DataResult<List<Account>> account = this.iAccountService.getAll();

		DataResult<List<GetListAccountDto>> accountsDto = new DataResult<List<GetListAccountDto>>(
				this.accountDtoMapper.listAccountToListAccountDto(account.getData()), account.isSuccess(),
				account.getMessage());

		return ResponseEntity.ok().body(accountsDto);
	}	

	private boolean checkAuthUser(String accountNumber) {

		int accountUserId = this.iAccountService.getByAccountNumber(accountNumber).getData().getUser().getId();

		MyCustomUser authUser = (MyCustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (authUser.getId() == accountUserId) {
			return false;
		}
		return true;
	}
	
	private boolean checkAuthUserById(int id) {
		
		List<Account> accounts = this.iAccountService.getByUserId(id).getData();
		
		if(accounts.size() !=0) {
			
			MyCustomUser authUser = (MyCustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (authUser.getId() == accounts.get(0).getUser().getId()) {
				return false;
			}
		}	
		return true;
	}
}
