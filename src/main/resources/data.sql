-- Tüm tabloları düşür
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS company;

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
) NOT NULL,
    wallet_id BIGINT
    );

INSERT INTO company (id, name, wallet_id)
VALUES (9999999, 'Example Company 1', 100006),
       (9999998, 'Example Company 2', 100007),
       (9999997, 'Example Company 3', 100008);

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
) NOT NULL
    );

INSERT INTO category (id, name)
VALUES (999, 'Electronics'),
       (998, 'Home Appliances');

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
) NOT NULL,
    password VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    255
),
    full_name VARCHAR
(
    255
),
    address VARCHAR
(
    255
),
    phone_number VARCHAR
(
    255
),
    role VARCHAR
(
    255
)
    );

INSERT INTO users (id, username, password, email, full_name, address, phone_number, role)
VALUES (9999990, 'u1', 'p1', 'u1@e.com', 'User One', 'Address 1', '1234567890', 'USER'),
       (9999991, 'u2', 'p2', 'u2@e.com', 'User Two', 'Address 2', '1234567891', 'USER'),
       (9999992, 'u3', 'p3', 'u3@e.com', 'User Three', 'Address 3', '1234567892', 'USER'),
       (9999993, 'u4', 'p4', 'u4@e.com', 'User Four', 'Address 4', '1234567893', 'USER'),
       (9999994, 'u5', 'p5', 'u5@e.com', 'User Five', 'Address 5', '1234567894', 'USER'),
       (9999995, 'u6', 'p6', 'u6@e.com', 'User Six', 'Address 6', '1234567895', 'USER'),
       (9999996, 'u7', 'p7', 'u7@e.com', 'User Seven', 'Address 7', '1234567896', 'USER'),
       (9999997, 'u8', 'p8', 'u8@e.com', 'User Eight', 'Address 8', '1234567897', 'USER'),
       (9999998, 'u9', 'p9', 'u9@e.com', 'User Nine', 'Address 9', '1234567898', 'USER'),
       (9999999, 'u10', 'p10', 'u10@e.com', 'User Ten', 'Address 10', '1234567899', 'USER'),
       (100006, 'company1', 'p6', 'c1@e.com', 'Company One', 'Company Address 1', '9876543210', 'COMPANY'),
       (100007, 'company2', 'p7', 'c2@e.com', 'Company Two', 'Company Address 2', '9876543211', 'COMPANY'),
       (100008, 'company3', 'p8', 'c3@e.com', 'Company Three', 'Company Address 3', '9876543212', 'COMPANY');

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
) NOT NULL,
    description TEXT,
    price DOUBLE,
    stock INT,
    company_id BIGINT,
    FOREIGN KEY
(
    company_id
) REFERENCES company
(
    id
)
    );

INSERT INTO product (id, name, description, price, stock, company_id)
VALUES (9999999, 'Laptop', 'High performance laptop', 1000.0, 10, 9999999),
       (9999998, 'Smartphone', 'Latest model smartphone', 700.0, 15, 9999998),
       (9999997, 'Blender', 'Powerful kitchen blender', 150.0, 20, 9999997);

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
),
    FOREIGN KEY
(
    category_id
) REFERENCES category
(
    id
)
    );

INSERT INTO product_category (product_id, category_id)
VALUES (9999999, 999),
       (9999998, 999),
       (9999997, 998);

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
)
    );

INSERT INTO cart (id, user_id)
VALUES (9999990, 9999990),
       (9999991, 9999991),
       (9999992, 9999992),
       (9999993, 9999993),
       (9999994, 9999994),
       (9999995, 9999995),
       (9999996, 9999996),
       (9999997, 9999997),
       (9999998, 9999998),
       (9999999, 9999999);

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
),
    FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
)
    );

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
)
    );

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
),
    FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
)
    );

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
),
    status VARCHAR
(
    255
),
    FOREIGN KEY
(
    order_id
) REFERENCES orders
(
    id
)
    );
