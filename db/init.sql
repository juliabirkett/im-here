CREATE USER birketta WITH PASSWORD 'birketta!';

CREATE DATABASE imhere;
CREATE DATABASE imhere_test;

GRANT ALL ON DATABASE imhere TO birketta;
GRANT ALL ON DATABASE imhere_test TO birketta;
