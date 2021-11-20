package com.example.demo.controllers.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserDetails;
import com.example.demo.model.response.ResponseUserDetails;
import com.example.demo.services.impl.UserDetailsServicesImpl;

@RestController
@RequestMapping("/users")
public class UserDetailsRestController {
	
	public static final String ERROR_MESSAGE = "Requested Index Out of Bound";
	
	@Autowired UserDetailsServicesImpl userDetailsServices;
	
	@GetMapping("/data")
	public List<UserDetails> getUserDetails() {
		
		return userDetailsServices.getUserDetails();
	}
	
/*
 * Count on the basis of distinct method
 * need to override hashcode and equals method
*/

/*
 * Count on the basis of filter method
 * by help of Set
*/
	@GetMapping("/count")
	public long getCount(@RequestParam Optional<Boolean> byObjectNature ) {
		List<UserDetails> responseUserDetails = getUserDetails();
		long res;
		if(byObjectNature != null && byObjectNature.isPresent() && byObjectNature.get()) {
			res = userDetailsServices.countUserIdsByClassNature(responseUserDetails);
		}else {
			res = userDetailsServices.countUserIdsByOtherLogic(responseUserDetails);
		}
		return res;
	}

	@GetMapping("/modify/{indexNo}/{value}")
	public Object getModifiedData(@PathVariable Integer indexNo,@PathVariable String value) {
		List<UserDetails> userDetails = getUserDetails();
		try{
			userDetails = userDetailsServices.modifyData(indexNo, value, userDetails);
			
		}catch(RuntimeException e) {
			return new ResponseEntity<Object>(ERROR_MESSAGE + "\n" + e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		ResponseUserDetails res =new ResponseUserDetails();
		res.setUserDetails(userDetails);
		
		return new ResponseEntity<Object>(res,HttpStatus.OK);
	}
	
}
