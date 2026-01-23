// This file belongs to the DAO layer (Data Access Object layer) 
package com.product.dao; 
// Import List because some methods return multiple products 
import java.util.List; 
// Import our DTO class because DAO works with ProductDTO objects 
import com.product.dto.ProductDTO; 
/* 
* This is an INTERFACE. 
* An interface only declares methods — it does NOT contain implementation. 
* The actual JDBC logic is written in ProductDAOImpl.java. 
*/ 
public interface ProductDAO { 
/* 
* Method to add a new product to the database. 
* Takes a ProductDTO object as input. 
* Implementation is provided in ProductDAOImpl. 
*/ 
void addProduct(ProductDTO p); 
/* 
* Method to fetch all products from the database. 
* Returns a List of ProductDTO objects. 
*/ 
List<ProductDTO> getAllProducts(); 
/* 
* Method to search a product by ID. 
* Returns ProductDTO if found, otherwise returns null. 
*/ 
ProductDTO getProductById(int id); 
/* 
* Method to update the quantity of a product. 
* Takes product ID and new quantity as input. 
*/ 
void updateQuantity(int id, int newQty); 
/* 
* Method to delete a product from the database. 
* Takes product ID as input. 
*/ 
void deleteProduct(int id); 
}