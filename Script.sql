DROP DATABASE IF EXISTS map;
CREATE DATABASE map;
USE map;


CREATE TABLE provaC(
       X varchar(10),
       Y float(5,2),
	  C float(5,2)
);

DROP USER IF EXISTS 'Student'@'localhost';
CREATE USER 'Student'@'localhost' IDENTIFIED BY 'map';
GRANT ALL PRIVILEGES ON provaC TO 'Student'@'localhost';


insert into map.provaC values('A',2,1);
insert into map.provaC values('A',2,1);
insert into map.provaC values('A',1,1);
insert into map.provaC values('A',2,1);
insert into map.provaC values('A',5,1.5);
insert into map.provaC values('A',5,1.5);
insert into map.provaC values('A',6,1.5);
insert into map.provaC values('B',6,10);
insert into map.provaC values('A',6,1.5);
insert into map.provaC values('A',6,1.5);
insert into map.provaC values('B',10,10);
insert into map.provaC values('B',5,10);
insert into map.provaC values('B',12,10);
insert into map.provaC values('B',14,10);
insert into map.provaC values('A',1,1);
commit;
