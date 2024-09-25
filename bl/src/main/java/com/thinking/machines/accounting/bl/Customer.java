package com.thinking.machines.accounting.bl;
public class Customer implements CustomerInterface 
{
public int code;
public String name;
public int openingBalance;
public String openingBalanceType;
public Customer()
{
this.code=0;
this.name="";
this.openingBalance=0;
this.openingBalanceType=null;
}
public void setCode(int code)
{
this.code=code;
}
public int getCode()
{
return this.code;
}
public void setName(String name)
{
this.name=name.trim();
}
public String getName()
{
return this.name;
}
public void setOpeningBalance(int openingBalance)
{
this.openingBalance=openingBalance;
}
public int getOpeningBalance()
{
return this.openingBalance;
}
public void setOpeningBalanceType(BALANCE_TYPE openingBalanceType)
{
if(openingBalanceType==BALANCE_TYPE.CREDIT)
{
this.openingBalanceType="Cr.";
}
else
{
this.openingBalanceType="Dr.";
}
}
public String getOpeningBalanceType()
{
return openingBalanceType; 
}
public boolean equals(Object object)
{
if(!(object instanceof CustomerInterface)) return false;
CustomerInterface customerInterface=(CustomerInterface) object;
return this.code==customerInterface.getCode();
}
public int compareTo(CustomerInterface customerInterface)
{
return this.name.toUpperCase().compareTo(customerInterface.getName().toUpperCase());
}
public int hashCode()
{
return code;
}
}

