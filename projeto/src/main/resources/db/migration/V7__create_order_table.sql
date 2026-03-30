CREATE TABLE orders
(
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    status     VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    total      DECIMAL(10, 2) NOT NULL,
    user_id    BIGINT         NOT NULL,
    address_id BIGINT         NOT NULL,
    created_at DATETIME       NOT NULL,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_order_address FOREIGN KEY (address_id) REFERENCES addresses (id)
);

CREATE TABLE order_items
(
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    quantity   INT            NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    order_id   BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products (id)
);