package com.thinking.machines.accounting.dl;
public class CustomerDTO implements CustomerDTOInterface 
{
private int code;
private char openingBalanceType;
private int openingBalance;
private String name;
public void setCode(int code)
{
this.code=code;
}
public int getCode()
{
return code;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return name;
}
public void setOpeningBalance(int openingBalance)
{
this.openingBalance=openingBalance;
}
public int getOpeningBalance()
{
return openingBalance;
}
public void setOpeningBalanceType(char openingBalanceType)
{
this.openingBalanceType=openingBalanceType;
}
public char getOpeningBalanceType()
{
return openingBalanceType;
}
public boolean equals(Object object )
{
if((object instanceof CustomerDTO)==false)return false;
CustomerDTO e;
e=(CustomerDTO)object;
return this.code==e.code;
}
public int compareTo(CustomerDTOInterface customerDTOInterface)
{
customerDTOInterface=new CustomerDTO();
return this.name.compareTo(customerDTOInterface.getName());
}
public int hashCode()
{
return code;
}
}