DROP DATABASE IF EXISTS ReadySetGo; 

CREATE DATABASE ReadySetGo; 

GRANT ALL PRIVILEGES ON ReadySetGo.* to admin@localhost IDENTIFIED BY 'admin';

USE ReadySetGo; 

CREATE TABLE 'Users'(
	uname VARCHAR(128) NOT NULL,
	name VARCHAR(128), 
	password VARCHAR(64),
	PRIMARY KEY(username)
); 

INSERT INTO Users VALUES ("krisht", "Krishna Thiyagarajan", "test123"); 
INSERT INTO Users VALUES ("rossk", "Ross Kaplan", "test123"); 
INSERT INTO Users VALUES ("abhinavj", "Abhinav Jain", "test123"); 
INSERT INTO Users VALUES ("brendas", "Brenda So", "test123"); 