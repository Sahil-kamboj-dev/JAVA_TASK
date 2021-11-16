package com.example.demo.services.interfaces;

import org.springframework.http.ResponseEntity;

import com.example.demo.model.UserDetails;

public interface UserDetailsServices {

	public ResponseEntity<UserDetails[]> getUserDetails();
	public UserDetails[] modifyData(Integer indexNo, String value,UserDetails[] userDetails);
}
