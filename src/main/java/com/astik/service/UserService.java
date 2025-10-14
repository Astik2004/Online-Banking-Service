package com.astik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.astik.dto.TransactionRequest;
import com.astik.entity.Account;
import com.astik.entity.Transaction;
import com.astik.entity.User;
import com.astik.exception.AccountNotFoundException;
import com.astik.exception.InsufficientBalanceException;
import com.astik.exception.UserNotFoundException;
import com.astik.repository.AccountRepository;
import com.astik.repository.TransactionRepository;
import com.astik.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UserService(UserRepository userRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public Account getAccountForUsername(String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + username));
        return user.getAccount();
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
    }

    @Transactional
    public String transfer(String fromUsername, TransactionRequest req) {
        User fromUser = userRepository.findByUserId(fromUsername)
                .orElseThrow(() -> new UserNotFoundException("Sender not found: " + fromUsername));

        Account fromAccount = fromUser.getAccount();
        Account toAccount = accountRepository.findByAccountNumber(req.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found: " + req.getToAccountNumber()));

        double amount = req.getAmount();

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient funds for transfer");
        }

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Record transactions
        Transaction debitTransaction = Transaction.builder()
                .account(fromAccount)
                .amount(amount)
                .transactionType("TRANSFER_DEBIT")
                .description("Transfer to " + toAccount.getAccountNumber())
                .build();

        Transaction creditTransaction = Transaction.builder()
                .account(toAccount)
                .amount(amount)
                .transactionType("TRANSFER_CREDIT")
                .description("Transfer from " + fromAccount.getAccountNumber())
                .build();

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        return "Your Current balance is "+fromAccount.getBalance();
    }

    @Transactional
    public String deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

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
