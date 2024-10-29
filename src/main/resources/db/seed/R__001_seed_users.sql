INSERT INTO users (id, username, email, created_at) 
VALUES
(1, 'john_doe', 'john.doe@example.com', NOW()),
(2, 'jane_smith', 'jane.smith@example.com', NOW()),
(3, 'alice_jones', 'alice.jones@example.com', NOW())
ON CONFLICT (username) DO NOTHING;
