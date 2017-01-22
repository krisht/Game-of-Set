DROP DATABASE IF EXISTS ReadySetGo; 

CREATE DATABASE ReadySetGo; 

GRANT ALL PRIVILEGES ON ReadySetGo.* to admin@localhost IDENTIFIED BY 'admin';

USE ReadySetGo; 

CREATE TABLE 'Users'(
	uid VARCHAR(128),
	name VARCHAR(128), 
	password VARCHAR(64),
	joindate DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(username)
);

CREATE TABLE 'playsin'(
	uid VARCHAR(128),
	gid INTEGER,
	finalscore INTEGER DEFAULT 0,
	PRIMARY KEY (uid, gid),
	FOREIGN KEY (uid) REFERENCES Users(uid),
	FOREIGN KEY (gid) REFERENCES Game(gid)
);

CREATE TABLE 'Game'(
	gid INTEGER AUTO_INCREMENT,
	gametime DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(gid)
);

INSERT INTO Users(uid, name, password) VALUES ("krisht", "Krishna Thiyagarajan", "test123"); 
INSERT INTO Users(uid, name, password) VALUES ("rossk", "Ross Kaplan", "test123"); 
INSERT INTO Users(uid, name, password) VALUES ("abhinavj", "Abhinav Jain", "test123"); 
INSERT INTO Users(uid, name, password) VALUES ("brendas", "Brenda So", "test123"); 