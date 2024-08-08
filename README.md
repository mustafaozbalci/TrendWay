# TrendWay Project

This project consists of two separate applications: **TrendWay** and **Wallet App**. To try this project, you need to pull and run the Wallet App first. After that, you can run the TrendWay application.

## Getting Started

### Prerequisites

1. **Pull and Run Wallet App**:
   - Pull the Wallet App from the relevant repository.
   - Run the application.

2. **Run TrendWay**:
   - Pull the TrendWay application from the relevant repository.
   - Run the application.

### Registration Note

- Users must register with the same username and password in the Wallet App for payments to be processed.

## TrendWay Components

### User

- Represents a user, each of whom can have only one role (e.g., customer or admin).
- Each user can have a shopping cart.
- Users can place multiple orders and make payments for each order.

### Role

- Defines various permissions for users. Users can be classified into different roles, such as customer or admin.

### Company

- Represents companies that produce or sell products.
- Companies can be managed by users.

### Category

- Defines the categories of products. A product can belong to multiple categories.

### Product

- Represents the products being sold. Products can belong to one or more categories and can be produced by a company.
- The stock quantity of products can be updated.

### Cart

- Represents the user's shopping cart. Users can add or remove products from the cart.
- The cart contains the products belonging to the user.

### CartItem

- Represents each product in the cart. There can be multiple products in the cart, and the quantity of each product can be specified.
- If an existing product is added to the cart, its quantity is updated.

### Order

- Represents the orders placed by the user. A user can place multiple orders.
- Orders are tracked with order date and the items (OrderItem) they contain.
- When an order is created, the products in the user's cart are converted to the order, and the cart is emptied.

### OrderItem

- Represents each product in an order. Each OrderItem contains a product (Product) and its quantity.

### Payment

- Represents payments made for orders. Each payment is linked to an order (Order).
- Payment transactions include payment method, amount, payment date, and status information.

## Steps to Use

1. **User and Company Registrations**:
   - Register new users and admins.
   - Register companies and assign them to admins.

2. **Add and Manage Products**:
   - Add products for companies.
   - Update the stock of existing products.

3. **Cart Operations**:
   - Users can add or remove products from their cart.
   - The cart content can be viewed.

4. **Create Orders and Make Payments**:
   - Users can create orders with the products in their cart.
   - Users can make payments for their orders using the same username and password registered in the Wallet App.

These steps and components describe the basic functionality of the TrendWay application and how users can interact with it.
