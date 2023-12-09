package com.sb.service;

import com.sb.entity.Users;

public interface UserService {

	public Users saveUser(Users u);
	
	public boolean existsEmail(String email);
	
	
}
