package com.bridgelabz.fundoonotes.controller;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.service.UserService;

@Controller
@RequestMapping("/user")	
public class UserController {

	@Autowired
	private UserService userService;


	@PostMapping(value="/register")
	public ResponseEntity<String> register(@RequestBody UserDetails user,HttpServletRequest request) {

		if (userService.register(user,request) != null)
			return new ResponseEntity<String>("User Registered Successfully",HttpStatus.OK);

		else
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
	}

	@PostMapping(value="/login")
	public ResponseEntity<?> login(@RequestBody UserDetails user, HttpServletRequest request, HttpServletResponse response) {

		UserDetails userDetails=userService.login(user, request,response);
		if(userDetails!=null) {
			return new ResponseEntity<String>("User has been activated and found successfully",HttpStatus.FOUND);	

		}
		else

			return new ResponseEntity<String>("User not found by the given Email Id",HttpStatus.NOT_FOUND);	

	}

	@PostMapping(value="/update")
	public ResponseEntity<String> update(@RequestHeader("token") String token, @RequestBody UserDetails existinguser, HttpServletRequest request)
	{
		UserDetails userDetails=userService.update(token, existinguser, request);

		if(userDetails!=null) {
			return new ResponseEntity<String>("User Successfully Updated", HttpStatus.OK);
		}
		else

			return new ResponseEntity<String>("EmailId or password are incorrect, Please provide correct details",HttpStatus.CONFLICT);

	}

	@DeleteMapping(value="/delete")
	public ResponseEntity<String> delete(@RequestHeader("token") String token, HttpServletRequest request)
	{

		UserDetails userDetails=userService.delete(token,request);

		if (userDetails!=null)

			return new ResponseEntity<String>("User Succesfully deleted",HttpStatus.FOUND);

		else
			return  new ResponseEntity<String>("User not Found by given  Id",HttpStatus.NOT_FOUND);

	}



@GetMapping(value="/activationstatus/{token:.+}")
public ResponseEntity<String> activateUser(@PathVariable String token, HttpServletRequest request) {

	UserDetails user = userService.activateUser(token, request);

	if (user != null) {

		return new ResponseEntity<String>("User has been activated successfully", HttpStatus.FOUND);
	} 
	else {

		return new ResponseEntity<String>("Email incorrect. Please enter valid email address present in database",
				HttpStatus.NOT_FOUND);
	}

}	


}
