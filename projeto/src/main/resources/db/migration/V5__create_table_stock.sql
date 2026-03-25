CREATE TABLE stock
(
    id         BIGINT   NOT NULL AUTO_INCREMENT,
    quantity   INT      NOT NULL DEFAULT 0,
    product_id BIGINT   NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_stock_product FOREIGN KEY (product_id) REFERENCES products (id)
);