package com.tcs.bms.service;

import com.tcs.bms.entity.User;

public interface UserService {
	public User registerUser(User user);

	User getUserByAccountNumber(String account_no);

	public void saveUser(User user);

	User updateUser(User user);
}
