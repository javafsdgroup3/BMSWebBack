package com.tcs.bms.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcs.bms.dto.PasswordReset;
//import com.tcs.bms.dto.UserDetails;
import com.tcs.bms.dto.UserResponse;
import com.tcs.bms.entity.Account;
import com.tcs.bms.entity.User;
import com.tcs.bms.repository.AccountRepository;
import com.tcs.bms.repository.UserRepository;

@Service
public class UserServicePublicMethods {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	
	public UserResponse createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles("user");

		Random random = new Random();
		long lowerBound = 1000000000L;
		long upperBound = 9999999999L;
		String accountNumber;

		do {
			long randomNum = lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound));
			accountNumber = String.valueOf(randomNum);
		} while (accountNumber.equals(accountRepository.findByAccountNumber(accountNumber)));

		Account accountObject = new Account();
		accountObject.setAccountNumber(accountNumber);
		accountObject.setBalance(0.0);
		Account account = accountRepository.save(accountObject);

		user.setAccount(account);
		userRepository.save(user);

		UserResponse userdetail = new UserResponse();
		userdetail.setName(user.getName());
		userdetail.setEmail(user.getEmail());
		userdetail.setAddress(user.getAddress());
		userdetail.setPhone_number(user.getPhone_number());
		userdetail.setAccountNumber(user.getAccount().getAccountNumber());
		userdetail.setIFSC_code(user.getAccount().getIFSC_code());
		userdetail.setBranch(user.getAccount().getBranch());
		userdetail.setAccount_type(user.getAccount().getAccount_type());

		return userdetail;
	}

	public void updatePassword(PasswordReset reset) {
		User user = null;

		if (reset.getPin()!="") {
			Account account = accountRepository.findByAccountNo(reset.getAccountNumber());
			if (account.getPin() != null && account.getPin().equals(reset.getPin())) {
				user = userRepository.findById(account.getId()).get();
				
				if (user.getId()!=null) {
					User userDetails = user;
					userDetails.setPassword(passwordEncoder.encode(reset.getPassword()));
					userRepository.save(userDetails);
				} else {
					throw new UsernameNotFoundException("User not found");
				}
			}
		} else {
			user = userRepository.findByAccountAccountNumber(reset.getAccountNumber());
			
			if (user.getId()!=null) {
				User userDetails = user;
				userDetails.setPassword(passwordEncoder.encode(reset.getPassword()));
				userRepository.save(userDetails);
			} else {
				throw new UsernameNotFoundException("User not found");
			}
		}

		
	}

}