package com.astik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astik.dto.DepositeRequest;
import com.astik.dto.TransactionRequest;
import com.astik.entity.Account;
import com.astik.entity.Transaction;
import com.astik.entity.User;
import com.astik.repository.TransactionRepository;
import com.astik.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://127.0.0.1:5500/")
public class UserController {

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, TransactionRepository transactionRepository) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Endpoint to get the currently logged-in user's account details.
     * The user is obtained from the SecurityContext via @AuthenticationPrincipal
     */
    @GetMapping("/account")
    public ResponseEntity<Account> getAccountDetails(@AuthenticationPrincipal UserDetails userDetails) {
        // Load full User entity using the userId from UserDetails
        User user = userService.getUserByUserId(userDetails.getUsername());
        
        // Return the associated account
        Account account = user.getAccount();
        return ResponseEntity.ok(account);
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUserId(userDetails.getUsername());
        Account acc = userService.getAccountForUsername(user.getUsername());
        return ResponseEntity.ok(userService.getTransactionsForAccount(acc.getId()));
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TransactionRequest req) 
    {
    	String result=userService.transfer(userDetails.getUsername(), req);
    	return ResponseEntity.ok(result);
    }
    
    @PostMapping("/deposit")
    public ResponseEntity<String> depositMoney(@AuthenticationPrincipal UserDetails userDetails,@RequestBody DepositeRequest deposite) {

        if (deposite.getBalance() <= 0) {
            return ResponseEntity.badRequest().body("Deposit amount must be greater than zero");
        }

        User user = userService.getUserByUserId(userDetails.getUsername());
        String accountNumber = user.getAccount().getAccountNumber();

        String result = userService.deposit(accountNumber, deposite.getBalance());
        return ResponseEntity.ok(result);
    }
}
