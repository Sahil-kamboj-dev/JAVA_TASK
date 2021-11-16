package com.example.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.UserDetails;
import com.example.demo.services.interfaces.UserDetailsServices;

@Service
public class UserDetailsServicesImpl implements UserDetailsServices{
	public static final String GET_USER_DETAILS_URL= "https://urldefense.com/v3/__http://jsonplaceholder.typicode.com/posts__;!!HPR1fWVfVgYu-HbDXw!IsFOsPRKgZQqdOwOqVQOunDGkE1MQxBPwMQwnQpiY7glTJi_e20UTgOfvhHp-Z2kSpW3$";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public ResponseEntity<UserDetails[]> getUserDetails() {
		ResponseEntity<UserDetails[]> response = restTemplate.exchange(GET_USER_DETAILS_URL, HttpMethod.GET, HttpEntity.EMPTY, UserDetails[].class);
		ResponseEntity<UserDetails[]> responseUserDetails = restTemplate.getForEntity(response.getHeaders().get("Location").get(0), UserDetails[].class);
		return responseUserDetails;
	}
	
	@Override
	public UserDetails[] modifyData(Integer indexNo, String value, UserDetails[] userDetails) throws RuntimeException {
		userDetails[indexNo].setBody(value);
		userDetails[indexNo].setTitle(value);
		return userDetails;
	}

}
