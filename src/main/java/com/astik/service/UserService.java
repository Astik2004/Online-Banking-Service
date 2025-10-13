package com.astik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.astik.dto.TransactionRequest;
import com.astik.entity.Account;
import com.astik.entity.Transaction;
import com.astik.entity.User;
import com.astik.exception.InsufficientBalanceException;
import com.astik.repository.AccountRepository;
import com.astik.repository.TransactionRepository;
import com.astik.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    
    @Autowired
    public UserService(UserRepository userRepository,TransactionRepository transactionRepository,AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.transactionRepository=transactionRepository;
        this.accountRepository=accountRepository;
    }

    public User getUserByUserId(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException("User not found: " + userId);
        }
    }


	public Account getAccountForUsername(String username) {
		Optional<User> user=userRepository.findByUserId(username);
		return user.get().getAccount();
	}
	
	public List<Transaction> getTransactionsForAccount(Long accountId) 
	{
		 return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
	}
	@Transactional
	public void transfer(String fromUsername, TransactionRequest req) {
		User userfrom=userRepository.findByUserId(fromUsername).orElseThrow(()->new RuntimeException("User not found"));
		Account fromAccount=userfrom.getAccount();
		
		Account toAccount=accountRepository.findByAccountNumber(req.getToAccountNumber()).orElseThrow(()->new RuntimeException("Recipent Account Not found"));
		
		Double amount=req.getAmount();
		
		if(fromAccount.getBalance()<amount)
		{
			 throw new InsufficientBalanceException("Insufficient funds");
		}
		
		//deduct money
		fromAccount.setBalance(fromAccount.getBalance()-amount);
		
		//credit
		
		toAccount.setBalance(toAccount.getBalance()+amount);
		
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		// create transaction records
		 Transaction t1 = Transaction.builder()
								 .account(fromAccount)
								 .amount(amount)
								 .transactionType("TRANSFER_DEBIT")
								 .description("Transfer to " + toAccount.getAccountNumber())
								 .build();
		 Transaction t2 = Transaction.builder()
								 .account(toAccount)
								 .amount(amount)
								 .transactionType("TRANSFER_CREDIT")
								 .description("Transfer from " + fromAccount.getAccountNumber())
								 .build();
		 transactionRepository.save(t1);
		 transactionRepository.save(t2);
	}

	public String deposit(String accountNumber, Double amount) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
	            .orElseThrow(() -> new RuntimeException("Account not found"));

	    // Credit the account
	    account.setBalance(account.getBalance() + amount);
	    accountRepository.save(account);

	    // Record transaction
	    Transaction transaction = Transaction.builder()
	            .account(account)
	            .transactionType("CREDIT")
	            .amount(amount)
	            .description("Credited by CASH")
	            .build();
	    
	    transactionRepository.save(transaction);

	    return "Deposit successful! New balance: " + account.getBalance();
	}
}
