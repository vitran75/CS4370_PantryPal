create database if not exist pantry_pal;
use pantry_pal; 
DROP TABLE IF EXISTS user;

CREATE TABLE IF NOT EXISTS user (
    userId INT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    profileImagePath VARCHAR(255), -- NEW: avatar path
    mostRecentPostDate DATETIME,
    PRIMARY KEY (userId),
    UNIQUE (username),
    CONSTRAINT userName_min_length CHECK (CHAR_LENGTH(TRIM(username)) >= 2),
    CONSTRAINT firstName_min_length CHECK (CHAR_LENGTH(TRIM(firstName)) >= 2),
    CONSTRAINT lastName_min_length CHECK (CHAR_LENGTH(TRIM(lastName)) >= 2)
);

CREATE TABLE IF NOT EXISTS recipe ( 
    recipeId int auto_increment,
    userId int not null,
    
)



