package com.capgemini.account.service;

import com.capgemini.account.model.Account;
import com.capgemini.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Get all accounts
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    // Get account by id
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
    
    // Get account by userId
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);  // Fetch accounts based on userId
    }
    

    // Create or update an account
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    // Delete account
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Account updateAccountStatus(Long id, String status) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setStatus(status); // Update the account's status
            return accountRepository.save(account); // Save the updated account
        }
        return null; // If account not found, return null
    }
    
    public Account updateBalance(Long accountId, Double newBalance) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setBalance(newBalance);
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found"); // Consider handling this more gracefully
        }
    }


}
