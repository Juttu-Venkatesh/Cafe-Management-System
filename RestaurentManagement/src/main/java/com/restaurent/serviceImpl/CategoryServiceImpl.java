package com.restaurent.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.restaurent.constents.RestaurentConstants;
import com.restaurent.entity.Category;
import com.restaurent.jwt.JwtFilter;
import com.restaurent.repository.CategoryDao;
import com.restaurent.service.CategoryService;
import com.restaurent.utility.RestaurentUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	CategoryDao categoryDao;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(requestMap, false)) {
					categoryDao.save(getCategoryFromMap(requestMap, false));
					return RestaurentUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
				}
			}
			else {
				return RestaurentUtils.getResponseEntity(RestaurentConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}
	
	private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
		Category category = new Category();
		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				log.info("Inside if");
				return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
			}
			return new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCatrgory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(requestMap, true)) {
					Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						categoryDao.save(getCategoryFromMap(requestMap, true));
						return RestaurentUtils.getResponseEntity("Category Updated Successfully",
								                                        HttpStatus.OK);
					} else {
						return RestaurentUtils.getResponseEntity("Category id does not exists",HttpStatus.OK);
					}
				}
			} else {
				return RestaurentUtils.getResponseEntity(RestaurentConstants.UNAUTHORIZED_ACCESS,
						                                                     HttpStatus.UNAUTHORIZED);
			}
			return RestaurentUtils.getResponseEntity(RestaurentConstants.INVALID_DATA,
					                                                     HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, 
                                                                     HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
