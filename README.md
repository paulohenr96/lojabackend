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
| DTO  | Description | Fields |
| ------------- |:-------------:|:-------------:|
|  ProductDTO    |Information about the product entity     |id,name,quantity,price,category     |
| ProductSaleDTO   | Information about Product that was sold    | id,productId,productName,quantity    |
| SaleDTO | Information about Sale     |Id,products,buyer,date,totalPrice,owner     |
| UserAccountDTO |Information about the entity UserAccount     |Id,userName,name,password,monthlyGoal,rolesName     |
| TokenDTO     | When the user send the right credentials (username and password) the api send back informations like the Token |roles,issuedDate,subject,fullToken     |
| LoginDTO      | Credentials used to login (username and password)   |username,password    |
| InfoDTO      |  Some general information, like the number of different products on stock and the total quantity   |income,totalProduct,sumQuantity    |
| ChartDTO      | Informations to create the bar chart   |xAxis,yAxis    |

# Repositories
| Repository  | Model |
| ------------- |:-------------:|
|  ProductRepository    |Product        |
| ProductSaleRepository   |ProductSale   |
| RoleRepository | Role     |
| SaleRepository |Sale        |
| UserAccountRepository     | UserAccount |

# Services
| Service  | Dependencies | Description|
| ------------- |:-------------:|:-------------:|
|  ProductService    |    ProductRepository, SaleRepository,ModelMapper     | Interface between ProductController and the ProductRepository |
| SaleService   |SaleRepository, ProductRepository and ModelMapper   | Interface between the SaleController and the SaleRepository|


# Controllers
| Controller  | Dependencies | Description|
| ------------- |:-------------:|:-------------:|
|  LoginController    |   UserAccountRepository     | Operations related to login and logout  |
| ProductController   |ProductService   | Call the ProductService according to the request|
| SaleController   |SaleService   |Call the SaleService according to the request|
| UserController   |ModelMapper,UserAccountRepository,RoleRepository,MetricRepository| Crud operations related to the user, and also configure the metrics|

# Security

| Class  |  Description|
| ------------- |:-------------:|
|  WebSecurityConfig    |   General spring security configuration       |
| JWTCreator   | Create new Token or check if the token is valid  |
| FilterAuthentication   | Filter that checks if the client send the token inside the header **Authorization**  |

# CrossOrigin
Inside every controller it was written the annotation below
```
@CrossOrigin(origins ="http://localhost:4200/")

```


