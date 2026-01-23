// This file belongs to the DAO layer (Database layer)
	package com.product.dao;
	
	// Import required JDBC classes
	import java.sql.Connection;       // Represents a connection to MySQL
	import java.sql.PreparedStatement;// Used for safe parameterized SQL queries
	import java.sql.ResultSet;        // Used to read data returned from database
	import java.sql.SQLException;     // Handles SQL errors
	import java.sql.Statement;        // Used for simple SQL queries
	import java.util.ArrayList;       // Used to store multiple products
	import java.util.List;            // List interface for collections
	
	// Import our DTO and DBConnection classes
	import com.product.dto.ProductDTO;
	import com.product.util.DBConnection;
	
	// This class implements the ProductDAO interface
	// That means it must provide implementation for all its methods
	public class ProductDAOimpl implements ProductDAO {
	
	    // ---------- ADD PRODUCT ----------
	    @Override
	    public void addProduct(ProductDTO p) {
	
	        // SQL query to insert product into database
	        String sql = "INSERT INTO products VALUES (?,?,?,?,?)";
	
	        // try-with-resources automatically closes connection and statement
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	
	            // Setting values in the correct order of table columns
	            ps.setInt(1, p.getProductId());   // product_id
	            ps.setString(2, p.getName());     // name
	            ps.setString(3, p.getCategory()); // category
	            ps.setDouble(4, p.getPrice());    // price
	            ps.setInt(5, p.getQuantity());    // quantity
	
	            // Execute the insert query
	            ps.executeUpdate();
	
	            System.out.println("Product added successfully!");
	
	        } catch (SQLException e) {
	            // If error occurs, print message
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	
	    // ---------- VIEW ALL PRODUCTS ----------
	    @Override
	    public List<ProductDTO> getAllProducts() {
	
	        // Create empty list to store products
	        List<ProductDTO> list = new ArrayList<>();
	
	        // SQL query to fetch all records
	        String sql = "SELECT * FROM products";
	
	        try (Connection con = DBConnection.getConnection();
	             Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {
	
	            // Loop through all rows returned by database
	            while (rs.next()) {
	
	                // Create ProductDTO object for each row
	                ProductDTO p = new ProductDTO(
	                        rs.getInt("product_id"),
	                        rs.getString("name"),
	                        rs.getString("category"),
	                        rs.getDouble("price"),
	                        rs.getInt("quantity")
	                );
	
	                // Add product object to list
	                list.add(p);
	            }
	
	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	
	        // Return list of products
	        return list;
	    }
	
	    // ---------- SEARCH PRODUCT BY ID ----------
	    @Override
	    public ProductDTO getProductById(int id) {
	
	        // SQL query with placeholder ?
	        String sql = "SELECT * FROM products WHERE product_id=?";
	
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	
	            // Set product ID in query
	            ps.setInt(1, id);
	
	            // Execute query
	            ResultSet rs = ps.executeQuery();
	
	            // If record exists
	            if (rs.next()) {
	
	                // Create and return ProductDTO object
	                return new ProductDTO(
	                        rs.getInt("product_id"),
	                        rs.getString("name"),
	                        rs.getString("category"),
	                        rs.getDouble("price"),
	                        rs.getInt("quantity")
	                );
	            }
	
	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	
	        // If product not found, return null
	        return null;
	    }
	
	    // ---------- UPDATE QUANTITY ----------
	    @Override
	    public void updateQuantity(int id, int newQty) {
	
	        // SQL query to update quantity
	        String sql = "UPDATE products SET quantity=? WHERE product_id=?";
	
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	
	            // Set new quantity
	            ps.setInt(1, newQty);
	
	            // Set product ID
	            ps.setInt(2, id);
	
	            // Execute update and get affected rows count
	            int rows = ps.executeUpdate();
	
	            if (rows > 0)
	                System.out.println("Quantity updated!");
	            else
	                System.out.println("Product not found!");
	
	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	
	    // ---------- DELETE PRODUCT ----------
	    @Override
	    public void deleteProduct(int id) {
	
	        // SQL query to delete record
	        String sql = "DELETE FROM products WHERE product_id=?";
	
	        try (Connection con = DBConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	
	            // Set product ID
	            ps.setInt(1, id);
	
	            // Execute delete
	            int rows = ps.executeUpdate();
	
	            if (rows > 0)
	                System.out.println("Product deleted!");
	            else
	                System.out.println("Product not found!");
	
	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	}