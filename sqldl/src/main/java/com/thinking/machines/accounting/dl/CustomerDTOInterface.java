package com.thinking.machines.accounting.dl;
public interface CustomerDTOInterface extends java.io.Serializable,Comparable<CustomerDTOInterface> 
{
public void setCode(int code);
public int getCode();
public void setName(String name);
public String getName();
public void setOpeningBalance(int openingBalance);
public int getOpeningBalance();
public void setOpeningBalanceType(char openingBalanceType);
public char getOpeningBalanceType();
public boolean equals(Object object);
public int hashCode();
}