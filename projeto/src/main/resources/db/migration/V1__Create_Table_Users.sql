CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(80) NOT NULL,
                       last_name VARCHAR(80) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       phone_number VARCHAR(100) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(100) NOT NULL,
                       cpf VARCHAR(11) NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP,
                       CONSTRAINT uk_users_email UNIQUE (email),
                       CONSTRAINT uk_users_cpf UNIQUE (cpf)
)