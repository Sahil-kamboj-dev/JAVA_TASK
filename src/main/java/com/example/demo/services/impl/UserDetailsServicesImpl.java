package com.example.demo.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	@Autowired
	private RestTemplate restTemplate;
	
	public static final String GET_USER_DETAILS_URL= "https://urldefense.com/v3/__http://jsonplaceholder.typicode.com/posts__;!!HPR1fWVfVgYu-HbDXw!IsFOsPRKgZQqdOwOqVQOunDGkE1MQxBPwMQwnQpiY7glTJi_e20UTgOfvhHp-Z2kSpW3$";
	
	@Override
	public List<UserDetails> modifyData(Integer indexNo, String value, List<UserDetails> userDetails) throws RuntimeException {
		UserDetails user = userDetails.get(indexNo);
		user.setBody(value);
		user.setTitle(value);
		return userDetails;
	}

	@Override
	public long countUserIdsByClassNature(List<UserDetails> userDetails) {
		return userDetails.stream().distinct().count();
	}

	@Override
	public long countUserIdsByOtherLogic(List<UserDetails> userDetails) {
		Set<Integer> setOfUserIds = new HashSet<Integer>();
		long result =0;
		if(userDetails != null)
		result = userDetails.stream().filter(e -> {
			if (setOfUserIds.contains(e.getUserId()))
				return false;
			else {
				setOfUserIds.add(e.getUserId());
				return true;
			}
		}).count();
		return result;
	}

	@Override
	public List<UserDetails> getUserDetails() {
		ResponseEntity<Object> response = restTemplate.exchange(GET_USER_DETAILS_URL, HttpMethod.GET, HttpEntity.EMPTY, Object.class);
		ResponseEntity<UserDetails[]> responseUserDetails = restTemplate.getForEntity(response.getHeaders().get("Location").get(0), UserDetails[].class);
		
		List<UserDetails> users = new ArrayList<UserDetails>();
		UserDetails[] userDetails = responseUserDetails.getBody();
		if(userDetails != null && userDetails.length>0 ) {
			users = Arrays.asList(userDetails);
		}
		return users;
	}

}
