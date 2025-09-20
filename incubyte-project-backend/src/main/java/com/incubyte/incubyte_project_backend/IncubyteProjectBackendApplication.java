package com.incubyte.incubyte_project_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Incubyte Project Backend.
 * 
 * This is the entry point for the Spring Boot application that manages
 * a sweet shop inventory system. The application provides REST APIs for
 * user authentication, sweet management, inventory tracking, and sales operations.
 * 
 * Key features:
 * - JWT-based authentication and authorization
 * - Role-based access control (USER/ADMIN)
 * - Sweet inventory management
 * - Purchase and restock operations
 * - Advanced search and filtering
 * 
 * @author Incubyte Team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
public class IncubyteProjectBackendApplication {
	
	/**
	 * Main method to start the Spring Boot application.
	 * 
	 * This method initializes the Spring application context and starts
	 * the embedded web server. The application will be available at
	 * http://localhost:8080 by default.
	 * 
	 * @param args Command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(IncubyteProjectBackendApplication.class, args);
	}
}
