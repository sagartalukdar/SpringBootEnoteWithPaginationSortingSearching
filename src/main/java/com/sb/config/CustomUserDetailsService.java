package com.sb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sb.entity.Users;
import com.sb.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository ur;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Users lu=ur.findByEmail(username);
	    if(lu!=null) {
	    	return new CustomUser(lu);
	    }else {
	    	throw new UsernameNotFoundException("User Not Found");
	    }
	}

}
