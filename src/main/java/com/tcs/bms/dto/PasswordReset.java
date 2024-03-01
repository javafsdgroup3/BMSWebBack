package com.tcs.bms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class PasswordReset {

	private String accountNumber;
	private String password;
	private String pin;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getUpdatedPassword() {
		return password;
	}
	public void setUpdatedPassword(String updatedPassword) {
		this.password = updatedPassword;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	@Override
	public String toString() {
		return "PasswordReset [accountNumber=" + accountNumber + ", updatedPassword=" + password + ", pin=" + pin
				+ "]";
	}

}