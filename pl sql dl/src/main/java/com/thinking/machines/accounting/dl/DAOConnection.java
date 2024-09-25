package com.thinking.machines.accounting.dl;
import java.sql.*;
public class DAOConnection
{
public static Connection getConnection()
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/uec20187amdb","uec20187am","uec20187am");

return c;
}
catch(SQLException sqlException)
{
//later we will change it
System.out.println(sqlException);
}
catch(ClassNotFoundException cnfe)
{
System.out.println(cnfe);
return null;
}
return null;
}
}
