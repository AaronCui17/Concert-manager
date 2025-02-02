DROP TABLE Perform;
DROP TABLE Film;
DROP TABLE Performers;
DROP TABLE Tickets;
DROP TABLE Performances;
DROP TABLE Audience;
DROP TABLE Manage;
DROP TABLE StageCrew;
DROP TABLE ManagersAttributes;
DROP TABLE ManagersAddress;
DROP TABLE VenuesAttributes;
DROP TABLE VenuesMID;
DROP TABLE Managers;
DROP TABLE Venues;
DROP TABLE Upload;
DROP TABLE Videographers;
DROP TABLE RecordingsDuration;
DROP TABLE RecordingsName;
DROP TABLE Recordings;

CREATE TABLE Recordings
  (rNum INTEGER PRIMARY KEY,
  duration INTEGER NOT NULL,
  rName VARCHAR(255) NOT NULL,
  rDate DATE,
  UNIQUE (rName));

grant select on Recordings to public;

CREATE TABLE RecordingsName
	(rName VARCHAR(255) PRIMARY KEY,
	rNum INTEGER NOT NULL,
UNIQUE (rNum));

grant select on RecordingsName to public;

CREATE TABLE RecordingsDuration
	(duration INTEGER,
	rName VARCHAR(255),
	rDate DATE,
	PRIMARY KEY (duration, rName, rDate),
UNIQUE (rName));

grant select on RecordingsDuration to public;

CREATE TABLE Videographers
	(vid INTEGER PRIMARY KEY,
	vName varchar(255) NOT NULL);

grant select on Videographers to public;

CREATE TABLE Upload
  (rNum INTEGER,
  vid INTEGER,
  PRIMARY KEY (rNum, vid),
  FOREIGN KEY (rNum) REFERENCES Recordings(rNum)
  ON DELETE CASCADE,
  FOREIGN KEY (vid) REFERENCES Videographers(vid)
    ON DELETE CASCADE);

grant select on Upload to public;

CREATE TABLE Venues 
  (vAddress VARCHAR(255) PRIMARY KEY,
  vName VARCHAR(255) NOT NULL,
  occupancy INTEGER
  );

grant select on Venues to public;

CREATE TABLE Managers
  (mid INTEGER PRIMARY KEY,
  mName VARCHAR(255) NOT NULL,
  mAddress VARCHAR(255) NOT NULL,   
  UNIQUE (mAddress),
  FOREIGN KEY (mAddress) REFERENCES Venues(vAddress)
    ON DELETE CASCADE);

grant select on Managers to public;

CREATE TABLE VenuesMID
	(mid INTEGER PRIMARY KEY,
	vAddress VARCHAR(255) NOT NULL, 
	UNIQUE (vAddress),
	FOREIGN KEY (mid) REFERENCES Managers(mid)  
  );

grant select on VenuesMID to public;

CREATE TABLE VenuesAttributes
  (vName VARCHAR(255) NOT NULL,
	occupancy INTEGER,
	mid INTEGER PRIMARY KEY,
	FOREIGN KEY (mid) REFERENCES Managers(mid)
	); 

grant select on VenuesAttributes to public;

CREATE TABLE ManagersAddress
  (mAddress VARCHAR(255) PRIMARY KEY,  
  mid INTEGER NOT NULL,
  UNIQUE (mid),
  FOREIGN KEY (mAddress) REFERENCES Venues(vAddress)
    ON DELETE CASCADE);

grant select on Managers to public;

CREATE TABLE ManagersAttributes
  (mName VARCHAR(255) NOT NULL,
  mAddress VARCHAR(255) PRIMARY KEY,
  UNIQUE (mName),
  FOREIGN KEY (mAddress) REFERENCES Venues(vAddress)
    ON DELETE CASCADE);

grant select on ManagersAttributes to public;

CREATE TABLE StageCrew
	(scid INTEGER PRIMARY KEY,
  scName VARCHAR(255) NOT NULL,
  job VARCHAR(255)); 

grant select on StageCrew to public;

CREATE TABLE Manage           	
  (mid INTEGER,
  scid INTEGER,
  PRIMARY KEY (mid, scid),
  FOREIGN KEY (mid) REFERENCES Managers(mid)
    ON DELETE CASCADE,
  FOREIGN KEY (scid) REFERENCES StageCrew(scid)
    ON DELETE CASCADE);

grant select on Manage to public;

CREATE TABLE Audience
  (email VARCHAR(255) PRIMARY KEY,
  aName VARCHAR(255) NOT NULL);
 
grant select on Audience to public;

CREATE TABLE Performances
  (showid INTEGER PRIMARY KEY,
  sName VARCHAR(255) NOT NULL,
  sDate DATE,
  sTime INTEGER,
  sAddress VARCHAR(255) NOT NULL,  
  numPerformers INTEGER,
  conductor VARCHAR(255),                                	 
  composer VARCHAR(255),
  FOREIGN KEY (sAddress) REFERENCES Venues(vAddress)  
  );

grant select on Performances to public;

CREATE TABLE Tickets
  (seatNum INTEGER,
  tRow CHAR(2),     
  tType VARCHAR(255),
   showid INTEGER,
  email VARCHAR(255),
  PRIMARY KEY (seatNum, tRow, showid),
  FOREIGN KEY (showid) REFERENCES Performances(showid)
    ON DELETE CASCADE,
  FOREIGN KEY (email) REFERENCES Audience(email)
    ); 

grant select on Tickets to public;

 CREATE TABLE Performers
  (pid INTEGER PRIMARY KEY,
  pName VARCHAR(255) NOT NULL,
  instrument VARCHAR(255),
  genre VARCHAR(255));

grant select on Performers to public;

CREATE TABLE Film
  (showid INTEGER,
  vid INTEGER,
  PRIMARY KEY (showid, vid),
  FOREIGN KEY (showid) REFERENCES Performances(showid)
    ON DELETE CASCADE,
  FOREIGN KEY (vid) REFERENCES Videographers(vid)
    ON DELETE CASCADE);

grant select on Film to public;

CREATE TABLE Perform
  (pid INTEGER,
   showid INTEGER,
  PRIMARY KEY (pid, showid),
  FOREIGN KEY (showid) REFERENCES Performances (showid)
    ON DELETE CASCADE,
  FOREIGN KEY (pid) REFERENCES Performers(pid)
    ON DELETE CASCADE);

grant select on Perform to public;

INSERT INTO Recordings VALUES (1, 120, 'Concert in the Park', DATE '2023-10-15');
INSERT INTO Recordings VALUES (2, 90, 'Acoustic Jam', DATE '2023-09-20');
INSERT INTO Recordings VALUES (3, 150, 'Pop Sensation Showcase', DATE '2023-11-05');
INSERT INTO Recordings VALUES (4, 90, 'Classical Crescendo Concert', DATE '2023-09-20');
INSERT INTO Recordings VALUES (5, 150, 'Soulful Melodies Showcase', DATE '2023-11-05');

INSERT INTO RecordingsName VALUES ('Concert in the Park', 1);
INSERT INTO RecordingsName VALUES ('Acoustic Jam', 2);
INSERT INTO RecordingsName VALUES ('Pop Sensation Showcase', 3);
INSERT INTO RecordingsName VALUES ( 'Classical Crescendo Concert', 4);
INSERT INTO RecordingsName VALUES ( 'Soulful Melodies Showcase', 5);

INSERT INTO RecordingsDuration VALUES (120, 'Concert in the Park', DATE '2023-10-15');
INSERT INTO RecordingsDuration VALUES (90, 'Acoustic Jam', DATE '2023-09-20');
INSERT INTO RecordingsDuration VALUES (150, 'Pop Sensation Showcase', DATE '2023-11-05');
INSERT INTO RecordingsDuration VALUES (90, 'Classical Crescendo Concert', DATE '2023-09-20');
INSERT INTO RecordingsDuration VALUES (150, 'Soulful Melodies Showcase', DATE '2023-11-05');

INSERT INTO Videographers VALUES (1, 'Amelia Parker');
INSERT INTO Videographers VALUES (2, 'Benjamin Smith');
INSERT INTO Videographers VALUES (3, 'Chloe Davis');

INSERT INTO Upload VALUES (1, 2);
INSERT INTO Upload VALUES (2, 2);
INSERT INTO Upload VALUES (3, 3);
INSERT INTO Upload VALUES (4, 5);
INSERT INTO Upload VALUES (5, 1);

INSERT INTO Venues VALUES ('123 Maple Street, Toronto, ON M5V 2N7', 'Rogers Arena', 200);
INSERT INTO Venues VALUES ('456 Cedar Avenue, Vancouver, BC V6B 2P4', 'Oracle Centre', 100);
INSERT INTO Venues VALUES ('789 Birch Lane, Calgary, AB T2P 3H9', 'TD Garden', 400);
INSERT INTO Venues VALUES ('567 Oak Road, Ottawa, ON K1A 0G9', 'Scotiabank Arena', 50);
INSERT INTO Venues VALUES ('321 Pine Drive, Montreal, QC H2Z 1J4', 'The Sphere', 250);

INSERT INTO Managers VALUES (1, 'Finn Anderson', '123 Maple Street, Toronto, ON M5V 2N7');
INSERT INTO Managers VALUES (2, 'Grace Martinez', '456 Cedar Avenue, Vancouver, BC V6B 2P4');
INSERT INTO Managers VALUES (3, 'Henry Taylor', '789 Birch Lane, Calgary, AB T2P 3H9');
INSERT INTO Managers VALUES (4, 'Isabella Brown', '321 Pine Drive, Montreal, QC H2Z 1J4');
INSERT INTO Managers VALUES (5, 'Jackson Wilson', '567 Oak Road, Ottawa, ON K1A 0G9');

INSERT INTO VenuesMID VALUES (1, '123 Maple Street, Toronto, ON M5V 2N7');
INSERT INTO VenuesMID VALUES (2, '456 Cedar Avenue, Vancouver, BC V6B 2P4');
INSERT INTO VenuesMID VALUES (3, '789 Birch Lane, Calgary, AB T2P 3H9');
INSERT INTO VenuesMID VALUES (4, '567 Oak Road, Ottawa, ON K1A 0G9');
INSERT INTO VenuesMID VALUES (5, '321 Pine Drive, Montreal, QC H2Z 1J4');

INSERT INTO VenuesAttributes VALUES ('Rogers Arena', 200, 1);
INSERT INTO VenuesAttributes VALUES ('Oracle Centre', 100, 2);
INSERT INTO VenuesAttributes VALUES ('TD Garden', 400, 3);
INSERT INTO VenuesAttributes VALUES ('Scotiabank Arena', 50, 4);
INSERT INTO VenuesAttributes VALUES ('The Sphere', 250, 5);

INSERT INTO ManagersAddress VALUES ('123 Maple Street, Toronto, ON M5V 2N7', 1);
INSERT INTO ManagersAddress VALUES ('456 Cedar Avenue, Vancouver, BC V6B 2P4', 2);
INSERT INTO ManagersAddress VALUES ('789 Birch Lane, Calgary, AB T2P 3H9', 3);
INSERT INTO ManagersAddress VALUES ('321 Pine Drive, Montreal, QC H2Z 1J4', 4);
INSERT INTO ManagersAddress VALUES ('567 Oak Road, Ottawa, ON K1A 0G9', 5);

INSERT INTO ManagersAttributes VALUES ('Finn Anderson', '123 Maple Street, Toronto, ON M5V 2N7');
INSERT INTO ManagersAttributes VALUES ('Grace Martinez', '456 Cedar Avenue, Vancouver, BC V6B 2P4');
INSERT INTO ManagersAttributes VALUES ('Henry Taylor', '789 Birch Lane, Calgary, AB T2P 3H9');
INSERT INTO ManagersAttributes VALUES ( 'Isabella Brown', '321 Pine Drive, Montreal, QC H2Z 1J4');
INSERT INTO ManagersAttributes VALUES ('Jackson Wilson', '567 Oak Road, Ottawa, ON K1A 0G9');

INSERT INTO StageCrew VALUES (1, 'Oliver Smith', 'Lighting Technician');
INSERT INTO StageCrew VALUES (2, 'Emma Johnson', 'Sound Engineer');
INSERT INTO StageCrew VALUES (3, 'Liam Davis', 'Stage Manager');
INSERT INTO StageCrew VALUES (4, 'Ava Wilson', 'Props Coordinator');
INSERT INTO StageCrew VALUES (5, 'Noah Martinez', 'Costume Designer');

INSERT INTO Manage VALUES (1, 1);
INSERT INTO Manage VALUES (2, 2);
INSERT INTO Manage VALUES (4, 3);
INSERT INTO Manage VALUES (1, 4);
INSERT INTO Manage VALUES (3, 5);

INSERT INTO Audience VALUES ('john@gmail.com', 'John Smith');
INSERT INTO Audience VALUES ('sarah@gmail.com', 'Sarah Johnson');
INSERT INTO Audience VALUES ('michael@gmail.com', 'Michael Brown');
INSERT INTO Audience VALUES ('emily@gmail.com', 'Emily Davis');
INSERT INTO Audience VALUES ('david@gmail.com', 'David Wilson');

INSERT INTO Performances VALUES (1, 'Moonlight Sonata', DATE '2023-10-23', 1930, '123 Maple Street, Toronto, ON M5V 2N7', 1, 'John Doe', NULL);
INSERT INTO Performances VALUES (2, 'Rhythmic Fusion', DATE '2023-11-12', 2000, '456 Cedar Avenue, Vancouver, BC V6B 2P4', 1, 'Michelle Jeans', NULL);
INSERT INTO Performances VALUES (3, 'Jazz Vibes Extravaganza', DATE '2023-12-05', 1845, '789 Birch Lane, Calgary, AB T2P 3H9', 1, 'Sophia Anderson', NULL);
INSERT INTO Performances VALUES (4, 'HarmonyFest2023', DATE '2023-01-18', 1400, '567 Oak Road, Ottawa, ON K1A 0G9', NULL, NULL, 'Raymond Johnson');
INSERT INTO Performances VALUES (5, 'Rock Legends Reunion', DATE '2024-02-07', 2315, '567 Oak Road, Ottawa, ON K1A 0G9', NULL, 'Carol Brooks', NULL);
INSERT INTO Performances VALUES (6, 'Moonlight Sonata', DATE '2023-11-12', 2000, '456 Cedar Avenue, Vancouver, BC V6B 2P4', 1, 'Michelle Jeans', NULL);
INSERT INTO Performances VALUES (7, 'Moonlight Sonata', DATE '2023-12-05', 1845, '789 Birch Lane, Calgary, AB T2P 3H9', 1, 'Sophia Anderson', NULL);
INSERT INTO Performances VALUES (8, 'Moonlight Sonata', DATE '2023-01-18', 1400, '567 Oak Road, Ottawa, ON K1A 0G9', NULL, NULL, 'Raymond Johnson');
INSERT INTO Performances VALUES (9, 'Moonlight Sonata', DATE '2024-02-07', 2315, '567 Oak Road, Ottawa, ON K1A 0G9', NULL, 'Carol Brooks', NULL);

INSERT INTO Tickets VALUES (1, 'AA', 'Balcony', 1,  'john@gmail.com');
INSERT INTO Tickets VALUES (2, 'AA', 'Balcony', 2, 'sarah@gmail.com');
INSERT INTO Tickets VALUES (3, 'AA', 'Balcony', 3, 'michael@gmail.com');
INSERT INTO Tickets VALUES (4, 'AA', 'Balcony', 4, 'emily@gmail.com');
INSERT INTO Tickets VALUES (5, 'AA', 'Balcony', 5, 'david@gmail.com');

INSERT INTO Performers VALUES (1, 'Sophia Adams', 'Violin', NULL);
INSERT INTO Performers VALUES (2, 'Ethan Parker', NULL, 'Pop');
INSERT INTO Performers VALUES (3, 'Ava Mitchell', 'Clarinet', NULL);
INSERT INTO Performers VALUES (4, 'Logan Anderson', 'Flute', NULL);
INSERT INTO Performers VALUES (5, 'Olivia Turner', NULL, 'Punk');

INSERT INTO Film VALUES (1, 1);
INSERT INTO Film VALUES (2, 1);
INSERT INTO Film VALUES (3, 1);
INSERT INTO Film VALUES (4, 1);
INSERT INTO Film VALUES (5, 1);
INSERT INTO Film VALUES (6, 1);
INSERT INTO Film VALUES (7, 1);
INSERT INTO Film VALUES (8, 1);
INSERT INTO Film VALUES (9, 1);
INSERT INTO Film VALUES (3, 2);
INSERT INTO Film VALUES (4, 2);
INSERT INTO Film VALUES (5, 2);
INSERT INTO Film VALUES (1, 3);
INSERT INTO Film VALUES (8, 3);

INSERT INTO Perform VALUES (1, 1);
INSERT INTO Perform VALUES (3, 2);
INSERT INTO Perform VALUES (4, 3);
INSERT INTO Perform VALUES (2, 4);
INSERT INTO Perform VALUES (5, 5);
