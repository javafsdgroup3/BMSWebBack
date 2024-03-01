package com.tcs.bms.service;

import com.tcs.bms.dto.AccountResponse;
import com.tcs.bms.dto.UserResponse;

public interface DashboardService {
    UserResponse getUserDetails(String accountNumber);
    AccountResponse getAccountDetails(String accountNumber);
}