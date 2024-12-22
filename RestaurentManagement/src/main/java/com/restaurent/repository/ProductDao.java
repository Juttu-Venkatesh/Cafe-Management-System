package com.restaurent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.restaurent.entity.Product;
import com.restaurent.wrapper.ProductWrapper;

import jakarta.transaction.Transactional;

public interface ProductDao extends JpaRepository<Product, Integer> {
    
    List<ProductWrapper> getAllProduct(); // Spring Data JPA will use the @NamedQuery

    @Modifying
    @Transactional
	Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

	List<ProductWrapper> getProductByCategory(@Param("id") Integer id);
	
	ProductWrapper getProductById(@Param("id") Integer id);
}
