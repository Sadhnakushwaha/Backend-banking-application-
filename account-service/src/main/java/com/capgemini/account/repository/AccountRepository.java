package com.capgemini.account.repository;

import com.capgemini.account.model.Account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // You can add custom query methods if needed
	 List<Account> findByUserId(Long userId);  // Add this method
}
