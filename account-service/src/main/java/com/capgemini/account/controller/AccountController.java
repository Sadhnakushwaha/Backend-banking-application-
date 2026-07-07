package com.capgemini.account.controller;

import com.capgemini.account.model.Account;
import com.capgemini.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")  // Allow CORS from your frontend
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Get all accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAccounts();
    }

    // Get account by ID
    @GetMapping("/{id}")
    public Optional<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }
    
 // Get accounts by userId (new method)
    @GetMapping("/by-user/{userId}")  // New endpoint to filter accounts by userId
    public List<Account> getAccountsByUserId(@PathVariable Long userId) {
        return accountService.getAccountsByUserId(userId);  // Fetch accounts for the specific userId
    }

    // Create or update an account
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.saveAccount(account);
    }

    // Edit account details
    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody Account accountDetails) {
        Optional<Account> existingAccount = accountService.getAccountById(id);
        if (existingAccount.isPresent()) {
            Account account = existingAccount.get();
            account.setAccountNumber(accountDetails.getAccountNumber());
            account.setBalance(accountDetails.getBalance());
            account.setStatus(accountDetails.getStatus());
            return accountService.saveAccount(account);
        }
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Account> updateAccountStatus(
            @PathVariable Long id, 
            @RequestParam String status) {

        Account updatedAccount = accountService.updateAccountStatus(id, status);
        
        if (updatedAccount != null) {
            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{accountId}/balance")
    public ResponseEntity<Account> updateBalance(@PathVariable Long accountId, @RequestBody Double newBalance) {
        Account updatedAccount = accountService.updateBalance(accountId, newBalance);
        return ResponseEntity.ok(updatedAccount);
    }

    
    // Delete an account
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }
}
