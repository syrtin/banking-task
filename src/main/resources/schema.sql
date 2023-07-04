DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS bank_accounts;

CREATE TABLE IF NOT EXISTS client (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    lastname            VARCHAR(255)        NOT NULL,
    firstname           VARCHAR(255)        NOT NULL,
    middlename          VARCHAR(255),
    document_type       VARCHAR(255)        NOT NULL,
    document_sn         VARCHAR(255)        NOT NULL,
    birth_date          DATE                NOT NULL
);

CREATE TABLE IF NOT EXISTS bank_account (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    account_number      VARCHAR(255)        NOT NULL,
    account_currency    VARCHAR(255)        NOT NULL,
    client_id           INT                 NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);