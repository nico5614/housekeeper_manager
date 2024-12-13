package org.example.housekeeping_manager;

// Importing necessary Spring Boot classes
import org.springframework.boot.SpringApplication; // Used to bootstrap the application
import org.springframework.boot.autoconfigure.SpringBootApplication; // Marks this class as a Spring Boot application (So Springboot knows this is the main entry point)
// it knows that this class is defined to use Spring Beans (Objects from Springboot)

// Annotation to enable Spring Boot auto-configuration (detects dependencies) and component scanning
@SpringBootApplication
public class manager_application {
    public static void main(String[] args) {
        // Launches the Spring Boot application
        SpringApplication.run(manager_application.class, args);
    }
}

/**
 * The `manager_application` class serves as the entry point for the Spring Boot application.
 *
 * Key Details:
 * - `@SpringBootApplication`:
 *   - Marks this class as the main configuration and startup class.
 *   - Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
 *   - Automatically sets up components, web servers, and dependencies.
 *
 * - How It Works:
 *   1. The `main` method runs the application via `SpringApplication.run()`.
 *   2. Spring Boot scans for components (e.g., @Route, @Controller, @Service) in the package.
 *   3. Initializes the Vaadin GUI (`manager_gui`) and integrates the embedded Tomcat server.
 *   4. Handles database interactions via `manager_functions` and `database_connection`.
 *
 * - Purpose:
 *   - Centralizes application startup and configuration.
 *   - Ensures seamless integration between the GUI, backend logic, and database.
 */