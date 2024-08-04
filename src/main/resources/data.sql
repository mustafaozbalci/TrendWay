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
VALUES (1, 'Example Company 1', 1),
       (2, 'Example Company 2', 2),
       (3, 'Example Company 3', 3);

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
VALUES (1, 'Electronics'),
       (2, 'Home Appliances');

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
) NOT NULL
    );

INSERT INTO role (id, name)
VALUES (1, 'ADMIN'),
       (2, 'USER');

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
    role_id BIGINT,
    FOREIGN KEY
(
    role_id
) REFERENCES role
(
    id
)
    );

INSERT INTO users (id, username, password, email, full_name, address, phone_number, role_id)
VALUES (1, 'u1', 'p1', 'u1@e.com', 'User One', 'Address 1', '1234567890', 2),
       (2, 'u2', 'p2', 'u2@e.com', 'User Two', 'Address 2', '1234567891', 2),
       (3, 'u3', 'p3', 'u3@e.com', 'User Three', 'Address 3', '1234567892', 2),
       (4, 'u4', 'p4', 'u4@e.com', 'User Four', 'Address 4', '1234567893', 2),
       (5, 'u5', 'p5', 'u5@e.com', 'User Five', 'Address 5', '1234567894', 2),
       (6, 'u6', 'p6', 'u6@e.com', 'User Six', 'Address 6', '1234567895', 2),
       (7, 'u7', 'p7', 'u7@e.com', 'User Seven', 'Address 7', '1234567896', 2),
       (8, 'u8', 'p8', 'u8@e.com', 'User Eight', 'Address 8', '1234567897', 2),
       (9, 'u9', 'p9', 'u9@e.com', 'User Nine', 'Address 9', '1234567898', 2),
       (10, 'u10', 'p10', 'u10@e.com', 'User Ten', 'Address 10', '1234567899', 2);

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
VALUES (1, 'Laptop', 'High performance laptop', 1000.0, 10, 1),
       (2, 'Smartphone', 'Latest model smartphone', 700.0, 15, 1),
       (3, 'Blender', 'Powerful kitchen blender', 150.0, 20, 2);

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
VALUES (1, 1),
       (2, 1),
       (3, 2);

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
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10);

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
