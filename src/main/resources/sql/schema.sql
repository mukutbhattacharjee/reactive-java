DROP TABLE IF EXISTS employee;

CREATE table employee (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    department VARCHAR(100)
);