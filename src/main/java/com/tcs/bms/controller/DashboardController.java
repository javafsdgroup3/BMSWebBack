package com.tcs.bms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.bms.dto.AccountResponse;
import com.tcs.bms.dto.UserResponse;
import com.tcs.bms.service.DashboardService;
import com.tcs.bms.util.LoggedinUser;

@RestController
@RequestMapping("/bms/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<UserResponse> getUserDetails() {
        String accountNumber = LoggedinUser.getAccountNumber();
        UserResponse userResponse = dashboardService.getUserDetails(accountNumber);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/account")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<AccountResponse> getAccountDetails() {
        String accountNumber = LoggedinUser.getAccountNumber();
        AccountResponse accountResponse = dashboardService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(accountResponse);
    }
    
    
   
}