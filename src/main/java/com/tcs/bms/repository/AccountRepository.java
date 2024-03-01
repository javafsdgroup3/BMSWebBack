package com.tcs.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcs.bms.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountNumber(String accountNumber);
    
    @Query(value = "Select * from account where account_number = :accountNumber", nativeQuery = true)
	Account findByAccountNo(@Param("accountNumber") String accountNumber);

	
}
