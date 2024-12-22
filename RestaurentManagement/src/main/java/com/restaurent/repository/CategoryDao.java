package com.restaurent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurent.entity.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

	List<Category> getAllCategory();
	
}
