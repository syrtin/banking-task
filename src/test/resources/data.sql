DELETE FROM client;
DELETE FROM bank_account;

-- Insert a single client into the client table
INSERT INTO client (lastname, firstname, middlename, document_type, document_sn, birth_date)
VALUES  ('Ivanov', 'Ivan', 'Ivanovich', 'PASSPORT', '1234567890', '1991-01-01');

-- Get the latest generated ID from the client table and store it in a variable
SET @client_id = (SELECT MAX(id) FROM client);

-- Insert a single bank account into the bank_account table
INSERT INTO bank_account (account_number, account_currency, client_id)
VALUES  ('123456789', 'RUB', @client_id);
