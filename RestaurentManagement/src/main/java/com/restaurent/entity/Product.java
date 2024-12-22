package com.restaurent.entity;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(
	    name = "Product.getAllProduct",
	    query = "SELECT new com.restaurent.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) FROM Product p"
	)

@NamedQuery(
	    name = "Product.updateProductStatus",
	    query = "UPDATE Product p SET p.status = :status WHERE p.id = :id"
	)

@NamedQuery(
	    name = "Product.getProductByCategory",
	    query = "SELECT new com.restaurent.wrapper.ProductWrapper(p.id,p.name) FROM Product p WHERE p.category.id=:id AND p.status='true'"
	)

@NamedQuery(
		name = "Product.getProductById",
		query = "SELECT new com.restaurent.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) FROM Product p WHERE p.id=:id"
		)


@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "product")
public class Product implements Serializable{
	
	private static final long serialVersionUID = 123456L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_fk", nullable = false)
	private Category category;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "price")
	private Integer price;
	
	@Column(name = "status")
	private String status;
}
