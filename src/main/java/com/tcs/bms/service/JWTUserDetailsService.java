package com.tcs.bms.service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tcs.bms.entity.User;
import com.tcs.bms.repository.UserRepository;

@Service
public class JWTUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;
      

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
        User user = userRepository.findByAccountAccountNumber(accountNumber);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid account number");
        }
        List<GrantedAuthority> authorities = Arrays.stream(user.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Return a UserDetails object that wraps the User entity
        return new org.springframework.security.core.userdetails.User(
                user.getAccount().getAccountNumber(),  // Use account number as the username
                user.getPassword(), authorities
        );
    }	
}