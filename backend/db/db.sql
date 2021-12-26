DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE roles(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(180) NOT NULL UNIQUE,
	image VARCHAR(255) NULL,
	route VARCHAR(255) NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL
);

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users(
	id BIGSERIAL PRIMARY KEY,
	email VARCHAR(255) NOT NULL UNIQUE,
	name VARCHAR(255) NOT NULL,
	lastname VARCHAR(255) NOT NULL,
	phone VARCHAR(80) NOT NULL UNIQUE,
	image VARCHAR(255) NULL,
	password VARCHAR(255) NOT NULL,
	is_available BOOLEAN NULL,
	session_token VARCHAR(255) NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL
);

DROP TABLE IF EXISTS user_has_roles CASCADE;
CREATE TABLE user_has_roles(
	id_user BIGSERIAL NOT NULL,
	id_role BIGSERIAL NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_user) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(id_role) REFERENCES roles(id) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(id_user, id_role)	
);

DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(180) NOT NULL UNIQUE,
	image VARCHAR(255) NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL
);

DROP TABLE IF EXISTS products CASCADE;
CREATE TABLE products(
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(180) NOT NULL UNIQUE, 
	description VARCHAR(255) NOT NULL,
	price DECIMAL DEFAULT 0,
	image00 VARCHAR(255) NOT NULL,
	image01 VARCHAR(255) NULL,
	image02 VARCHAR(255) NULL,
	id_category BIGINT NOT NULL,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_category) REFERENCES categories(id) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS address CASCADE;
CREATE TABLE address(
	id BIGSERIAL PRIMARY KEY,
	id_user BIGINT NOT NULL,
	address VARCHAR(255) NOT NULL,
	neighborhood VARCHAR(255) NOT NULL,
	lat DECIMAL DEFAULT 0,
	lng DECIMAL DEFAULT 0,
	created_at TIMESTAMP(0) NOT NULL,
	updated_at TIMESTAMP(0) NOT NULL,
	FOREIGN KEY(id_user) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO roles(
	name,
	route,
	image,
	created_at,
	updated_at
)
VALUES(
	'CLIENT',
	'client/home',
	'https://cdn-icons-png.flaticon.com/512/1257/1257081.png',
	'2021-10-04',
	'2021-10-04'
);

INSERT INTO roles(
	name,
	route,
	image,
	created_at,
	updated_at
)
VALUES(
	'RESTAURANT',
	'restaurant/home',
	'https://cdn-icons-png.flaticon.com/512/808/808175.png',
	'2021-10-04',
	'2021-10-04'
);

INSERT INTO roles(
	name,
	route,
	image,
	created_at,
	updated_at
)
VALUES(
	'DELIVERY',
	'delivery/home',
	'https://cdn-icons-png.flaticon.com/512/1254/1254223.png',
	'2021-10-04',
	'2021-10-04'
);