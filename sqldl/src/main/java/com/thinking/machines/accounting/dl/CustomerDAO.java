package com.thinking.machines.accounting.dl;
import java.util.*;
import java.sql.*;
public class CustomerDAO implements CustomerDAOInterface 
{ 
public void add (CustomerDTOInterface customerDTOInterface)throws DAOException
{
try 
{
String vName;
int code=0;
Connection c=DAOConnection.getConnection();
ResultSet r;
vName=customerDTOInterface.getName();
PreparedStatement ps=c.prepareStatement("select name from Customer where name=?");
ps.setString(1,vName);
r=ps.executeQuery();

if(r.next())
{
r.close();
ps.close();
c.close();
throw new DAOException("Name exists:"+vName);
}
ps=c.prepareStatement("insert into Customer(name,opening_Balance,opening_Balance_Type) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
ps.setString(1,vName);
ps.setInt(2,customerDTOInterface.getOpeningBalance());
ps.setString(3,String.valueOf(customerDTOInterface.getOpeningBalanceType()));
ps.executeUpdate();
r=ps.getGeneratedKeys();
if(r.next())
{
 code=r.getInt(1);
}
customerDTOInterface.setCode(code);
r.close();
ps.close();
c.close();
}
catch(SQLException sqlException)
{
System.out.println(sqlException);
}

}
public void update(CustomerDTOInterface customerDTOInterface) throws DAOException
{
try
{
Connection c=DAOConnection.getConnection();
int vCode=customerDTOInterface.getCode();
String vName=customerDTOInterface.getName();
String Sname;
PreparedStatement  ps=c.prepareStatement("select name from customer where code=?");
ps.setInt(1,vCode);
ResultSet r;
r=ps.executeQuery();
if(!r.next())
{
r.close();
ps.close();
c.close();
throw new DAOException("Record not exists");
}
Sname=r.getString("name");
r.close();
ps.close();
//dupliacy of name
ps=c.prepareStatement("select name from customer where NOT code=?");
ps.setInt(1,vCode);
r=ps.executeQuery();
if(r.next())
{
r.close();
ps.close();
c.close();
throw new DAOException("Name exists.");
}
r.close();
ps.close();
ps=c.prepareStatement("update customer set name=?,opening_Balance=? ,opening_Balance_Type=? where code=? ");
ps.setString(1,vName);
ps.setInt(2,customerDTOInterface.getOpeningBalance());
ps.setString(3,String.valueOf(customerDTOInterface.getOpeningBalanceType()));
ps.setInt(4,vCode);
ps.executeUpdate();
r.close();
ps.close();
c.close();
}
catch(SQLException sqlException)
{
System.out.println(sqlException);
}
}
public void delete(int code)throws DAOException
{
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("select code as c from customer where code=?");
ps.setInt(1,code);
ResultSet r=ps.executeQuery();
if(!r.next())
{
r.close();
ps.close();
c.close();
throw new DAOException("Code not exists."+code);
}
r.close();
ps.close();

ps=c.prepareStatement("delete from customer where code=?");
ps.setInt(1,code);
ps.executeUpdate();
ps.close();
c.close();
}
catch(SQLException sqlException)
{
System.out.println(sqlException);
}

}
public List<CustomerDTOInterface> getAll()throws DAOException
{
CustomerDTOInterface customerDTOInterface=null;
List<CustomerDTOInterface> customers =new LinkedList<>();
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("select * from customer ");

ResultSet r=ps.executeQuery();
if(!r.next())
{
r.close();
ps.close();
c.close();
throw new DAOException("No customers.");
} 
do
{
customerDTOInterface=new CustomerDTO();
customerDTOInterface.setCode(r.getInt("code"));
customerDTOInterface.setName(r.getString("name"));
customerDTOInterface.setOpeningBalance(r.getInt("opening_Balance"));
customerDTOInterface.setOpeningBalanceType(r.getString("opening_Balance_Type").charAt(0));
customers.add(customerDTOInterface);
} while(r.next());
r.close();
ps.close();
c.close();

}
catch(SQLException sqlException)
{
System.out.println(sqlException);
}
return customers;

}
public CustomerDTOInterface getByCode(int code)throws DAOException
{
CustomerDTOInterface customerDTOInterface=null;
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("select * from customer where code=?");
ps.setInt(1,code);
ResultSet r=ps.executeQuery();
if(!r.next())
{
r.close();
ps.close();
c.close();
throw new DAOException("Code not exists."+code);
}
customerDTOInterface=new CustomerDTO();
customerDTOInterface.setCode(r.getInt(1));
customerDTOInterface.setName(r.getString("name"));
customerDTOInterface.setOpeningBalance(r.getInt("opening_Balance"));
customerDTOInterface.setOpeningBalanceType(r.getString("opening_Balance_Type").charAt(0));
r.close();
ps.close();
c.close();

}

catch(SQLException sqlException)
{
System.out.println(sqlException);
}
return customerDTOInterface;
}
public CustomerDTOInterface getByName(String name)throws DAOException
{
CustomerDTOInterface customerDTOInterface=null;
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("select * from customer where name=?");
ps.setString(1,name);
ResultSet r=ps.executeQuery();
if(!r.next())
{
r.close();
ps.close();
c.close();
throw new DAOException("Name not exists."+name);
}
customerDTOInterface=new CustomerDTO();
customerDTOInterface.setCode(r.getInt(1));
customerDTOInterface.setName(r.getString("name"));
customerDTOInterface.setOpeningBalance(r.getInt("opening_Balance"));
customerDTOInterface.setOpeningBalanceType(r.getString("opening_Balance_Type").charAt(0));
r.close();
ps.close();
c.close();
}

catch(SQLException sqlException)
{
System.out.println(sqlException);
}
return customerDTOInterface;
}
public int  getCount()
{
int count=0;
try
{
Connection c=DAOConnection.getConnection();
ResultSet r;
PreparedStatement ps=c.prepareStatement("Select count(*) as cnt from customer");
r=ps.executeQuery();
r.next();
count=r.getInt(1);
}
catch(SQLException sqlException)
{
System.out.print(sqlException);
}
return count;
}
}