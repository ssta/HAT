CREATE TABLE IF NOT EXISTS TRANSACTIONS (
    TX_ID TEXT, -- output hash
    TX_INDEX INTEGER, -- output hash block ID
    TX_TIMESTAMP INTEGER, -- UNIX timestamp (seconds since epoch)
    TX_TYPE TEXT, -- one of SEND, RECV, MINT, MOVE
    PROCESSED_TIME INTEGER, -- when it was processed (0 implies not yet 
                            -- processed) UNIX TIMESTAMP
    PRIMARY KEY (TX_ID, TX_INDEX)
);
