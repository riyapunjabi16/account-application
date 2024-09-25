drop procedure if exists update_customer; 
create procedure update_customer(vCode int,vName char(100),vOpeningBalance int,vOpeningBalanceType char(1))
Begin
declare c char(100);
declare co int;
declare mssg char(110);
select name into c from customer where code=vCode;
if c is NULL  then
 select CONCAT(vCode,' ','not exists.') into mssg; 
 signal SQLSTATE '45000' set MESSAGE_TEXT=mssg;
 end if;
select code into co from customer where name=vName ;
if co<>vCode  then
 select CONCAT(vName,' already exists') INTO mssg ;
 signal SQLSTATE '45000' set MESSAGE_TEXT =mssg;
 end if;
 

UPDATE customer SET name=vName,opening_Balance=vOpeningBalance,opening_Balance_Type=vOpeningBalanceType 
WHERE code=vCode ;

end; //