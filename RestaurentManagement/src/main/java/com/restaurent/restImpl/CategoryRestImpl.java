package com.restaurent.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.restaurent.constents.RestaurentConstants;
import com.restaurent.entity.Category;
import com.restaurent.rest.CategoryRest;
import com.restaurent.service.CategoryService;
import com.restaurent.utility.RestaurentUtils;

@RestController
public class CategoryRestImpl implements CategoryRest {

	@Autowired
	CategoryService categoryService;
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			return categoryService.addNewCategory(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, 
				                                                     HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			return categoryService.getAllCategory(filterValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCatrgory(Map<String, String> requestMap) {
		try {
			return categoryService.updateCatrgory(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, 
                                                                     HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
