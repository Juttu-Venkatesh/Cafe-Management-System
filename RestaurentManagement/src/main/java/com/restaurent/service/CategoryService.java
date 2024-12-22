package com.restaurent.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.restaurent.entity.Category;

public interface CategoryService {

	ResponseEntity<String> addNewCategory(Map<String, String> requestMap);
	
	ResponseEntity<List<Category>> getAllCategory(String filterValue);
	
	ResponseEntity<String> updateCatrgory(Map<String, String> requestMap);
}
