CREATE TABLE TIMELINE_ITEMS (
    ID SERIAL PRIMARY KEY,
    ACCOUNT_ID INTEGER NOT NULL,
    SERVICE_ID VARCHAR(50) NOT NULL,
    CONTENTS TEXT NOT NULL,
    CREATED_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ACCOUNTS (
    ID SERIAL PRIMARY KEY,
    NAME VARCHAR(128) UNIQUE,
    PASSWORD VARCHAR(60),
    CREATED_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO ACCOUNTS(NAME, PASSWORD) VALUES('Tom', 'password');