package com.tcs.bms.service;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcs.bms.dto.PasswordReset;
//import com.tcs.bms.dto.UserDetails;
import com.tcs.bms.entity.Account;
import com.tcs.bms.entity.User;
import com.tcs.bms.exception.UserValidation;
import com.tcs.bms.repository.AccountRepository;
import com.tcs.bms.repository.UserRepository;
import com.tcs.bms.util.LoggedinUser;

@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
	private AccountRepository accountRepository;


    public UserServiceImpl(UserRepository userRepository, AccountService accountService,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.passwordEncoder =  passwordEncoder;
    }
    
    @Override
    public User getUserByAccountNumber(String account_no) {
    	return userRepository.findByAccountAccountNumber(account_no);
    }
    
    @Override
    public User registerUser(User user) {
        
    	 String encodedPassword = passwordEncoder.encode(user.getPassword());
         user.setPassword(encodedPassword);

        // Save the user details
        User savedUser = userRepository.save(user);

        // Create an account for the user
        Account account = accountService.createAccount(savedUser);

        savedUser.setAccount(account);
        userRepository.save(savedUser);
        
        System.out.println(savedUser.getAccount().getAccountNumber());
        System.out.println(account.getUser().getName());

        
        return savedUser;
    }

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
		
	}

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findByAccountAccountNumber(LoggedinUser.getAccountNumber());
        if(user.getEmail() != null){
            if(user.getEmail().isEmpty())
                throw new UserValidation("Email can't be empty");
            else
                existingUser.setEmail(user.getEmail());
        }
        if(user.getName() != null){
            if(user.getName().isEmpty())
                throw new UserValidation("Name can't be empty");
            else
                existingUser.setName(user.getName());
        }
        if(user.getPhone_number() != null){
            if(user.getPhone_number().isEmpty())
                throw new UserValidation("Phone number can't be empty");
            else
                existingUser.setPhone_number(user.getPhone_number());
        }
        if(user.getAddress() != null){
            existingUser.setAddress(user.getAddress());
        }
        return userRepository.save(existingUser);
    }

	
		
	}



