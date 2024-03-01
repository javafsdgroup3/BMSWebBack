package com.tcs.bms.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.bms.dto.LoginRequest;
import com.tcs.bms.dto.OtpRequest;
import com.tcs.bms.dto.OtpVerificationRequest;
import com.tcs.bms.dto.PasswordReset;
import com.tcs.bms.dto.UserResponse;
import com.tcs.bms.entity.User;
import com.tcs.bms.security.JwtTokenUtil;
import com.tcs.bms.service.OTPService;
import com.tcs.bms.service.UserService;
import com.tcs.bms.service.UserServicePublicMethods;

import lombok.Data;


@RestController
@RequestMapping("/bms/users")
@Data
public class UserController {

	private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final OTPService otpService;
    private final UserServicePublicMethods service;
    
    public UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
			UserDetailsService userDetailsService, UserService userService, OTPService otpService,
			UserServicePublicMethods service) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
		this.otpService = otpService;
		this.service = service;
	}
 
    
    @PostMapping("/passwordreset")
    
	public ResponseEntity<String> passwordreset(@RequestBody PasswordReset reset) {
		System.out.println(reset.getPin() + "--"+reset.getAccountNumber());
		service.updatePassword(reset);
		//return "Updated Successfully";
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/register")
 
	public ResponseEntity<UserResponse> createUser(@RequestBody User user) {
		UserResponse userDetails = service.createUser(user);
		return ResponseEntity.ok(userDetails);
	}

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
        	Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getAccountNumber(), loginRequest.getPassword()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid account number or password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getAccountNumber());
        System.out.println(userDetails);
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> result =  new HashMap<>();
        result.put("token", token);
        return new ResponseEntity<>(result , HttpStatus.OK);
    }
    
    
    @PostMapping("/generate-otp")
    
    public ResponseEntity<?> generateOtp(@RequestBody OtpRequest otpRequest) {

    	 String accountNumber = otpRequest.getAccountNumber();

         // Fetch the user by account number to get the associated email
         User user = userService.getUserByAccountNumber(accountNumber);
         if (user == null) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found for the given account number");
         }

         // Generate OTP and save it in the database
         String otp = otpService.generateOTP(accountNumber);


        // Send the OTP to the user's email address asynchronously
        CompletableFuture<Boolean> emailSendingFuture = otpService.sendOTPByEmail(user.getEmail(), user.getName(), accountNumber, otp);

        // Wait for the email sending process to complete and handle the response
        try {
            boolean otpSent = emailSendingFuture.get(); // This will block until the email sending is complete

            if (otpSent) {
                // Return JSON response with success message
                return ResponseEntity.ok().body("{\"message\": \"OTP sent successfully\"}");
            } else {
                // Return JSON response with error message
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to send OTP\"}");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Return JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to send OTP\"}");
        }
    }

    
    @PostMapping("/verify-otp")
    
    public ResponseEntity<?> verifyOtpAndLogin(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        String accountNumber = otpVerificationRequest.getAccountNumber();
        String otp = otpVerificationRequest.getOtp();
        
        System.out.println(accountNumber+"  "+otp);

        // Validate OTP against the stored OTP in the database
        boolean isValidOtp = otpService.validateOTP(accountNumber, otp);
        System.out.println(isValidOtp);

        if (isValidOtp) {
            // If OTP is valid, generate JWT token and perform user login
        	
            // If authentication successful, generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(accountNumber);
            String token = jwtTokenUtil.generateToken(userDetails);
            Map<String, String> result = new HashMap<>();
            result.put("token", token);
            // Return the JWT token in the response
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // Invalid OTP, return 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Invalid OTP\"}");
        }
    }

    
    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setName(updateUser.getName());
        userResponse.setEmail(updateUser.getEmail());
        userResponse.setAccountNumber(updateUser.getAccount().getAccountNumber());
        userResponse.setIFSC_code(updateUser.getAccount().getIFSC_code());
        userResponse.setBranch(updateUser.getAccount().getBranch());
        userResponse.setAccount_type(updateUser.getAccount().getAccount_type());


        return ResponseEntity.ok(userResponse);
    }

}