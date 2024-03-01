package com.tcs.bms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.tcs.bms.security.JwtTokenUtil;
import com.tcs.bms.service.JWTUserDetailsService;
import com.tcs.bms.service.OTPService;
import com.tcs.bms.service.UserService;

public class GenerateToken {
public static AuthenticationManager authenticationManager;
public  static JwtTokenUtil jwtTokenUtil;
public  static JWTUserDetailsService userDetailsService;
    private  static UserService userService;
public  static OTPService otpService;

    
    
	public GenerateToken(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
			JWTUserDetailsService userDetailsService, UserService userService, OTPService otpService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
		this.otpService = otpService;
	}

	    
	   
	    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String AccountNo="11a5d8";
		  // If authentication successful, generate JWT token
      
        
        try {
            // Authenticate the user with the account number and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(AccountNo, "Prabu@3")
                    
            );
            
            UserDetails userDetails = userDetailsService.loadUserByUsername(AccountNo);
            System.out.println(userDetails);
            String token = jwtTokenUtil.generateToken(userDetails);
            System.out.println("Token Number for Account : "+AccountNo + "- Token -"+ token);
            
        } catch (BadCredentialsException e) {
            // Invalid credentials, return 401 Unauthorized
            System.out.println(e);
        }

		/*
		 * // If authentication successful, generate JWT token UserDetails userDetails =
		 * userDetailsService.loadUserByUsername(loginRequest.getAccountNumber());
		 * System.out.println(userDetails); String token =
		 * jwtTokenUtil.generateToken(userDetails); Map<String, String> result = new
		 * HashMap<>(); result.put("token", token); // Return the JWT token in the
		 * response
		 */        
	}

}
