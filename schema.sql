DROP DATABASE IF EXISTS ReadySetGo; 

CREATE DATABASE ReadySetGo;

GRANT ALL PRIVILEGES ON ReadySetGo.* to ross@localhost IDENTIFIED BY 'rossk';

USE ReadySetGo;

DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS playsin;
DROP TABLE IF EXISTS Game;

CREATE TABLE Users(
    uid INTEGER AUTO_INCREMENT,
	username VARCHAR(128) UNIQUE,
	password VARCHAR(64),
	joindate DATETIME DEFAULT CURRENT_TIMESTAMP,
	score INTEGER DEFAULT 0,
	PRIMARY KEY(uid)
);


INSERT INTO Users(username, password) VALUES ('krisht',  'test123');
INSERT INTO Users(username, password) VALUES ('rossk', 'test123');
INSERT INTO Users(username, password) VALUES ('abhinavj', 'test123');
INSERT INTO Users(username, password) VALUES ('brendas', 'test123');