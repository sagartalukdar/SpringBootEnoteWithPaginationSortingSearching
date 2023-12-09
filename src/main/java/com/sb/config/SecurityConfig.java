package com.sb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userdetailsservice;
	@Bean
	public BCryptPasswordEncoder pe() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider dapm() {
	DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
	dap.setUserDetailsService(userdetailsservice);
	dap.setPasswordEncoder(pe());
	
	return dap;
	}
	@Bean
	public SecurityFilterChain sfc(HttpSecurity hs) throws Exception {
		hs.csrf().disable().authorizeHttpRequests()
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/**").permitAll().and()
		.formLogin().loginPage("/loginpage")
		.loginProcessingUrl("/userlogin")
		.defaultSuccessUrl("/user/add").
	     permitAll();
		return hs.build();
		
		
	}
}
