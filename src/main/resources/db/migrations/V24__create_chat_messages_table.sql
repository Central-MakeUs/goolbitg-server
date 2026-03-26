CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    buyornot_id BIGINT,
    user_id VARCHAR(50),
    content VARCHAR(1000),
    sent_datetime DATETIME
);

CREATE INDEX chat_messages_idx_buyornot_id
ON chat_messages (buyornot_id);
