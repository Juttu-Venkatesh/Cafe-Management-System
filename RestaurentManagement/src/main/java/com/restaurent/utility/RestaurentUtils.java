package com.restaurent.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestaurentUtils {
	
	private RestaurentUtils() {
		
	}
	
	 public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
	        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
	    }}
