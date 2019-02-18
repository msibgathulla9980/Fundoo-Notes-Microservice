package com.bridgelabz.fundoonotes.service;


import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bridgelabz.fundoonotes.dao.UserDetailsRepository;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.utility.EmailUtil;
import com.bridgelabz.fundoonotes.utility.TokenGenerator;



@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private EmailUtil email;

	@Autowired
	private TokenGenerator generateToken;

	@Transactional
	public UserDetails register(UserDetails user,HttpServletRequest request) {

		UserDetails newuser= userDetailsRepository.save(user);
		if(newuser==null) {
			return null;
		}

		String token = generateToken.generateToken(String.valueOf(user.getId()));
		String link=" Please click the link below to verify. \n\nhttp://localhost:8082/user/activationstatus/"+token+"\n\nRegards,\nMohammed Sibgathulla.";
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		email.sendEmail("msibgathulla@gmail.com", "Registration Mail", link);

		return newuser;
	}

	@Transactional
	public UserDetails activateUser(String token, HttpServletRequest request) {
		int id = generateToken.verifyToken(token);
		Optional<UserDetails> optional = userDetailsRepository.findById(id);	

		if(optional.isPresent())
		{
			UserDetails user=optional.get();
			user.setActivationStatus(true);
			userDetailsRepository.save(user);
			return user;
		}
		return null;  
	}

	@Transactional
	public UserDetails login(UserDetails user, HttpServletRequest request, HttpServletResponse response) {
		UserDetails existinguser=userDetailsRepository.findByEmailId(user.getEmailId());
		if(existinguser!=null) {
			if(bcryptEncoder.matches(user.getPassword(), existinguser.getPassword()) && existinguser.isActivationStatus()==true){
				//		user.setPassword(bcryptEncoder.encode(user.getPassword()));
				String token=generateToken.generateToken(String.valueOf(existinguser.getId()));
				response.setHeader("Token",token);
				return existinguser;
			}
		}

		return null;
	}

	@Transactional
	public UserDetails update(String token,UserDetails user,HttpServletRequest request) {

		int id=generateToken.verifyToken(token);
		Optional<UserDetails> optional=userDetailsRepository.findById(id);
		if(optional.isPresent())
		{
			UserDetails userNew=optional.get();
			System.out.println(userNew);
			userNew.setEmailId(user.getEmailId());
			userNew.setPassword(bcryptEncoder.encode(userNew.getPassword()));
			userNew.setMobileNumber(user.getMobileNumber());
			userDetailsRepository.save(userNew);
			System.out.println(userNew);
			return userNew;
		}
		return null;

	}

	@Transactional
	public UserDetails delete(String token,HttpServletRequest request) {
		int id=generateToken.verifyToken(token);
		Optional<UserDetails> optional=userDetailsRepository.findById(id);
		if(optional.isPresent())
		{
			UserDetails userNew=optional.get();
			userDetailsRepository.deleteAll();
			return userNew;
		}
		return null;

	}
}



