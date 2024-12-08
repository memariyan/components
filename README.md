# Component Library Project

## Introduction

In complex systems with multiple microservices, managing shared functionality across services can become cumbersome.
Repeated implementations of logging, monitoring, and security utilities lead to duplicated efforts and increased
maintenance overhead.

This project introduces a **Component Library** designed to address such challenges by providing reusable, modular
components that can be seamlessly integrated into various microservices. For example, rather than implementing search
system separately for each of your 10+ microservices, this library offers a centralized **search module** that injects
the functionality into your services without redundant code.

The project aims to streamline concerns like test, metrics, security, and more into reusable modules.

---

## Design

The **Component Library** is structured as a parent-child Maven project, with isolated modules for specific
functionalities. Each module can be individually included in your microservices based on your requirements.

### Modules Overview

1. **Common**  
   Contains shared classes, such as common DTOs (Request/Response objects) and validator annotations.

2. **Metric**  
   Implements monitoring and business metrics infrastructure. Includes logging integration and external log shipping via
   an AOP-based approach.

3. **Test**  
   Provides tools for structured testing, such as Docker test containers and reusable test annotations.

4. **Mongo**  
   Houses MongoDB base classes and preconfigured object converters for streamlined MongoDB operations.

5. **Jpa**  
   Includes Jpa base classes and utilities for relational database operations.

6. **Security**  
   Centralizes security utilities, authentication tools, and shared security configurations.

7. **Search**  
   Offers a unified framework for search models, handling various search query types and supporting multiple storage
   backends.

8. **User**  
   Handles user management functions such as registration, profiling, administration and all related user operations,
   this module is designed according to AAA concepts

8. **Scheduling**  
      Manages global tasks and distributed scheduling by acquiring distributed locks

---

## Key Features

- Modular design with isolated modules for better maintainability and reusability.
- Parent-child Maven structure for efficient dependency management.
- Extensive use of Spring conditional beans for dynamic dependency resolution.
- Simplified integration of cross-cutting concerns into microservices.
- Support for dynamic dependency management via Maven scopes.

---

## Getting Started

### Prerequisites

- **Java 21+**
- **Spring 3.2+**

### Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/memariyan/components.git
   cd component-library
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Include desired modules in your microservice's `pom.xml`:
   ```xml
   <dependencyManagement>
       <dependencies>
           <dependency>
               <groupId>com.memariyan.components</groupId>
               <artifactId>components</artifactId>
               <version>${component.version}</version>
               <type>pom</type>
               <scope>import</scope>
           </dependency>
       </dependencies>
   </dependencyManagement>   

   <dependencies>
       <dependency>
           <groupId>com.memariyan.components</groupId>
           <artifactId>test</artifactId>
           <scope>test</scope>
       </dependency>
       <dependency>
           <groupId>com.memariyan.components</groupId>
           <artifactId>jpa</artifactId>
       </dependency>
   </dependencies>
   ```

---
