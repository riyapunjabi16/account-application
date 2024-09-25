package com.thinking.machines.accounting.bl;
public interface CustomerInterface extends java.io.Serializable,Comparable<CustomerInterface> 
{
public enum BALANCE_TYPE{DEBIT,CREDIT};
public final BALANCE_TYPE DEBIT=BALANCE_TYPE.DEBIT; 
public final BALANCE_TYPE CREDIT=BALANCE_TYPE.CREDIT; 

public void setCode(int code);
public int getCode();
public void setName(String name);
public String getName();
public void setOpeningBalance(int openingBalance);
public int getOpeningBalance();
public void setOpeningBalanceType(BALANCE_TYPE openingBalanceType);
public String getOpeningBalanceType();
public boolean equals(Object object);
public int hashCode();
}