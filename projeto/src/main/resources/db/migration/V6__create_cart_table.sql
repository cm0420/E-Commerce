CREATE TABLE cart
(
    id         BIGINT   NOT NULL AUTO_INCREMENT,
    user_id    BIGINT   NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE cart_items
(
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    cart_id    BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    created_at DATETIME       NOT NULL,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart (id),
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products (id)
);