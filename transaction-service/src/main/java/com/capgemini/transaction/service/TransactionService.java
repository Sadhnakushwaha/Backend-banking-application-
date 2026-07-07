package com.capgemini.transaction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.capgemini.account.model.Account;
//import com.capgemini.account.repository.AccountRepository;
import com.capgemini.transaction.model.Transaction;
import com.capgemini.transaction.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${accountService.url}")
    private String accountServiceUrl; // The URL of Account Service

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get a transaction by ID
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
 // In your TransactionService:
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }


    @Transactional
    // Fetch the Account details from Account Service using RestTemplate
    public Transaction saveTransaction(Transaction transaction) {
        if (transaction.getAccountId() == null) {
            throw new IllegalArgumentException("Account ID cannot be null.");
        }

        // Call the Account Service to get the Account
        Account account = restTemplate.getForObject(accountServiceUrl + "/accounts/" + transaction.getAccountId(), Account.class);

        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }

        // Handle balance update based on transaction type (deposit/withdraw)
        if ("deposit".equals(transaction.getType())) {
            System.out.println("Deposit transaction received. Amount: " + transaction.getAmount());
            account.setBalance(account.getBalance() + transaction.getAmount());
            // Log the updated balance
            System.out.println("Updated balance after deposit: " + account.getBalance());
        } else if ("withdraw".equals(transaction.getType())) {
            if (account.getBalance() < transaction.getAmount()) {
                System.out.println("Insufficient funds for withdrawal. Available balance: " + account.getBalance());
                throw new IllegalArgumentException("Insufficient funds.");
            }
            account.setBalance(account.getBalance() - transaction.getAmount());
        }


        // Log the request URL and account balance update for debugging purposes
        System.out.println("Account balance before update: " + account.getBalance());
        System.out.println("Updating account balance to: " + account.getBalance());

        // Ensure the PUT request is correct and check the response
        try {
            restTemplate.put(accountServiceUrl + "/accounts/" + account.getId() + "/balance", account.getBalance());
            System.out.println("Account balance updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating account balance: " + e.getMessage());
            throw new RuntimeException("Error updating account balance: " + e.getMessage());
        }


        // Save the transaction
        return transactionRepository.save(transaction);
    }

    // Update an existing transaction (if needed)
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Delete a transaction by ID
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
