
# TrendWay Project

This project consists of two separate applications: **TrendWay** and **Wallet App**(https://github.com/mustafaozbalci/Wallet). To try this project, you need to pull and run the Wallet App first. After that, you can run the TrendWay application.

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

## Predefined Data

The system comes with predefined data for quick testing and demonstration purposes.

### Users

| ID    | Username | Password | Role  | Email            |
|-------|----------|----------|-------|------------------|
| 10001 | user     | pu       | USER  | user@example.com |
| 10002 | admin    | pc       | ADMIN | admin@example.com|

### Company

| ID    | Name            | Wallet ID | User ID |
|-------|-----------------|-----------|---------|
| 10001 | Example Company | 10001     | 10002   |

### Products

| ID    | Name        | Description                | Price | Stock | Company ID |
|-------|-------------|----------------------------|-------|-------|------------|
| 10001 | Product One | Description for Product One| 100.0 | 50    | 10001      |
| 10002 | Product Two | Description for Product Two| 200.0 | 30    | 10001      |

## Steps to Use

### 1. Cart Operations

#### Add to Cart

1. **Endpoint**: `POST /carts/add`
2. **Headers**:
   - `payerUsername: user`
   - `payerPassword: pu`
3. **Body**:
   ```json
   {
       "userId": 10001,
       "productId": 10001,
       "quantity": 2
   }
   ```

#### Remove From Cart

1. **Endpoint**: `POST /carts/remove`
2. **Headers**:
   - `payerUsername: user`
   - `payerPassword: pu`
3. **Body**:
   ```json
   {
       "userId": 10001,
       "productId": 10001,
       "quantity": 2
   }
   ```

#### View Cart

1. **Endpoint**: `POST /carts/view`
2. **Headers**:
   - `payerUsername: user`
   - `payerPassword: pu`
3. **Body**:
   ```json
   {
       "userId": 10001
   }
   ```

### 2. Create Orders

#### Create Order

1. **Endpoint**: `POST /orders/create`
2. **Headers**:
   - `payerUsername: user`
   - `payerPassword: pu`
3. **Body**:
   ```json
   {
       "userId": 10001
   }
   ```

### 3. Make Payments

#### Process Payment

1. **Endpoint**: `POST /payments/process`
2. **Headers**:
   - `payerUsername: user`
   - `payerPassword: pu`
3. **Body**:
   ```json
   {
       "orderId": 1
   }
   ```

## Running the Applications

### Wallet Application:

- Ensure that the Wallet application is running on port 8081.

### TrendWay Application:

- Ensure that the TrendWay application is running on port 8080.

## Example Workflow

### User and Company Registrations:

- Users and admin are predefined as shown above.

### Add Products:

- Products are predefined as shown above.

### User Adds Products to Cart:

- Use the `/carts/add` endpoint to add products to the cart.

### Create an Order:

- Use the `/orders/create` endpoint to create an order with the products in the cart.

### Process the Payment:

- Use the `/payments/process` endpoint to process the payment for the created order.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
