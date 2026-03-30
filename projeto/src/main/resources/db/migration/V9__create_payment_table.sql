CREATE TABLE payments
(
    id               BIGINT         NOT NULL AUTO_INCREMENT,
    method           VARCHAR(20)    NOT NULL,
    status           VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    amount           DECIMAL(10, 2) NOT NULL,
    transaction_code VARCHAR(100),
    order_id         BIGINT         NOT NULL UNIQUE,
    created_at       DATETIME       NOT NULL,
    updated_at       DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE payment_cards
(
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    card_holder_name VARCHAR(100) NOT NULL,
    last_four_digits VARCHAR(4)   NOT NULL,
    expiry_month     INT          NOT NULL,
    expiry_year      INT          NOT NULL,
    brand            VARCHAR(20)  NOT NULL,
    is_default       BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id          BIGINT       NOT NULL,
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_card_user FOREIGN KEY (user_id) REFERENCES users (id)
);