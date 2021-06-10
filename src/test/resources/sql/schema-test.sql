DROP TABLE IF EXISTS employee;

CREATE table employee (
    id BIGINT IDENTITY,
    name VARCHAR(255) NOT NULL,
    department VARCHAR(100)
);