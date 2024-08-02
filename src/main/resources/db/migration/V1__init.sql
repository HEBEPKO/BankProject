-- Создание таблицы account
CREATE TABLE account (
id SERIAL PRIMARY KEY,
account_number VARCHAR(20) NOT NULL,
balance DECIMAL(19, 2) NOT NULL,
recipient_name VARCHAR(100) NOT NULL,
pin_code VARCHAR(4) NOT NULL
);

-- Вставка исходных данных в таблицу account
INSERT INTO account (account_number, balance, recipient_name, pin_code) VALUES
('1234567890', 1000.00, 'Ivan Ivanov', '1234'),
('0987654321', 2000.00, 'Den Krylov', '5678');

-- Создание таблицы transaction
CREATE TABLE transaction (
id SERIAL PRIMARY KEY,
account_id BIGINT NOT NULL,
amount DECIMAL(19, 2) NOT NULL,
type VARCHAR(20) NOT NULL,
timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (account_id) REFERENCES account(id)
);

-- Вставка исходных данных в таблицу transaction
INSERT INTO transaction (account_id, amount, type) VALUES
(1, 1000.00, 'DEPOSIT'),
(2, 2000.00, 'DEPOSIT');