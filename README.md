# Money Management System

## Overview
This project is a comprehensive money management system with backend services built using Spring Boot. The system provides robust category management capabilities with parent-child relationships and type-based categorization.

## Features
- **Category Management**: Full CRUD operations for financial categories
- **Hierarchical Structure**: Support for parent-child category relationships
- **Type Classification**: Categories can be marked as income or expense types
- **Validation**: Built-in validation for category data and relationships
- **Pagination & Sorting**: Efficient data retrieval with pagination and sorting

## Technologies
- **Backend**:
  - Java 17
  - Spring Boot 3.x
  - Spring Data MongoDB
  - MapStruct (for DTO-Entity mapping)
- **Database**: MongoDB

## API Endpoints
### Category API
- `GET /api/categories` - Retrieve paginated list of categories
  - Parameters: page, size, sort, direction
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update existing category
- `DELETE /api/categories/{id}` - Delete category

## Setup Instructions
1. **Prerequisites**:
   - Java 17 JDK
   - Maven 3.8+
   - MongoDB 5.0+

2. **Installation**:
   ```bash
   git clone <repository-url>
   cd money-management
   mvn clean install
   ```

3. **Configuration**:
   - Update MongoDB connection in `application.properties` if not using localhost

4. **Running**:
   ```bash
   mvn spring-boot:run
   ```

## Development
### MapStruct Implementation Details
1. **Mapper Interfaces**:
   - Located in `com.example.money.mappers` package
   - Annotated with `@Mapper(componentModel = "spring")`
   - Example:
     ```java
     @Mapper
     public interface CategoryMapper {
         CategoryDTO toDto(Category entity);
         Category toEntity(CategoryDTO dto);
     }
     ```

2. **Usage in Services**:
   - Autowired mapper interfaces
   - Used to convert between DTOs and entities
   - Example:
     ```java
     Category category = categoryMapper.toEntity(categoryDTO);
     ```

3. **Benefits**:
   - Eliminates boilerplate mapping code
   - Compile-time safety (errors caught early)
   - Better performance than reflection-based mappers
   - Easy to maintain and extend
- The project uses MapStruct for efficient DTO-entity mapping
  - MapStruct configuration requires `@Mapper` annotation on interfaces
  - Mapper implementations are generated during compilation
  - Supports complex mappings between DTOs and entities
  - Provides compile-time type safety
  - Includes null checks and default values
- Category validation includes circular reference checks
- MongoDB document structure supports hierarchical categories

## Future Enhancements
- Add user authentication
- Implement budget tracking features
- Add reporting capabilities
