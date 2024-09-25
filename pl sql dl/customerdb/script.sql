create table Customer
(code int primary key auto_increment,
name char(100)  not null unique,
opening_Balance int not null,
opening_Balance_Type char(1) not null )Engine=InnoDB; 