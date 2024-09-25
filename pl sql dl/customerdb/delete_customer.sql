drop procedure if exists delete_customer;
create procedure delete_customer(v_code int)
begin
declare mssg char(110);
declare vCode int default 0;
select  code into vCode from customer where code=v_code;
if vCode=0 then
 select CONCAT(v_code,' not  exists.') into mssg;
 SIGNAL SQLSTATE'45000' SET MESSAGE_TEXT=mssg;
 end if;
delete from customer 
where code=v_code;
end;//