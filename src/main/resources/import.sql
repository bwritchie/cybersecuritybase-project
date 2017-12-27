INSERT INTO Users (username,password) VALUES ('dave', 'password');
INSERT INTO Users (username,password) VALUES ('alan', 'foobar');
INSERT INTO Users (username,password) VALUES ('rick', '123456');
INSERT INTO Users (username,password) VALUES ('joe', 'letmein');
INSERT INTO Users (username,password) VALUES ('admin', 'badmin');

INSERT INTO Notes (username,note) VALUES ('dave', 'employee of the month award on 31/10/17');
INSERT INTO Notes (username,note) VALUES ('dave', 'late for work on 22/12/17');

INSERT INTO Employees (username, name, address, ninumber, salary, manager) VALUES ('dave', 'Dave Smith', '123 Acacia Road', 'AB123456C', 40000, FALSE);
INSERT INTO Employees (username, name, address, ninumber, salary, manager) VALUES ('alan', 'Alan Jones', '3 The Street', 'UU786578D', 25000, FALSE);
INSERT INTO Employees (username, name, address, ninumber, salary, manager) VALUES ('rick', 'Rick Witter', '1b Dustbin Lane', 'TT679843E', 15000, FALSE);
INSERT INTO Employees (username, name, address, ninumber, salary, manager) VALUES ('joe', 'Joe King', 'The Manor', 'PQ184758D', 65000, TRUE);
