package com.sb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sb.entity.Users;
import com.sb.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Component
public class UserServiceimpl implements UserService{

	@Autowired
	private UserRepository ur;
	@Autowired
	private BCryptPasswordEncoder bpe;
	@Override
	public Users saveUser(Users u) {
		u.setRole("ROLE_USER");
		String p=u.getPassword();
		u.setPassword(bpe.encode(p));
		Users nu=ur.save(u);
		return nu;
	}

	@Override
	public boolean existsEmail(String email) {
		return ur.existsByEmail(email);
	}

	public void removeMessage() {
		HttpSession mess=((ServletRequestAttributes)
				(RequestContextHolder.getRequestAttributes()))
				.getRequest().getSession();
		mess.removeAttribute("msg");
	}

}
