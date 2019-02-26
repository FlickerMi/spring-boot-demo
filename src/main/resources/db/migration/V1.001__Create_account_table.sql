CREATE TABLE account(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255),
  password VARCHAR(255),
  gender VARCHAR(255),
  status VARCHAR(255),
  age INT,
  wechat_open_id VARCHAR(255),
  created_by VARCHAR(255),
  updated_by VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
