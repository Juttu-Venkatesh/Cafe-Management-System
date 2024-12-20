package com.restaurent.serviceImpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.restaurent.constents.RestaurentConstants;
import com.restaurent.entity.User;
import com.restaurent.repository.UserRepository;
import com.restaurent.service.UserService;
import com.restaurent.utility.RestaurentUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDao;

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

    /**
     * Validates if the required keys are present in the request map.
     */
    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("phone") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("password");
    }

    /**
     * Converts the request map into a User object.
     */
    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setPhone(requestMap.get("phone"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.getOrDefault("status", "false")); // Default to 'false' if not provided
        user.setRole(requestMap.getOrDefault("role", "user")); // Default to 'user' if not provided
        return user;
    }
}
