-- Şirket tablosu ve verileri
CREATE TABLE IF NOT EXISTS company
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL );

INSERT INTO company (name)
VALUES ('Example Company 1');
INSERT INTO company (name)
VALUES ('Example Company 2');
INSERT INTO company (name)
VALUES ('Example Company 3');

-- Kategori tablosu ve verileri
CREATE TABLE IF NOT EXISTS category
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL );

INSERT INTO category (name)
VALUES ('Electronics');
INSERT INTO category (name)
VALUES ('Home Appliances');
INSERT INTO category (name)
VALUES ('Books');
INSERT INTO category (name)
VALUES ('Fashion');
INSERT INTO category (name)
VALUES ('Toys');

-- Rol tablosu ve verileri
CREATE TABLE IF NOT EXISTS role
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL );

INSERT INTO role (name)
VALUES ('ADMIN'),
       ('USER');

-- Kullanıcı tablosu ve verileri
CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    username
    VARCHAR
(
    255
) NOT NULL, password VARCHAR
(
    255
) NOT NULL, email VARCHAR
(
    255
), full_name VARCHAR
(
    255
), address VARCHAR
(
    255
), phone_number VARCHAR
(
    255
), role_id BIGINT, FOREIGN KEY
(
    role_id
) REFERENCES role
(
    id
) );

INSERT INTO users (username, password, email, full_name, address, phone_number, role_id)
VALUES ('user1', 'password1', 'user1@example.com', 'User One', 'Address 1', '1234567890', 2),
       ('user2', 'password2', 'user2@example.com', 'User Two', 'Address 2', '1234567891', 2),
       ('user3', 'password3', 'user3@example.com', 'User Three', 'Address 3', '1234567892', 2),
       ('user4', 'password4', 'user4@example.com', 'User Four', 'Address 4', '1234567893', 2),
       ('user5', 'password5', 'user5@example.com', 'User Five', 'Address 5', '1234567894', 2);

-- Ürün tablosu ve verileri
CREATE TABLE IF NOT EXISTS product
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL, description TEXT, price DOUBLE, stock INT, company_id BIGINT, FOREIGN KEY
(
    company_id
) REFERENCES company
(
    id
) );

-- Product-Category bağlantı tablosu ve verileri
CREATE TABLE IF NOT EXISTS product_category
(
    product_id
    BIGINT,
    category_id
    BIGINT,
    FOREIGN
    KEY
(
    product_id
) REFERENCES product
(
    id
), FOREIGN KEY
(
    category_id
) REFERENCES category
(
    id
) );

-- Örnek ürünler ve kategoriler
INSERT INTO product (name, description, price, stock, company_id)
VALUES ('Laptop', 'High performance laptop', 1000.0, 10, 1),
       ('Smartphone', 'Latest model smartphone', 700.0, 15, 1),
       ('Blender', 'Powerful kitchen blender', 150.0, 20, 2),
       ('Vacuum Cleaner', 'High suction vacuum cleaner', 300.0, 25, 2),
       ('Novel', 'Bestselling novel', 20.0, 50, 3),
       ('Textbook', 'Educational textbook', 50.0, 30, 3),
       ('T-shirt', 'Comfortable cotton t-shirt', 15.0, 100, 1),
       ('Jeans', 'Stylish blue jeans', 40.0, 75, 2),
       ('Action Figure', 'Collectible action figure', 25.0, 40, 3),
       ('Puzzle', '1000-piece jigsaw puzzle', 10.0, 60, 1);

-- Ürünleri kategorilere bağlama
INSERT INTO product_category (product_id, category_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 3),
       (6, 3),
       (7, 4),
       (8, 4),
       (9, 5),
       (10, 5);

-- Sepet tablosu ve verileri
CREATE TABLE IF NOT EXISTS cart
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    user_id
    BIGINT,
    FOREIGN
    KEY
(
    user_id
) REFERENCES users
(
    id
) );

-- Sepet Öğesi tablosu ve verileri
CREATE TABLE IF NOT EXISTS cart_item
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    cart_id
    BIGINT,
    product_id
    BIGINT,
    quantity
    INT,
    FOREIGN
    KEY
(
    cart_id
) REFERENCES cart
(
    id
), FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
) );

-- Örnek sepet ve sepet öğeleri
INSERT INTO cart (user_id)
VALUES (1),
       (2),
       (3),
       (4),
       (5);

INSERT INTO cart_item (cart_id, product_id, quantity)
VALUES (1, 1, 2),
       (1, 2, 1),
       (2, 3, 4),
       (2, 4, 1),
       (3, 5, 3),
       (3, 6, 2),
       (4, 7, 5),
       (4, 8, 1),
       (5, 9, 2),
       (5, 10, 3);

-- Sipariş tablosu ve verileri
CREATE TABLE IF NOT EXISTS orders
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    user_id
    BIGINT,
    order_date
    TIMESTAMP
    DEFAULT
    CURRENT_TIMESTAMP,
    FOREIGN
    KEY
(
    user_id
) REFERENCES users
(
    id
) );

-- Sipariş Öğesi tablosu ve verileri
CREATE TABLE IF NOT EXISTS order_item
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    order_id
    BIGINT,
    product_id
    BIGINT,
    quantity
    INT,
    price
    DOUBLE,
    FOREIGN
    KEY
(
    order_id
) REFERENCES orders
(
    id
), FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
) );

-- Örnek siparişler ve sipariş öğeleri
INSERT INTO orders (user_id)
VALUES (1),
       (2),
       (3),
       (4),
       (5);

INSERT INTO order_item (order_id, product_id, quantity, price)
VALUES (1, 1, 2, 1000.0),
       (2, 2, 1, 700.0),
       (3, 3, 4, 150.0),
       (4, 4, 1, 300.0),
       (5, 5, 3, 20.0);

-- Ödeme tablosu ve verileri
CREATE TABLE IF NOT EXISTS payment
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    order_id
    BIGINT,
    payment_date
    TIMESTAMP
    DEFAULT
    CURRENT_TIMESTAMP,
    amount
    DOUBLE,
    payment_method
    VARCHAR
(
    255
), status VARCHAR
(
    255
), FOREIGN KEY
(
    order_id
) REFERENCES orders
(
    id
) );

-- Örnek ödemeler
INSERT INTO payment (order_id, amount, payment_method, status)
VALUES (1, 2000.0, 'Credit Card', 'Completed'),
       (2, 700.0, 'PayPal', 'Completed'),
       (3, 600.0, 'Credit Card', 'Completed'),
       (4, 300.0, 'Bank Transfer', 'Pending'),
       (5, 60.0, 'Credit Card', 'Completed');
