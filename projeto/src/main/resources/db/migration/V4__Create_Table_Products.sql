CREATE TABLE products (
                          id          BIGINT          NOT NULL AUTO_INCREMENT,
                          name        VARCHAR(80)     NOT NULL,
                          description VARCHAR(255),
                          price       DECIMAL(10,2)   NOT NULL,
                          sku         VARCHAR(50)     NOT NULL UNIQUE,
                          image_url   VARCHAR(255),
                          is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
                          category_id BIGINT          NOT NULL,
                          created_at  DATETIME        NOT NULL,
                          updated_at  DATETIME,
                          PRIMARY KEY (id),
                          CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);