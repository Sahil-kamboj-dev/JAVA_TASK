package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.model.UserDetails;

public interface UserDetailsServices {
	public List<UserDetails> getUserDetails();
	public List<UserDetails> modifyData(Integer indexNo, String value,List<UserDetails> userDetails);
	public long countUserIdsByClassNature(List<UserDetails> userDetails);
	public long countUserIdsByOtherLogic(List<UserDetails> userDetails);
}
