package com.restaurent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.restaurent.entity.User;
import com.restaurent.wrapper.UserWrapper;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmailId(@Param("email")String email);
	
	List<UserWrapper> getAllUser();
	
	// JavaMailSender
	//List<String> getAllAdmin();
	
	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String status, @Param("id") Integer id);
	
	User findByEmail(String email);
}
