package com.example.demo;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.controllers.rest.UserDetailsRestController;
import com.example.demo.model.UserDetails;
import com.example.demo.model.response.ResponseUserDetails;
import com.example.demo.services.impl.UserDetailsServicesImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserDetailsRestController userDetailsRestController;

	@MockBean
	UserDetailsServicesImpl userDetailsServices;
	
	@Test
	void testCountDefaultMethod() {
		List<UserDetails> users = Stream.of(new UserDetails(1, 1, "title1", "title2"), new UserDetails(1, 1, "title1", "title2"),
				new UserDetails(2, 1, "title1", "title2")).collect(Collectors.toList());
		when(userDetailsServices.getUserDetails()).thenReturn(users);
		when(userDetailsServices.countUserIdsByClassNature(users)).thenCallRealMethod();
		when(userDetailsServices.countUserIdsByOtherLogic(users)).thenCallRealMethod();
		
		assertEquals(2,userDetailsRestController.getCount(null));
		
		verify(userDetailsServices,times(1)).countUserIdsByOtherLogic(users);
		verify(userDetailsServices,times(0)).countUserIdsByClassNature(users);
	}
	
	@Test
	void testCountMethodCallsByCondition() {
		List<UserDetails> users = Stream.of(getMockUserDetailsByUserId(1), getMockUserDetailsByUserId(2),
				getMockUserDetailsByUserId(2),getMockUserDetailsByUserId(3)).collect(Collectors.toList());
		when(userDetailsServices.getUserDetails()).thenReturn(users);
		when(userDetailsServices.countUserIdsByClassNature(users)).thenCallRealMethod();
		when(userDetailsServices.countUserIdsByOtherLogic(users)).thenCallRealMethod();
		
		assertEquals(3,userDetailsRestController.getCount(Optional.of(true)));
		verify(userDetailsServices,times(1)).countUserIdsByClassNature(users);
		
		assertEquals(3,userDetailsRestController.getCount(Optional.of(false)));
		verify(userDetailsServices,times(1)).countUserIdsByOtherLogic(users);
		
	}
	
	@Test
	void testModifyUserDetails() {
		List<UserDetails> users = Stream.of(getMockUserDetailsByUserId(1), getMockUserDetailsByUserId(2),
				getMockUserDetailsByUserId(2),getMockUserDetailsByUserId(3)).collect(Collectors.toList());
		
		when(userDetailsServices.getUserDetails()).thenReturn(users);
		when(userDetailsServices.modifyData(2, "sahilTest", users)).thenCallRealMethod();
		when(userDetailsServices.countUserIdsByOtherLogic(users)).thenCallRealMethod();
		
		ResponseEntity<ResponseUserDetails> res = (ResponseEntity<ResponseUserDetails>) userDetailsRestController
				.getModifiedData(2, "sahilTest");
		Collection<UserDetails> userDetailsList = (Collection<UserDetails>) res.getBody().getUserDetails();
				UserDetails userDetails = (UserDetails) userDetailsList.toArray()[2];
		
		assertEquals("sahilTest",userDetails.getBody());
		assertEquals("sahilTest",userDetails.getTitle());
		assertEquals(4,userDetailsList.size());
		verify(userDetailsServices,times(1)).modifyData(2, "sahilTest", users);
		
	}
	
	@Test()
	void testModifyUserDetailsBadRequestScenario() {
		
		List<UserDetails> users = Stream.of(getMockUserDetailsByUserId(1), getMockUserDetailsByUserId(2),
				getMockUserDetailsByUserId(2),getMockUserDetailsByUserId(3)).collect(Collectors.toList());
		
		when(userDetailsServices.getUserDetails()).thenReturn(users);
		when(userDetailsServices.modifyData(users.size(), "sahilTest", users)).thenCallRealMethod();
		when(userDetailsServices.modifyData(-1, "sahilTest", users)).thenCallRealMethod();
		when(userDetailsServices.countUserIdsByOtherLogic(users)).thenCallRealMethod();
		
		ResponseEntity<Object> res1 = (ResponseEntity<Object>) userDetailsRestController
				.getModifiedData(-1, "sahilTest");
		
		
		assertEquals(true,res1.getBody().toString().startsWith("Requested Index Out of Bound"));
		assertEquals(true,res1.getStatusCode().equals(HttpStatus.BAD_REQUEST));
		
		
		verify(userDetailsServices,times(1)).modifyData(-1, "sahilTest", users);
		
		ResponseEntity<Object> res2 = (ResponseEntity<Object>) userDetailsRestController
				.getModifiedData(users.size(), "sahilTest");
		
		assertEquals(true,res2.getBody().toString().startsWith("Requested Index Out of Bound"));
		assertEquals(true,res2.getStatusCode().equals(HttpStatus.BAD_REQUEST));
		
		verify(userDetailsServices,times(1)).modifyData(users.size(), "sahilTest", users);
		
	}

	
	private UserDetails getMockUserDetailsByUserId(int userId) {
		return new UserDetails(userId, 1, "title1", "title2");
	}

}
