package com.thinking.machines.accounting.dl;
import com.thinking.machines.seq.*;
import java.util.*;
import java.io.*;
public class CustomerDAO implements CustomerDAOInterface 
{
private static final String DATA_FILE="Customers.dat"; 
public void add (CustomerDTOInterface customerDTOInterface)throws DAOException
{
try
{
File file;
file=new File(DATA_FILE);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
int vCode;
String vName;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
vName=randomAccessFile.readLine();
if(vName.equalsIgnoreCase(customerDTOInterface.getName())) 
{
throw new DAOException("Customer Name Exists"+vName);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
}
try
{
vCode=SequenceEngine.getNext(new Sequence(){
public int getStep()
{
return 1;
}
public int getStartFrom()
{
return 1;
}
public  String getName()
{
return "Customers";
}
});
}
catch(SequenceException sequenceException)
{
randomAccessFile.close();
throw new DAOException(sequenceException.getMessage());
}
randomAccessFile.writeBytes(String.valueOf(vCode));
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(customerDTOInterface.getName()+"\n");
randomAccessFile.writeBytes(String.valueOf(customerDTOInterface.getOpeningBalance())+"\n");
randomAccessFile.writeBytes(customerDTOInterface.getOpeningBalanceType()+"\n");
randomAccessFile.close();
}
catch(IOException io)
{
throw new DAOException(io.getMessage());
}
}
public void update(CustomerDTOInterface customerDTOInterface) throws DAOException
{
try
{
File file;
File tFile;
tFile=new File("temp.xid");
if(tFile.exists()) tFile.delete();
file=new File(DATA_FILE);
if(file.exists()==false)
throw new DAOException("No customers.");
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
RandomAccessFile tRandomAccessFile=new RandomAccessFile(tFile,"rw");
int vCode=0;
int codeFound=0;
int nameFound=0;
String vName=null;
int code=customerDTOInterface.getCode();
String name=customerDTOInterface.getName();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vCode=Integer.parseInt(randomAccessFile.readLine());
vName=randomAccessFile.readLine();
if(vCode==code) codeFound=1;
if(codeFound==1 && nameFound==1 )
{
throw new DAOException("Name exists.");
}
if(vName.equalsIgnoreCase(name)) nameFound=1;

if(vCode!=code)
{
tRandomAccessFile.writeBytes(String.valueOf(vCode)+"\n");
tRandomAccessFile.writeBytes(vName+"\n");
tRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
tRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
else
{
tRandomAccessFile.writeBytes(String.valueOf(vCode)+"\n");
tRandomAccessFile.writeBytes(customerDTOInterface.getName()+"\n");
tRandomAccessFile.writeBytes(customerDTOInterface.getOpeningBalance()+"\n");
tRandomAccessFile.writeBytes(customerDTOInterface.getOpeningBalanceType()+"\n");
randomAccessFile.readLine();
randomAccessFile.readLine();
}
}
if(codeFound==0) 
throw new DAOException("Code not exists."+ customerDTOInterface.getCode());
randomAccessFile.seek(0);
tRandomAccessFile.seek(0);
while(tRandomAccessFile.getFilePointer()<tRandomAccessFile.length())
{
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tRandomAccessFile.length());
randomAccessFile.close();
tRandomAccessFile.close();
}
catch(IOException io)
{
throw new DAOException(io.getMessage());
}
}
public void delete(int code)throws DAOException
{
try
{
File file;
File tFile;
tFile=new File("temp.xid");
if(tFile.exists()) tFile.delete();
file=new File(DATA_FILE);
if(file.exists()==false)
throw new DAOException("No customers.");
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
RandomAccessFile tRandomAccessFile=new RandomAccessFile(tFile,"rw");
int vCode=0;
int codeFound=0;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vCode=Integer.parseInt(randomAccessFile.readLine());
if(vCode!=code)
{
tRandomAccessFile.writeBytes(String.valueOf(vCode)+"\n");
tRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
tRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
tRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
else
{
codeFound=1;
randomAccessFile.readLine();
randomAccessFile.readLine();
randomAccessFile.readLine();
}
}
if(codeFound==0) throw new DAOException("code not exists"+code);
randomAccessFile.seek(0);
tRandomAccessFile.seek(0);
while(tRandomAccessFile.getFilePointer()<tRandomAccessFile.length())
{
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
randomAccessFile.writeBytes(tRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tRandomAccessFile.length());
randomAccessFile.close();
tRandomAccessFile.close();
}
catch(IOException io)
{
throw new DAOException(io.getMessage());
}
}
public List<CustomerDTOInterface> getAll()throws DAOException
{
try
{
File file;
file=new File(DATA_FILE);
if(file.exists()==false)
throw new DAOException("no customers");
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"r");
List<CustomerDTOInterface> customers;
customers= new LinkedList<CustomerDTOInterface>();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
CustomerDTOInterface customerDTOInterface;
customerDTOInterface= new CustomerDTO();
customerDTOInterface.setCode(Integer.parseInt(randomAccessFile.readLine())); 
customerDTOInterface.setName(randomAccessFile.readLine());
customerDTOInterface.setOpeningBalance(Integer.parseInt(randomAccessFile.readLine()));
customerDTOInterface.setOpeningBalanceType(randomAccessFile.readLine().charAt(0));
customers.add(customerDTOInterface);
}
return customers;
}
catch(IOException io)
{
throw new DAOException(io.getMessage());
}
}
public CustomerDTOInterface getByCode(int code)throws DAOException
{
try
{
File file;
file=new File(DATA_FILE);
if(file.exists()==false)
throw new DAOException("no customers");
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"r");
CustomerDTOInterface customerDTOInterface;
customerDTOInterface=new CustomerDTO();
int vCode,vOpeningBalance;
String vName;
char vOpeningBalanceType;
vCode=0;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vCode=Integer.parseInt(randomAccessFile.readLine());
vName=randomAccessFile.readLine();
vOpeningBalance=Integer.parseInt(randomAccessFile.readLine());
vOpeningBalanceType=randomAccessFile.readLine().charAt(0);
if(vCode==code)
{
customerDTOInterface.setCode(vCode);
customerDTOInterface.setName(vName);
customerDTOInterface.setOpeningBalance(vOpeningBalance);
customerDTOInterface.setOpeningBalanceType(vOpeningBalanceType);
break;
}
}
if(vCode!=code) throw new DAOException(code+" not exists ");
return customerDTOInterface;
}
catch(IOException io)
{
throw new DAOException(io.getMessage());
}
}
public CustomerDTOInterface getByName(String name)throws DAOException
{
try
{
File file;
file=new File(DATA_FILE);
if(file.exists()==false)
throw new DAOException("no customers");
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"r");
CustomerDTOInterface customerDTOInterface;
customerDTOInterface=new CustomerDTO();
int vCode,vOpeningBalance;
String vName;
char vOpeningBalanceType;
vName=null;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vCode=Integer.parseInt(randomAccessFile.readLine());
vName=randomAccessFile.readLine();
vOpeningBalance=Integer.parseInt(randomAccessFile.readLine());
vOpeningBalanceType=randomAccessFile.readLine().charAt(0);
if(vName.equalsIgnoreCase(name))
{
customerDTOInterface.setCode(vCode);
customerDTOInterface.setName(vName);
customerDTOInterface.setOpeningBalance(vOpeningBalance);
customerDTOInterface.setOpeningBalanceType(vOpeningBalanceType);
break;
}
}
if(vName.equalsIgnoreCase(name)==false) throw new DAOException(name+" not exists ");
return customerDTOInterface;
}
catch(IOException io)
{
throw new DAOException(io.getMessage());
}
}
}