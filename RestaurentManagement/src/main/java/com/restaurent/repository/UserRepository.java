package com.restaurent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.restaurent.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmailId(@Param("email")String email);

}
