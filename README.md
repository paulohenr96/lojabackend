# Description
Rest API for an Inventory Management System
# Dependencies
| Dependency  | Description |
| ------------- |:-------------:|
|  Jwt     | Authentication     |
| Postgres   9.4.1212 | Database     |
| ModelMapper 3.1.0| Map dto to entity and vice-versa     |
| Spring DevTools | Automatic restart     |
| Spring JPA Data      | Communication with the database     |
| Spring Security      | Authentication, protect the endpoints and etc    |
| Spring Starter Web      | Tools useful to build the web application like Tomcat     |

# Repositories

# Services

# Controllers

# Security

# Entities
| Entity  | Description | Fields |
| ------------- |:-------------:|:-------------:|
|  Product     | Entity that represent the product on the inventory     |id,name,quantity,price,category     |
| ProductSale   | Product that was sold    | id,product (many to one),quantity    |
| Sale | Entity that represent a sale     |Id,products(one to many),buyer,date,totalPrice,owner     |
| UserAccount | Entity that represent the user account     |Id,name,userName,password,monthlyGoal,roles (many to many)     |
| Role     | Diferent Roles the user can have     |Id,name     |
| Metrics      | Metrics of the system    |Id,monthlyGoal     |


# DTO

# Chart
