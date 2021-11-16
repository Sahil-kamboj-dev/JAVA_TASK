package com.example.demo.controllers.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.UserDetails;
import com.example.demo.model.response.ResponseUserDetails;

@RestController
public class JavaTaskRestController {
	
	public static final String GET_USER_DETAILS_URL= "https://urldefense.com/v3/__http://jsonplaceholder.typicode.com/posts__;!!HPR1fWVfVgYu-HbDXw!IsFOsPRKgZQqdOwOqVQOunDGkE1MQxBPwMQwnQpiY7glTJi_e20UTgOfvhHp-Z2kSpW3$";
	public static final String ERROR_MESSAGE = "Requested Index Out of Bound";
	@Autowired
	private RestTemplate restTemplate;

	private ResponseEntity<UserDetails[]> getUserDetails() {
		ResponseEntity<UserDetails[]> response = restTemplate.exchange(GET_USER_DETAILS_URL, HttpMethod.GET, HttpEntity.EMPTY, UserDetails[].class);
		ResponseEntity<UserDetails[]> responseUserDetails = restTemplate.getForEntity(response.getHeaders().get("Location").get(0), UserDetails[].class);
		return responseUserDetails;
	}
	
/*
 * Count on the basis of distinct method
 * need to override hashcode and equals method
*/
	@GetMapping("/countOfUniqueUserIds")
	public long getCountOfUniqueUserIds() {
		ResponseEntity<UserDetails[]> responseUserDetails = getUserDetails();
		return Stream.of(responseUserDetails.getBody()).distinct().count();
	}

/*
 * Count on the basis of filter method
 * by help of Set
*/
	@GetMapping("/count")
	public long getCount() {
		ResponseEntity<UserDetails[]> responseUserDetails = getUserDetails();
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
		ResponseEntity<UserDetails[]> responseGetUserDetails = getUserDetails();
		UserDetails[] userDetails = responseGetUserDetails.getBody();
		try{
			userDetails = modifyData(indexNo, value, userDetails);
			
		}catch(RuntimeException e) {
			return new ResponseEntity<Object>(ERROR_MESSAGE + "\n" + e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		ResponseUserDetails res =new ResponseUserDetails();
		res.setUserDetails(Arrays.asList(userDetails));
		
		return new ResponseEntity<Object>(res,HttpStatus.OK);
	}

	private UserDetails[] modifyData(Integer indexNo, String value,
			UserDetails[] userDetails) throws RuntimeException{
			userDetails[indexNo].setBody(value);
			userDetails[indexNo].setTitle(value);
		return userDetails;
	}
	
}
