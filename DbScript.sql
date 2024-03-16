-- Create database

CREATE DATABASE `safe_scheduler`

-- Calendar table

CREATE TABLE Calendar (
  id int NOT NULL,
  name varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  start_time varchar(255) NOT NULL,
  end_time varchar(255) NOT NULL,
  user_id int DEFAULT NULL,
  user_name varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--  ChessEvent table

CREATE TABLE ChessEvent (
  id int NOT NULL,
  player1 varchar(255) NOT NULL,
  player2 varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  start_time varchar(255) NOT NULL,
  end_time varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;