package com.msa.bankingsystem.services.account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.msa.bankingsystem.core.exception.BusinessException;
import com.msa.bankingsystem.core.externalServices.ExchangeChanger;
import com.msa.bankingsystem.core.results.DataResult;
import com.msa.bankingsystem.core.results.SuccessDataResult;
import com.msa.bankingsystem.dataAccess.account.IAccountRepository;
import com.msa.bankingsystem.models.Account;
import com.msa.bankingsystem.models.Bank;
import com.msa.bankingsystem.models.UserEntity;
import com.msa.bankingsystem.services.message.Messages;
import com.msa.bankingsystem.services.requests.CreateAccountRequest;
import com.msa.bankingsystem.services.requests.CreateDepositRequest;
import com.msa.bankingsystem.services.requests.CreateTransferRequest;
import com.msa.bankingsystem.services.requests.CreateWithDrawRequest;
import com.msa.bankingsystem.services.webSecurityConfig.MyCustomUser;

@Service
public class AccountManager implements IAccountService {

	@Value("${account.type}")
	private List<String> definedTypes;
	@Value(value = "${kafka.topicName}")
	public String topicName;

	private IAccountRepository iAccountRepository;
	private KafkaTemplate<String, String> kafkaTemplate;
	private ExchangeChanger exchangeChanger;

	@Autowired
	public AccountManager(List<String> definedTypes, IAccountRepository iAccountRepository,
			KafkaTemplate<String, String> kafkaTemplate, ExchangeChanger exchangeChanger) {
		this.definedTypes = definedTypes;
		this.iAccountRepository = iAccountRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.exchangeChanger = exchangeChanger;
	}

	@Override
	public DataResult<Account> create(CreateAccountRequest createAccountRequest) {

		checkIfType(createAccountRequest.getType(), this.definedTypes);
		
		MyCustomUser authUser = (MyCustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		UserEntity user = new UserEntity().builder().id(authUser.getId()).username(authUser.getUsername())
				.email(authUser.getEmail()).password(authUser.getPassword()).isEnabled(authUser.isEnabled())
				.authorities(authUser.getAuthorities().toString()).build();

		Bank bank = new Bank().builder().id(createAccountRequest.getBank_id()).build();

		Account account = Account.builder().accountNumber(generateRandomAccountNumber()).user(user).bank(bank)
				.type(createAccountRequest.getType().toUpperCase()).isDeleted(false).creationDate(LocalDateTime.now())
				.lastUpdateDate(LocalDateTime.now()).balance(0).build();

		this.iAccountRepository.save(account);

		return new SuccessDataResult<Account>(account, Messages.CREATEACCOUNT);
	}

	@Override
	public DataResult<Account> delete(String accountNumber) {

		checkIfAccountExists(accountNumber);

		Account account = null;

		if (this.iAccountRepository.updateDeletedColumn(accountNumber, LocalDateTime.now())) {
			account = this.iAccountRepository.getByAccountNumber(accountNumber);
		}
		return new SuccessDataResult<Account>(account, Messages.DELETEACCOUNTBYID);
	}

	@Override
	public DataResult<Account> getByAccountNumber(String accountNumber) {

		checkIfAccountExists(accountNumber);

		Account account = this.iAccountRepository.getByAccountNumber(accountNumber);

		return new SuccessDataResult<Account>(account, Messages.LISTEDACCOUNTBYACCOUNTNO);
	}

	@Override
	public DataResult<List<Account>> getByUserId(int userId) {

		List<Account> account = this.iAccountRepository.getByUserIdNumberWithUserAndBank(userId);

		return new SuccessDataResult<List<Account>>(account, Messages.LISTEDACCOUNTBYUSERID);
	}

	@Override
	public DataResult<Account> deposit(String accountNumber, CreateDepositRequest createDepositRequest) {

		checkIfAccountExists(accountNumber);

		Account account = this.iAccountRepository.getByAccountNumber(accountNumber);
		if (this.iAccountRepository.updateBalance(accountNumber,
				account.getBalance() + createDepositRequest.getAmount(), LocalDateTime.now())) {
			account = this.iAccountRepository.getByAccountNumber(accountNumber);
		}
		kafkaTemplate.send(topicName, log(accountNumber, null, "deposit", createDepositRequest.getAmount()));

		return new SuccessDataResult<Account>(account, Messages.SUCCESSDEPOSİTOPERATİON);
	}

	@Override
	public DataResult<Account> withDraw(String accountNumber, CreateWithDrawRequest createWithDrawRequest) {

		checkIfAccountExists(accountNumber);

		checkIfEnoughBalance(accountNumber, createWithDrawRequest.getAmount());
		Account account = this.iAccountRepository.getByAccountNumber(accountNumber);
		if (this.iAccountRepository.updateBalance(accountNumber,
				account.getBalance() - createWithDrawRequest.getAmount(), LocalDateTime.now())) {
			account = this.iAccountRepository.getByAccountNumber(accountNumber);
		}

		return new SuccessDataResult<Account>(account, Messages.SUCCESSWITHDRAWOPERATİON);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BusinessException.class)
	public DataResult<Account> transferBetweenAccounts(String senderAccountNumber,
			CreateTransferRequest createTransferRequest) {

		checkIfAccountExists(senderAccountNumber);
		checkIfAccountExists(createTransferRequest.getTransferredAccountNumber());
		checkIfEnoughBalance(senderAccountNumber, createTransferRequest.getAmount());

		double balance = this.iAccountRepository.getByAccountNumber(senderAccountNumber).getBalance();
		double transferredAccountBalance = this.iAccountRepository
				.getByAccountNumber(createTransferRequest.getTransferredAccountNumber()).getBalance();

		double calculatedCurrencyAmount = checkCurrencyAmount(senderAccountNumber,
				createTransferRequest.getTransferredAccountNumber(), createTransferRequest.getAmount());

		double lastSentAmount = checkTheBankId(senderAccountNumber, createTransferRequest.getTransferredAccountNumber(),
				createTransferRequest.getAmount());

		this.iAccountRepository.updateBalance(senderAccountNumber, balance - lastSentAmount, LocalDateTime.now());
		this.iAccountRepository.updateBalance(createTransferRequest.getTransferredAccountNumber(),
				transferredAccountBalance + calculatedCurrencyAmount, LocalDateTime.now());

		kafkaTemplate.send(topicName, log(senderAccountNumber, createTransferRequest.getTransferredAccountNumber(),
				"transfer", createTransferRequest.getAmount()));

		Account lastSenderAccount = this.iAccountRepository.getByAccountNumber(senderAccountNumber);

		return new SuccessDataResult<Account>(lastSenderAccount, Messages.SUCCESSTRANSFEROPERATİON);

	}

	@Override
	public DataResult<List<Account>> getAll() {
		System.err.println(this.iAccountRepository.getAll());
		return new SuccessDataResult<List<Account>>(this.iAccountRepository.getAll(), Messages.LISTEDALLACCOUNTS);
	}

	private boolean checkIfType(String accountType, List<String> definedTypes) {

		accountType = accountType.toUpperCase();

		for (String type : definedTypes) {
			if (type.matches(accountType)) {
				return true;
			}
		}
		throw new BusinessException(Messages.INVALIDACCOUNTTYPE + accountType);
	}

	// or UUID.randomUUID().toString();
	private String generateRandomAccountNumber() {

		Random random = new Random();
		int randomNumber = random.nextInt(1000000000, 2000000000);
		return String.valueOf(randomNumber);
	}

	// check amount in account balance
	private boolean checkIfEnoughBalance(String accountNumber, double amount) {

		if (this.iAccountRepository.getByAccountNumber(accountNumber).getBalance() >= amount) {
			return true;
		}
		throw new BusinessException(Messages.INSUFFICIENTBALANCE);
	}

	// check if exists account
	private boolean checkIfAccountExists(String accountNumber) {

		if (this.iAccountRepository.getByAccountNumber(accountNumber) != null) {
			return true;
		}
		throw new BusinessException(Messages.NOTFOUNDACCOUNTNUMBER);
	}

	// The currency is checked and the amount of money is converted if necessary
	private double checkCurrencyAmount(String senderAccountNumber, String transferredAccountNumber, double amount) {

		String senderAccountType = this.iAccountRepository.getByAccountNumber(senderAccountNumber).getType();

		String transferredAccountType = this.iAccountRepository.getByAccountNumber(transferredAccountNumber).getType();

		if (!senderAccountType.contains(transferredAccountType)) {
			// senderAccount TYPE == TRY -- >>
			if (senderAccountType.contains("TRY")) {

				if (transferredAccountType.contains("GAU")) {
					return amount / this.exchangeChanger.calculateGoldBuy(); // -->> GAU
				} else {
					return this.exchangeChanger.calculateExchange(senderAccountType, transferredAccountType, amount); // -->
																														// OTHERS
				}
			}

			// senderAccount TYPE == USD -- >>
			else if (senderAccountType.contains("USD")) { // USD

				if (transferredAccountType.contains("GAU")) {
					double tryAmount = this.exchangeChanger.calculateExchange(senderAccountType, "TRY", amount); // -->>
																													// GAU
					return tryAmount / this.exchangeChanger.calculateGoldBuy();
				} else {
					return this.exchangeChanger.calculateExchange(senderAccountType, transferredAccountType, amount); // -->
																														// OTHERS
				}
			}

			// senderAccount TYPE == EUR -- >>
			else if (senderAccountType.contains("EUR")) { // EUR -->>

				if (transferredAccountType.contains("GAU")) {
					double tryAmount = this.exchangeChanger.calculateExchange(senderAccountType, "TRY", amount); // -->>
																													// GAU
					return tryAmount / this.exchangeChanger.calculateGoldBuy();
				} else {
					return this.exchangeChanger.calculateExchange(senderAccountType, transferredAccountType, amount); // -->
																														// OTHERS
				}

			}

			// senderAccount TYPE == GAU -- >>
			else if (senderAccountType.contains("GAU")) {

				if (transferredAccountType.contains("TRY")) { // -->> TRY
					return this.exchangeChanger.calculateGoldSelling() * amount;
				} else if (transferredAccountType.contains("USD")) { // -->> USD
					double tryAmount = this.exchangeChanger.calculateGoldSelling() * amount;
					return this.exchangeChanger.calculateExchange("TRY",transferredAccountType, tryAmount);
				}

				else if (transferredAccountType.contains("EUR")) {
					double tryAmount = this.exchangeChanger.calculateGoldSelling() * amount;
					return this.exchangeChanger.calculateExchange("TRY",transferredAccountType , tryAmount); // EUR
				}

			}

		}

		// else senderAccountType == transferredAccountType
		return amount;
	}

	// Transaction fees apply if bank accounts are different
	private double checkTheBankId(String senderAccountNumber, String transferredAccountNumber, double amount) {

		Account transferredAccount = this.iAccountRepository.getByAccountNumber(transferredAccountNumber);
		Account senderAccount = this.iAccountRepository.getByAccountNumber(senderAccountNumber);

		double newAmount = amount;

		if ((transferredAccount.getBank().getId() != senderAccount.getBank().getId())) {

				newAmount = calTransferFee(senderAccount.getType(), amount);		

		}
		return newAmount;
	}

	// Applies the transaction fee to be applied
	private double calTransferFee(String senderAccountType, double amount) {
		if (senderAccountType.contains("TRY")) {
			amount += 3;
		} else if ((senderAccountType.contains("USD") || (senderAccountType.contains("EUR")))) {
			amount += 1;
		}
		return amount;
	}

	private String log(String senderAccountNumber, String transferredAccountNumber, String operationType,
			double amount) {
		String message = null;
		if (operationType.contains("transfer")) {

			message = amount + "," + senderAccountNumber + " to " + transferredAccountNumber + " : transferred";

		} else if (operationType.contains("deposit")) {
			// deposit operation
			message = senderAccountNumber + "," + amount + " " + " : " + " deposited";

		}
		return message;
	}

}
