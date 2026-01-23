// This file belongs to the main (UI / Console) layer 
package com.product.main; 
// Scanner is used to take input from the user 
import java.util.List; 
import java.util.Scanner; 
// Import DTO and Service classes 
import com.product.dto.ProductDTO; 
import com.product.service.ProductService; 
/* 
* This is the MAIN class of the Product Management System. 
* Responsibilities: 
*  - Display menu 
*  - Take user input 
*  - Show output 
* 
* It does NOT contain database or SQL code. 
*/ 
public class ProductApp { 
public static void main(String[] args) { 
// Scanner object to read input from keyboard 
Scanner sc = new Scanner(System.in); 
// Creating Service layer object 
// Main class talks only to Service layer 
ProductService service = new ProductService(); 
// Infinite loop to keep program running until Exit is chosen 
while (true) { 
// Display menu options 
System.out.println("\n===== PRODUCT MANAGEMENT SYSTEM ====="); 
System.out.println("1. Add Product"); 
System.out.println("2. View All Products"); 
System.out.println("3. Search Product by ID"); 
System.out.println("4. Update Quantity"); 
System.out.println("5. Delete Product"); 
System.out.println("6. Exit"); 
System.out.print("Choice: "); 
// Read user's menu choice 
int choice = sc.nextInt(); 
switch (choice) { 
// -------- CASE 1: ADD PRODUCT -------- 
case 1: 
System.out.print("Product ID: "); 
int id = sc.nextInt(); 
sc.nextLine(); // consume leftover newline 
// Check duplicate ID immediately 
if (service.getProductById(id) != null) { 
System.out.println("Error: Product ID already exists!"); 
break; // stop and return to menu 
} 
System.out.print("Name: "); 
String name = sc.nextLine(); 
System.out.print("Category: "); 
String category = sc.nextLine(); 
System.out.print("Price: "); 
double price = sc.nextDouble(); 
System.out.print("Quantity: "); 
int qty = sc.nextInt(); 
// Create ProductDTO object using input 
ProductDTO p = new ProductDTO(id, name, category, price, qty); 
// Send product data to Service layer 
service.addProduct(p); 
break; 
// -------- CASE 2: VIEW ALL PRODUCTS -------- 
case 2: 
// Fetch all products from Service layer 
List<ProductDTO> list = service.getAllProducts(); 
// Print table header 
System.out.println("\nID | NAME | CATEGORY | PRICE | QUANTITY"); 
System.out.println("-------------------------------------------"); 
                    // Loop through list and print each product 
                    for (ProductDTO x : list) { 
                        System.out.println( 
                                x.getProductId() + " | " + 
                                x.getName() + " | " + 
                                x.getCategory() + " | " + 
                                x.getPrice() + " | " + 
                                x.getQuantity() 
                        ); 
                    } 
                    break; 
 
                // -------- CASE 3: SEARCH PRODUCT BY ID -------- 
                case 3: 
                    System.out.print("Enter Product ID: "); 
                    int sid = sc.nextInt(); 
 
                    // Search product using Service layer 
                    ProductDTO prod = service.getProductById(sid); 
 
                    if (prod != null) 
                        System.out.println( 
                                prod.getName() + " | Price: " + 
                                prod.getPrice() + " | Qty: " + 
                                prod.getQuantity() 
                        ); 
                    else 
                        System.out.println("Product not found!"); 
                    break; 
 
// -------- CASE 4: UPDATE QUANTITY -------- 
case 4: 
System.out.print("Enter Product ID: "); 
int uid = sc.nextInt(); 
System.out.print("New Quantity: "); 
int newQty = sc.nextInt(); 
// Update quantity using Service layer 
service.updateQuantity(uid, newQty); 
break; 
// -------- CASE 5: DELETE PRODUCT -------- 
case 5: 
System.out.print("Enter Product ID to delete: "); 
int did = sc.nextInt(); 
// Delete product using Service layer 
service.deleteProduct(did); 
break; 
// -------- CASE 6: EXIT -------- 
case 6: 
System.out.println("Goodbye!"); 
sc.close(); // close scanner to avoid memory leak 
return;    // stop program completely 
// Handle invalid choice 
default: 
System.out.println("Invalid choice!"); 
            } 
        } 
    } 
}