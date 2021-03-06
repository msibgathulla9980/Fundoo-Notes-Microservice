package com.bridgelabz.fundoonotes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
@ComponentScan("com.bridgelabz.fundoonotes")
public class ApplicationConfiguration {
	
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}

	
	
}
