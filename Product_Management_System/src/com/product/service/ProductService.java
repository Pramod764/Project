// This file belongs to the Service layer (Business Logic layer) 
package com.product.service; 
// Import List because this class may return multiple products 
import java.util.List; 
// Import DAO and DTO classes used by this service 
import com.product.dao.ProductDAO; 
import com.product.dao.ProductDAOimpl; 
import com.product.dto.ProductDTO; 
/* 
* This class is the BUSINESS LOGIC layer. 
* It sits between: 
*   
*   
* - ProductApp (console/UI layer) - ProductDAOImpl (database layer) 
* It validates data before sending it to the database. 
*/ 
public class ProductService { 
// Creating object of DAO implementation 
// Service NEVER talks to database directly — it always goes through DAO 
private ProductDAO dao = new ProductDAOimpl(); 
/* 
* Method to add a new product 
* This is where validation happens before saving to database 
*/ 
public void addProduct(ProductDTO p) { 
// Validate product ID 
if (p.getProductId() <= 0) { 
System.out.println("Invalid Product ID!"); 
return;   // Stop execution if ID is invalid 
} 
// Validate price 
if (p.getPrice() <= 0) { 
System.out.println("Price must be positive!"); 
return;   // Stop execution if price is invalid 
} 
// Validate quantity 
if (p.getQuantity() < 0) { 
System.out.println("Quantity cannot be negative!"); 
return; 
} 
// Prevent duplicate product ID 
if (dao.getProductById(p.getProductId()) != null) { 
System.out.println("Error: Product ID already exists!"); 
return; 
} 
// If all validations pass, send data to DAO layer 
dao.addProduct(p); 
} 
/* 
* Method to fetch all products 
* Service simply calls DAO and returns result 
*/ 
public List<ProductDTO> getAllProducts() { 
return dao.getAllProducts(); 
} 
/* 
* Method to search product by ID 
* Returns ProductDTO or null 
*/ 
public ProductDTO getProductById(int id) { 
return dao.getProductById(id); 
} 
/* 
* Method to update product quantity 
* Calls DAO layer to update in database 
*/ 
public void updateQuantity(int id, int qty) { 
// Basic validation 
if (qty < 0) { 
System.out.println("Quantity cannot be negative!"); 
return; 
} 
dao.updateQuantity(id, qty); 
} 
/* 
* Method to delete product 
* Directly calls DAO layer 
*/ 
public void deleteProduct(int id) { 
dao.deleteProduct(id); 
} 
}
