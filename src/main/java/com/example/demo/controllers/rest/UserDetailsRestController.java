package com.example.demo.controllers.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserDetails;
import com.example.demo.model.response.ResponseUserDetails;
import com.example.demo.services.interfaces.UserDetailsServices;

@RestController
@RequestMapping("/users")
public class UserDetailsRestController {
	
	public static final String ERROR_MESSAGE = "Requested Index Out of Bound";
	
	@Autowired UserDetailsServices userDetailsServices;
	
/*
 * Count on the basis of distinct method
 * need to override hashcode and equals method
*/
	@GetMapping("/countOfUniqueUserIds")
	public long getCountOfUniqueUserIds() {
		ResponseEntity<UserDetails[]> responseUserDetails = userDetailsServices.getUserDetails();
		return Stream.of(responseUserDetails.getBody()).distinct().count();
	}

/*
 * Count on the basis of filter method
 * by help of Set
*/
	@GetMapping("/count")
	public long getCount() {
		ResponseEntity<UserDetails[]> responseUserDetails = userDetailsServices.getUserDetails();
		Set<Integer> setOfUserIds = new HashSet<Integer>();
		return Stream.of(responseUserDetails.getBody()).
				filter(e -> {
					if (setOfUserIds.contains(e.getUserId()))
						return false;
					else {
						setOfUserIds.add(e.getUserId());
						return true;
					}
				})
				.count();
	}

	@GetMapping("/modify/{indexNo}/{value}")
	public Object getModifiedData(@PathVariable Integer indexNo,@PathVariable String value) {
		ResponseEntity<UserDetails[]> responseGetUserDetails = userDetailsServices.getUserDetails();
		UserDetails[] userDetails = responseGetUserDetails.getBody();
		try{
			userDetails = userDetailsServices.modifyData(indexNo, value, userDetails);
			
		}catch(RuntimeException e) {
			return new ResponseEntity<Object>(ERROR_MESSAGE + "\n" + e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		ResponseUserDetails res =new ResponseUserDetails();
		res.setUserDetails(Arrays.asList(userDetails));
		
		return new ResponseEntity<Object>(res,HttpStatus.OK);
	}
	
}
