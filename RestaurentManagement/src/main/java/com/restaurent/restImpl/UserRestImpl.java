package com.restaurent.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.restaurent.constents.RestaurentConstants;
import com.restaurent.rest.UserRest;
import com.restaurent.service.UserService;
import com.restaurent.utility.RestaurentUtils;

@RestController
public class UserRestImpl implements UserRest {
	
	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<String> signup(Map<String, String> requestMap) {
		try {
			return userService.signup(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
