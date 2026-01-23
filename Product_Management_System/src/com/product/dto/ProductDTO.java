// This file belongs to the DTO layer (Data Transfer Object layer) 
package com.product.dto; 
// Public class so it can be accessed from other packages 
public class ProductDTO { 
// Private data fields – cannot be accessed directly from other classes 
private int productId;  
private String name;    
    // Unique product ID   
    // Product name   
private String category;    // Product category (e.g., Electronics, Grocery)   
private double price;   
    // Price of the product   
private int quantity;       // Available stock   
// No-argument constructor (empty constructor) 
// Good practice and sometimes required by frameworks 
public ProductDTO() {} 
// Parameterized constructor 
// Used to create a product object with all details at once 
public ProductDTO(int productId, String name, String category, 
double price, int quantity) { 
this.productId = productId;   // Assign parameter to class variable 
this.name = name; 
this.category = category; 
this.price = price; 
this.quantity = quantity; 
} 
// Getter for productId – allows safe reading of value 
public int getProductId() { 
return productId; 
} 
// Setter for productId – allows safe modification 
public void setProductId(int productId) { 
this.productId = productId; 
} 
// Getter for name 
public String getName() { 
return name; 
} 
// Setter for name 
public void setName(String name) { 
this.name = name; 
} 
// Getter for category 
public String getCategory() { 
return category; 
} 
// Setter for category 
public void setCategory(String category) { 
this.category = category; 
} 
// Getter for price 
public double getPrice() { 
return price; 
} 
// Setter for price 
public void setPrice(double price) { 
this.price = price; 
} 
// Getter for quantity 
public int getQuantity() { 
return quantity; 
} 
// Setter for quantity 
public void setQuantity(int quantity) { 
this.quantity = quantity; 
} 
}