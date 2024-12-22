package com.restaurent.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.restaurent.constents.RestaurentConstants;
import com.restaurent.entity.Category;
import com.restaurent.entity.Product;
import com.restaurent.jwt.JwtFilter;
import com.restaurent.repository.ProductDao;
import com.restaurent.service.ProductService;
import com.restaurent.utility.RestaurentUtils;
import com.restaurent.wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDao productDao;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
	    try {
	        if (jwtFilter.isAdmin()) {
	            if (validateProductMap(requestMap, false)) {
	                productDao.save(getProductFromMap(requestMap, false));
	                return RestaurentUtils.getResponseEntity("Product Added Successfully", HttpStatus.OK);
	            }
	            return RestaurentUtils.getResponseEntity(RestaurentConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
	        } else {
	            return RestaurentUtils.getResponseEntity(RestaurentConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
	    // Validate mandatory fields
	    if (requestMap.containsKey("name") &&
	        requestMap.containsKey("categoryId") &&
	        requestMap.containsKey("description") &&
	        requestMap.containsKey("price")) {

	        // If validating ID, ensure it is also present
	        if (validateId) {
	            return requestMap.containsKey("id");
	        }
	        return true;
	    }
	    return false;
	}

	
	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
	    Category category = new Category();
	    category.setId(Integer.parseInt(requestMap.get("categoryId")));
	    
	    Product product = new Product();
	    if (isAdd && requestMap.containsKey("id")) {
	        product.setId(Integer.parseInt(requestMap.get("id")));
	    } else {
	        product.setStatus("true"); // Default status
	    }
	    product.setCategory(category);
	    product.setName(requestMap.get("name"));
	    product.setDescription(requestMap.get("description"));
	    product.setPrice(Integer.parseInt(requestMap.get("price")));
	    return product;
	}



	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, true)) {
					Optional<Product> optional= productDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						Product product  = getProductFromMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						return RestaurentUtils.getResponseEntity("Product Updated Successfully",
								                                                                HttpStatus.OK);
					} else {
						return RestaurentUtils.getResponseEntity("Product id does not exists",
								                                                             HttpStatus.OK);
					}
				} else {
					return RestaurentUtils.getResponseEntity(RestaurentConstants.INVALID_DATA, 
							                                                     HttpStatus.BAD_REQUEST);
				}
				
			} else {
				return RestaurentUtils.getResponseEntity(RestaurentConstants.UNAUTHORIZED_ACCESS, 
						                                                     HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, 
				                                                     HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(id);
				if (!optional.isEmpty()) {
					productDao.deleteById(id);
					return RestaurentUtils.getResponseEntity("Product Deleted Successfully",
							                                                               HttpStatus.OK);
				}
				return RestaurentUtils.getResponseEntity("Product id does not exists",
                                                                                     HttpStatus.OK);
			} else {
				return RestaurentUtils.getResponseEntity(RestaurentConstants.UNAUTHORIZED_ACCESS, 
                                                                             HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, 
                                                                     HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					return RestaurentUtils.getResponseEntity("Product status updated successfully",
                                                                                          HttpStatus.OK);
				} else {
					return RestaurentUtils.getResponseEntity("Product id does not exists",
                                                                                         HttpStatus.OK);
				}
			} else {
				return RestaurentUtils.getResponseEntity(RestaurentConstants.UNAUTHORIZED_ACCESS, 
                                                                             HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return RestaurentUtils.getResponseEntity(RestaurentConstants.SOMETHING_WENT_WRONG, 
                                                                     HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
	    try {
	        return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
	}



}
