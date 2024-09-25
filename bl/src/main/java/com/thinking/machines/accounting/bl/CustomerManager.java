package com.thinking.machines.accounting.bl;
import com.thinking.machines.accounting.dl.*;
import com.thinking.machines.common.*;
import java.util.*;
import java.util.stream.*;
public class CustomerManager
{
private List<CustomerEventListener> customerEventListeners;
private HashMap<Integer,CustomerInterface> customersCodeMap;
private HashMap<String,CustomerInterface> customersNameMap;
private List<CustomerInterface> customers;
public CustomerManager()
{
customerEventListeners=new LinkedList<CustomerEventListener>();
populateDataStructure();
}
private void populateDataStructure()
{
customersCodeMap=new HashMap<Integer,CustomerInterface>();
customersNameMap=new HashMap<String,CustomerInterface>();
customers=new LinkedList<CustomerInterface>();
List<CustomerDTOInterface> list=null;
try
{
list=(new CustomerDAO()).getAll();
}catch(DAOException daoException)
{
return;
}
CustomerDTOInterface customerDTOInterface;
CustomerInterface customerInterface;
Iterator<CustomerDTOInterface> iterator=list.iterator();
int code;
String name;
while(iterator.hasNext())
{
customerDTOInterface=iterator.next();
customerInterface=new Customer();
code=customerDTOInterface.getCode();
name=customerDTOInterface.getName();
customerInterface.setCode(code);
customerInterface.setName(name);
customerInterface.setOpeningBalance(customerDTOInterface.getOpeningBalance());
customerInterface.setOpeningBalanceType((customerDTOInterface.getOpeningBalanceType()=='C')?CustomerInterface.BALANCE_TYPE.CREDIT:CustomerInterface.BALANCE_TYPE.DEBIT);
customersCodeMap.put(new Integer(code),customerInterface);
customersNameMap.put(name.toUpperCase(),customerInterface);
customers.add(customerInterface);
}
}
public void add(CustomerInterface customerInterface) throws ValidationException,ProcessException
{
List<Property> invalidProperties=new LinkedList<>();
int code=customerInterface.getCode();
String name=customerInterface.getName();
int openingBalance=customerInterface.getOpeningBalance();
String openingBalanceType=customerInterface.getOpeningBalanceType();
if(code!=0)
{
invalidProperties.add(new Property("Code","Code is auto generated."));
}
if(name==null || name.length()==0)
{
invalidProperties.add(new Property("Name","Name required."));
}
if(name.length()>100)
{
invalidProperties.add(new Property("Name","Name cannot exceed 100 characters."));
}
if(openingBalance<0)
{
invalidProperties.add(new Property("OpeningBalance","Opening balance cannot be negative"));
}

if(openingBalanceType==null)
{
if(openingBalance==0)
{
customerInterface.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
}
else
{
invalidProperties.add(new Property("OpeningBalanceType","Opening balance type required."));
}
}
if(invalidProperties.size()>0)
{
throw new ValidationException(invalidProperties);
}
if(customersNameMap.containsKey(name.toUpperCase()))
{
invalidProperties.add(new Property("Name","Customer : "+name+" exists."));
throw new ValidationException(invalidProperties);
}
try
{
CustomerDTOInterface customerDTOInterface;
customerDTOInterface=new CustomerDTO();
customerDTOInterface.setName(name);
customerDTOInterface.setOpeningBalance(openingBalance);
customerDTOInterface.setOpeningBalanceType(openingBalanceType.charAt(0));
new CustomerDAO().add(customerDTOInterface);
code=customerDTOInterface.getCode();
customerInterface.setCode(code);
CustomerInterface ci=new Customer();
ci.setCode(code);
ci.setName(name);
ci.setOpeningBalance(openingBalance);
if(openingBalanceType.equals("Dr."))
{
ci.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
}
if(openingBalanceType.equals("Cr."))
{
ci.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.CREDIT);
}
customersCodeMap.put(new Integer(code),ci);
customersNameMap.put(name.toUpperCase(),ci);
customers.add(ci);
Iterator<CustomerEventListener> iterator=customerEventListeners.iterator();
CustomerInterface ci2=new Customer();
ci2.setCode(code);
ci2.setName(name);
ci2.setOpeningBalance(openingBalance);
if(openingBalanceType.equals("Dr."))
{
ci2.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
}
if(openingBalanceType.equals("Cr."))
{
ci2.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.CREDIT);
}
CustomerEventListener listener;
while(iterator.hasNext())
{
listener=iterator.next();
listener.customerAdded(new CustomerEvent(ci2));
}
}catch(DAOException daoException)
{
throw new ProcessException(daoException.getMessage());
}

}
public void update(CustomerInterface customerInterface) throws ValidationException,ProcessException
{
List<Property> invalidProperties=new LinkedList<>();
int code=customerInterface.getCode();
String name=customerInterface.getName();
int openingBalance=customerInterface.getOpeningBalance();
String openingBalanceType=customerInterface.getOpeningBalanceType();
if(code==0)
{
invalidProperties.add(new Property("code","Code required."));
}
if(code<0)
{
invalidProperties.add(new Property("code","Invalid code."));
}
if(name==null || name.length()==0)
{
invalidProperties.add(new Property("name","Name required."));
}
if(name.length()>100)
{
invalidProperties.add(new Property("name","Name cannot exceed 100 characters."));
}
if(openingBalance<0)
{
invalidProperties.add(new Property("openingBalance","Opening balance cannot be negative"));
}

if(openingBalanceType==null)
{
if(openingBalance==0)
{
customerInterface.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
}
else
{
invalidProperties.add(new Property("openingBalanceType","Opening balance type required."));
}
}
if(invalidProperties.size()>0)
{
throw new ValidationException(invalidProperties);
}
if(!customersCodeMap.containsKey(code))
{
invalidProperties.add(new Property("Code","Invalid Code :"+code));
throw new ValidationException(invalidProperties);
}
CustomerInterface ci;
ci=customersNameMap.get(name.toUpperCase());
String vName=ci.getName();
if(ci.getName().equalsIgnoreCase(name) && code!=ci.getCode())
{
invalidProperties.add(new Property("name","Name already exists."));
throw new ValidationException(invalidProperties);
}
try
{
CustomerDTOInterface customerDTOInterface;
customerDTOInterface=new CustomerDTO();
customerDTOInterface.setCode(code);
customerDTOInterface.setName(name);
customerDTOInterface.setOpeningBalance(openingBalance);
customerDTOInterface.setOpeningBalanceType(openingBalanceType.charAt(0));
(new CustomerDAO()).update(customerDTOInterface);
ci.setName(name);
ci.setOpeningBalance(openingBalance);
if(openingBalanceType.equals("Dr."))
{
ci.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
}
if(openingBalanceType.equals("Cr."))
{
ci.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.CREDIT);
}
customersNameMap.remove(vName.toUpperCase());
customersNameMap.put(name.toUpperCase(),ci);
Iterator<CustomerEventListener> iterator=customerEventListeners.iterator();
CustomerInterface ci3=new Customer();
ci3.setCode(code);
ci3.setName(name);
ci3.setOpeningBalance(openingBalance);
if(openingBalanceType.equals("Dr."))
{
ci3.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
}
if(openingBalanceType.equals("Cr."))
{
ci3.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.CREDIT);
}
CustomerEventListener listener;
while(iterator.hasNext())
{
listener=iterator.next();
listener.customerUpdated(new CustomerEvent(ci3));
}
}catch(DAOException daoException)
{
throw new ProcessException(daoException.getMessage());
}

}
public void remove(int code) throws ValidationException,ProcessException
{
List<Property> invalidProperties=new LinkedList<>();
if(code==0)
{
invalidProperties.add(new Property("Code","Code Must be Provided"));
}
if(code<0)
{
invalidProperties.add(new Property("Code","Invalid Code"));
}
if(!(customersCodeMap.containsKey(code)))
{
invalidProperties.add(new Property("Code","Invalid Code"));
}
try
{
CustomerDTOInterface customerDTOInterface;
customerDTOInterface=new CustomerDTO();
CustomerInterface ci=customersCodeMap.get(code);
customerDTOInterface.setCode(code);
(new CustomerDAO()).delete(customerDTOInterface.getCode());
customersCodeMap.remove(code);
customersNameMap.remove(ci.getName());
customers.remove(ci);
Iterator<CustomerEventListener> iterator=customerEventListeners.iterator();
while(iterator.hasNext())
{
(iterator.next()).customerRemoved(new CustomerEvent(ci));
}
}catch(DAOException daoException)
{
throw new ProcessException(daoException.getMessage());
}
}
public CustomerInterface getByCode(int code) throws ValidationException
{
if(code==0)
{
List<Property> invalidProperties=new LinkedList<>();
invalidProperties.add(new Property("code","Code required."));
throw new ValidationException(invalidProperties);
}
if(code<0)
{
List<Property> invalidProperties=new LinkedList<>();
invalidProperties.add(new Property("code","Invalid code."));
throw new ValidationException(invalidProperties);
}
CustomerInterface customerInterface=customersCodeMap.get(code);
if(customerInterface==null)
{
List<Property> invalidProperties=new LinkedList<>();
invalidProperties.add(new Property("code","Invalid code."));
throw new ValidationException(invalidProperties);
}
CustomerInterface ci=new Customer();
ci.setCode(code);
ci.setName(customerInterface.getName());
ci.setOpeningBalance(customerInterface.getOpeningBalance());
ci.setOpeningBalanceType((customerInterface.getOpeningBalanceType().charAt(0)=='D')?CustomerInterface.BALANCE_TYPE.DEBIT:CustomerInterface.BALANCE_TYPE.CREDIT);
return ci;
}
public CustomerInterface getByName(String name) throws ValidationException
{
if(name==null || name.length()==0)
{
List<Property> invalidProperties=new LinkedList<>();
invalidProperties.add(new Property("name","name required."));
throw new ValidationException(invalidProperties);
}
CustomerInterface customerInterface=customersNameMap.get(name);
if(customerInterface==null)
{
List<Property> invalidProperties=new LinkedList<>();
invalidProperties.add(new Property("name","Invalid name."));
throw new ValidationException(invalidProperties);
}
CustomerInterface ci=new Customer();
ci.setCode(customerInterface.getCode());
ci.setName(name);
ci.setOpeningBalance(customerInterface.getOpeningBalance());
ci.setOpeningBalanceType((customerInterface.getOpeningBalanceType().charAt(0)=='D')?CustomerInterface.BALANCE_TYPE.DEBIT:CustomerInterface.BALANCE_TYPE.CREDIT);
return ci;
}
public List<CustomerInterface> getList()
{
Iterator<CustomerInterface> it=customers.iterator();
CustomerInterface ci,ci2;
List<CustomerInterface> list=new LinkedList<>();
while(it.hasNext())
{
ci=it.next();
ci2=new Customer();
ci2.setCode(ci.getCode());
ci2.setName(ci.getName());
ci2.setOpeningBalance(ci.getOpeningBalance());
ci2.setOpeningBalanceType((ci.getOpeningBalanceType().charAt(0)=='C')?CustomerInterface.BALANCE_TYPE.CREDIT:CustomerInterface.BALANCE_TYPE.DEBIT);
list.add(ci2);
}
return list;
}
public List<CustomerInterface> getListOrderedByCode()
{
List<CustomerInterface> list=getList();
Collections.sort(list,(p,q)->p.getCode()-q.getCode());
return list;
}
public List<CustomerInterface> getListOrderedByName() 
{
List<CustomerInterface> list=getList();
Collections.sort(list,(p,q)->p.getName().toUpperCase().compareTo(q.getName().toUpperCase()));
return list;
}
public List<CustomerInterface> getListByOpeningBalanceType(CustomerInterface.BALANCE_TYPE openingBalanceType) 
{
List<CustomerInterface> list1;
list1=customers.stream().filter(p->(openingBalanceType==CustomerInterface.BALANCE_TYPE.CREDIT)?p.getOpeningBalanceType().charAt(0)=='C':p.getOpeningBalanceType().charAt(0)=='D').collect(Collectors.toList());
List<CustomerInterface> list=new LinkedList<>();
list1.forEach(s->{
CustomerInterface ci=new Customer();
ci.setCode(s.getCode());
ci.setName(s.getName());
ci.setOpeningBalance(s.getOpeningBalance());
ci.setOpeningBalanceType(openingBalanceType);
list.add(ci);
});
return list;
}
public List<CustomerInterface> getListByOpeningBalanceTypeOrderedByCode(CustomerInterface.BALANCE_TYPE openingBalanceType)
{
List<CustomerInterface> list=getListByOpeningBalanceType(openingBalanceType);
Collections.sort(list,(p,q)->p.getCode()-q.getCode());
return list;
}
public List<CustomerInterface> getListByOpeningBalanceTypeOrderedByName(CustomerInterface.BALANCE_TYPE openingBalanceType)
{
List<CustomerInterface> list=getListByOpeningBalanceType(openingBalanceType);
Collections.sort(list,(p,q)->p.getCode()-q.getCode());
return list;
}
public List<CustomerInterface> getListWithOpeningBalanceZero() throws ValidationException
{
List<CustomerInterface> list1;
list1=customers.stream().filter(p->p.getOpeningBalance()==0).collect(Collectors.toList());
List<CustomerInterface> list=new LinkedList<>();
list1.forEach(s->{
CustomerInterface ci=new Customer();
ci.setCode(s.getCode());
ci.setName(s.getName());
ci.setOpeningBalance(0);
ci.setOpeningBalanceType((s.getOpeningBalanceType().charAt(0)=='D')?CustomerInterface.BALANCE_TYPE.DEBIT:CustomerInterface.BALANCE_TYPE.CREDIT);
list.add(ci);
});
return list;
}
public List<CustomerInterface> getListWithOpeningBalanceZeroOrderedByCode() throws ValidationException
{
List<CustomerInterface> list=getListWithOpeningBalanceZero();
Collections.sort(list,(p,q)->p.getCode()-q.getCode());
return list;
}
public List<CustomerInterface> getListWithOpeningBalanceZeroOrderedByName() throws ValidationException
{
List<CustomerInterface> list=getListWithOpeningBalanceZero();
Collections.sort(list,(p,q)->p.getCode()-q.getCode());
return list;
}
public int getCount()
{
return this.customers.size();
}
public long getCountWithOpeningBalanceTypeAsCredit()
{
return customers.stream().filter(p->p.getOpeningBalanceType().charAt(0)=='C').count();
}
public long getCountWithOpeningBalanceTypeAsDebit()
{
return customers.stream().filter(p->p.getOpeningBalanceType().charAt(0)=='D').count();
}
public long getCountWithOpeningBalanceZero()
{
return customers.stream().filter(p->p.getOpeningBalance()==0).count();
}
}
