CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(50),
    content VARCHAR(1000),
    sent_datetime DATETIME
);

CREATE INDEX chat_messages_idx_sent_datetime
ON chat_messages (sent_datetime);
