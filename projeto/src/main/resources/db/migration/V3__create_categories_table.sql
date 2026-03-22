CREATE TABLE categories
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(80) NOT NULL UNIQUE,
    description VARCHAR(255),
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  DATETIME    NOT NULL,
    updated_at  DATETIME,
    PRIMARY KEY (id)
);