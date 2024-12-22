package com.restaurent.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.restaurent.constents.RestaurentConstants;
import com.restaurent.entity.User;
import com.restaurent.jwt.CustomerUserDetailsService;
import com.restaurent.jwt.JwtFilter;
import com.restaurent.jwt.JwtUtil;
import com.restaurent.repository.UserRepository;
import com.restaurent.service.UserService;
//import com.restaurent.utility.EmailUtils;
import com.restaurent.utility.RestaurentUtils;
import com.restaurent.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userDao;

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    
    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    JwtFilter jwtFilter;
    
//    @Autowired
//    EmailUtils emailUtils;
    
    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                // Check if user with given email already exists
                User existingUser = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(existingUser)) {
                    User newUser = getUserFromMap(requestMap);
                    userDao.save(newUser);
                    log.info("User successfully signed up: {}", newUser.getEmail());
                    return RestaurentUtils.getResponseEntity(RestaurentConstants.SUCCESS,
                            HttpStatus.OK);
                } else {
                    log.warn("Email already exists: {}", requestMap.get("email"));
                    return RestaurentUtils.getResponseEntity(RestaurentConstants.EMAIL_EXISTS,
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                log.error("Invalid signup data: {}", requestMap);
                return RestaurentUtils.getResponseEntity(RestaurentConstants.INVALID_DATA,
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error occurred during signup: {}", ex.getMessage(), ex);
            return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("phone") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setPhone(requestMap.get("phone"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.getOrDefault("status", "false"));
        user.setRole(requestMap.getOrDefault("role", "user"));
        return user;
    }

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("Inside login");
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
					);
			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>("{\"token\":\"" +
				           jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
				        		   customerUserDetailsService.getUserDetail().getRole()) +"\"}",
				   HttpStatus.OK);
				}
				else {
					return new ResponseEntity<>("{\"message\":\"Wait for admin approval."+"\"}",
							HttpStatus.BAD_REQUEST);
				}
			}
		}catch (Exception ex) {
			log.error("{}",ex);
		}
		return new ResponseEntity<>("{\"message\":\"Bad Credentials."+"\"}",
				HttpStatus.BAD_REQUEST);

	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
	    try {
	        if (jwtFilter.isAdmin()) {
	            List<UserWrapper> users = userDao.getAllUser();
	            return new ResponseEntity<>(users, HttpStatus.OK);
	        } else {
	            log.warn("Unauthorized access attempt by non-admin user");
	            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.FORBIDDEN);
	        }
	    } catch (Exception ex) {
	        log.error("Error fetching users: {}", ex.getMessage(), ex);
	        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
	    try {
	        if (jwtFilter.isAdmin()) {
	            Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
	            if (optional.isPresent()) {
	                userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
	                //sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
	                return RestaurentUtils.getResponseEntity("User status updated Successfully", HttpStatus.OK);
	            } else {
	                return RestaurentUtils.getResponseEntity("User ID does not exist", HttpStatus.NOT_FOUND);
	            }
	        } else {
	            return RestaurentUtils.getResponseEntity(RestaurentConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public ResponseEntity<String> checkToken() {
		return RestaurentUtils.getResponseEntity("true",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
	    try {
	        User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
	        
	        if (!userObj.equals(null)) {
	            if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
	            	userObj.setPassword(requestMap.get("newPassword"));
	            	userDao.save(userObj);
	            	return RestaurentUtils.getResponseEntity(RestaurentConstants.PASSWORD_UPDATED,HttpStatus.OK);
	            }
	            return RestaurentUtils.getResponseEntity(RestaurentConstants.INCORRECT_OLDPASSWORD,HttpStatus.BAD_REQUEST);
	        }
	        
	        return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
  // JavaMailSender
	
//	public void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
//		
//		allAdmin.remove(jwtFilter.getCurrentUser());
//		if(status != null && status.equalsIgnoreCase("true")) {
//			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", 
//					"USER:- " +user+ "\n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
//		} 
//		else {
//			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", 
//					"USER:- " +user+ "\n is disabled by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
//		}
//	}
//	
	
}
