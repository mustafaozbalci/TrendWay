-- Tüm tabloları düşür
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS company;

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
    address VARCHAR
(
    255
),
    email VARCHAR
(
    255
),
    phone_number VARCHAR
(
    255
),
    wallet_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY
(
    user_id
) REFERENCES users
(
    id
)
    );

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

-- Product-Category bağlantı tablosu ve verileri
CREATE TABLE IF NOT EXISTS product_category
(
    product_id
    BIGINT,
    category
    VARCHAR
(
    255
),
    FOREIGN KEY
(
    product_id
) REFERENCES product
(
    id
)
    );

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
    company_id
    BIGINT,
    order_date
    TIMESTAMP
    DEFAULT
    CURRENT_TIMESTAMP,
    total_amount
    DOUBLE,
    FOREIGN
    KEY
(
    user_id
) REFERENCES users
(
    id
),
    FOREIGN KEY
(
    company_id
) REFERENCES company
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

-- Kullanıcı verileri (admin ve normal kullanıcı)
INSERT INTO users (id, username, password, email, full_name, address, phone_number, role)
VALUES (10001, 'user', 'pu', 'user@example.com', 'User One', 'Address 1', '1234567890', 'USER'),
       (10002, 'admin', 'pc', 'admin@example.com', 'Admin One', 'Admin Address', '0987654321', 'ADMIN');

-- Şirket verisi
INSERT INTO company (id, name, address, email, phone_number, wallet_id, user_id)
VALUES (10001, 'Example Company', 'Company Address', 'company@example.com', '1122334455', 10001, 10002);

-- Ürün verileri
INSERT INTO product (id, name, description, price, stock, company_id)
VALUES (10001, 'Product One', 'Description for Product One', 100.0, 50, 10001),
       (10002, 'Product Two', 'Description for Product Two', 200.0, 30, 10001);

-- Product-Category bağlantı verileri
INSERT INTO product_category (product_id, category)
VALUES (10001, 'CATEGORY_ONE'),
       (10002, 'CATEGORY_TWO');

-- Sepet verisi
INSERT INTO cart (id, user_id)
VALUES (10001, 10001);
