package com.tcs.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.tcs.bms.entity.OtpInfo;

@Repository
public interface otpInfoRepository extends JpaRepository<OtpInfo, Long> {

	OtpInfo findByAccountNumberAndOtp(String accountNumber, String otp);
	
	OtpInfo findByAccountNumber(String accountNumber);

}
