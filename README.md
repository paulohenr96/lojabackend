# DESCRIPTION
Front end web application that consumes an api to manage the inventory.
## CLASSES

 | Classes  | Description |
| ------------- |:-------------:|
| Login      | Send the login to the api     |
| Metrics      | The object that contains all the general metrics, like monthly sale goals     |
| PageProduct      | Page object used show the list of products on inventory with pagination     |
| Product      | Product in the inventory     |
| ProductSale      | The object that is going to have all the information about the product that is going to be added into the sale (id and quantity)    |
| Sale      | Receive or send sale data to the API     |
| SaleChart      | Receive the data to create the bar chart     |
| User      | The object that represent the accounts. The user can have the role of 'admin' or the role of 'user'     |

 
 
## PAGES
| Page  | Description |
| ------------- |:-------------:|
| Home      | Home page. If the user logged in is an admin he will see the total monthly sales of every salesman and he will be able to see the the bar chart with the sales of every month    |
| Login      | Page used to login. If the login is successful the user goes to the home page    |
| Metrics      | This page is used to edit the metrics    |
| New Product     | Page used to save new products, or to edit products that already exist.     |
| Products      | Page used to see a table with all the products in the inventory    |
| Sales      | Page with a table that shows all the sales     |
| Sales Form      | Page used to registry a new sale.    |
| Users      | Page that shows all the users     |
| UserForm      | Page used to save new users     |

## COMPONENTS

## SERVICES

## GUARD

## INTERCEPTOR
