package com.example.demo.model.response;

import java.util.Collection;

import com.example.demo.model.UserDetails;

public class ResponseUserDetails{
	
	private Collection<UserDetails> userDetails;


	public Collection<UserDetails> getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(Collection<UserDetails> userDetails) {
		this.userDetails = userDetails;
	}
	
	
}
