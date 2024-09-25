drop procedure if exists add_customer;
create procedure add_customer(OUT code int,vName char(100),openingBalance int,openingBalanceType char(1)) 
Begin
declare vCode int;
declare str char(120);
select code into vCode from customer where name=vName;
if vCode is NOT NULL 
then
 select CONCAT(name,' ','exists') into str from customer where name=vName;
 SIGNAL SQLSTATE'45000'
 SET MESSAGE_TEXT=str;
else 
 INSERT INTO customer(name,opening_Balance,opening_Balance_Type) 
 VALUES(vName,openingBalance,openingBalanceType);
 end if;


end;//