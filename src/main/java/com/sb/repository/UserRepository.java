package com.sb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Users;
import java.util.List;


public interface UserRepository extends JpaRepository<Users, Integer>{

	public boolean existsByEmail(String email);
	
	public Users findByEmail(String email);
    public Users findByEmailAndPhoneno(String email, String phoneno);
}
