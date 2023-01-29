CREATE DATABASE IF NOT EXISTS `auth_2`;
USE `auth_2`;

CREATE TABLE IF NOT EXISTS users
(
    id                      VARCHAR(255) NOT NULL,
    username                varchar(255) NOT NULL,
    password                varchar(255) NOT NULL,
    role_id                 int          NOT NULL,
    isAccountNonExpired     BOOLEAN      NOT NULL DEFAULT '1',
    isAccountNonLocked      BOOLEAN      NOT NULL DEFAULT '1',
    isCredentialsNonExpired BOOLEAN      NOT NULL DEFAULT '1',
    isEnabled               BOOLEAN      NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS roles
(
    id   int          NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE = InnoDB;

INSERT INTO roles (name)
VALUES ('ROLE_USER');
INSERT INTO roles (name)
VALUES ('ROLE_ADMIN');
