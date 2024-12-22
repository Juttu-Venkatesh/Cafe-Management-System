package com.restaurent.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWrapper {

    private Integer id;          // Product ID
    private String name;         // Product Name
    private String description;  // Product Description
    private Integer price;       // Product Price
    private String status;       // Product Status
    private Integer categoryId;  // Category ID
    private String categoryName; // Category Name
    
    public ProductWrapper(Integer id, String name) {
        this.id=id;
        this.name=name;
    }

    public ProductWrapper(Integer id, String name, String description, Integer price) {
    	this.id=id;
    	this.name=name;
    	this.description=description;
    	this.price=price;
    }
}
