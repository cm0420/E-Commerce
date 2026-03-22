CREATE TABLE addresses (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          street VARCHAR(120) NOT NULL,
                          number VARCHAR(10) NOT NULL,
                          complement VARCHAR(120),
                          district VARCHAR(120) NOT NULL,
                          city VARCHAR(120) NOT NULL,
                          state VARCHAR(50) NOT NULL,
                          zip_code VARCHAR(8) NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          user_id BIGINT NOT NULL,
                          CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
)