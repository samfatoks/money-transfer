DROP TABLE IF EXISTS account;

CREATE TABLE account (
    id INT(11) AUTO_INCREMENT NOT NULL,
    username VARCHAR(40),
    balance DECIMAL(18,2),
    PRIMARY KEY (`id`),
    UNIQUE KEY username (username)
);

INSERT INTO account (username,balance) VALUES ('james',100.00);
INSERT INTO account (username,balance) VALUES ('john',200.00);
