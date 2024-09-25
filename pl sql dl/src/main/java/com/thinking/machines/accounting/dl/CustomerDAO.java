package com.thinking.machines.accounting.dl;
import java.util.*;
import java.sql.*;
public class CustomerDAO implements CustomerDAOInterface 
{ 
public void add (CustomerDTOInterface customerDTOInterface)throws DAOException
{
try 
{
int Vcode=0;
Connection c=DAOConnection.getConnection();
CallableStatement cs=c.prepareCall("{call add_customer(?,?,?,?)}");
cs.registerOutParameter(1,Types.INTEGER);
cs.setString(2,customerDTOInterface.getName());
cs.setInt(3,customerDTOInterface.getOpeningBalance());
cs.setString(4,String.valueOf(customerDTOInterface.getOpeningBalanceType()));

//cs.setInt(4,4);
System.out.println("jaa rha execute hone");
cs.execute();
System.out.println("execute hua");
//Vcode=cs.getInt(1);
customerDTOInterface.setCode(Vcode);
c.close();
}
catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}

}
public void update(CustomerDTOInterface customerDTOInterface) throws DAOException
{
try
{
Connection c=DAOConnection.getConnection();
int vCode=customerDTOInterface.getCode();
String vName=customerDTOInterface.getName();
CallableStatement cs=c.prepareCall("{call update_customer(?,?,?,?)}");
cs.setInt(1,vCode);
cs.setString(2,vName);
cs.setInt(3,customerDTOInterface.getOpeningBalance());
cs.setString(4,String.valueOf(customerDTOInterface.getOpeningBalanceType()));
cs.execute();
c.close();
}
catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public void delete(int code)throws DAOException
{
try
{
Connection c=DAOConnection.getConnection();
CallableStatement cs=c.prepareCall("{call delete_customer(?)}");
cs.setInt(1,code);
cs.execute();
c.close();
}
catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
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