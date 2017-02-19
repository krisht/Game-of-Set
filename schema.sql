DROP DATABASE IF EXISTS ReadySetGo; 

CREATE DATABASE ReadySetGo; 

GRANT ALL PRIVILEGES ON ReadySetGo.* to admin@localhost IDENTIFIED BY 'admin';

USE ReadySetGo; 

CREATE TABLE Users (
    uid INTEGER AUTO_INCREMENT,
	username VARCHAR(128),
	name VARCHAR(128), 
	password VARCHAR(64),
	joindate datetime DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(uid)
);

CREATE TABLE playsin(
	uid INTEGER,
	gid INTEGER,
	finalscore INTEGER DEFAULT 0,
	PRIMARY KEY (uid, gid),
	FOREIGN KEY (uid) REFERENCES Users(uid),
	FOREIGN KEY (gid) REFERENCES Game(gid)
);

CREATE TABLE Game (
	gid INTEGER AUTO_INCREMENT,
	gametime DATETIME,
	PRIMARY KEY(gid)
);

INSERT INTO Users(username, name, password) VALUES ("krisht", "Krishna Thiyagarajan", "test123"); 
INSERT INTO Users(username, name, password) VALUES ("rossk", "Ross Kaplan", "test123"); 
INSERT INTO Users(username, name, password) VALUES ("abhinavj", "Abhinav Jain", "test123"); 
INSERT INTO Users(username, name, password) VALUES ("brendas", "Brenda So", "test123"); 
