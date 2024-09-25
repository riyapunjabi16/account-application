package com.thinking.machines.accounting.bl;
public class CustomerEvent
{
private CustomerInterface customerInterface;
public CustomerEvent()
{
customerInterface=null;
}
public CustomerEvent(CustomerInterface customerInterface)
{
this.customerInterface=customerInterface;
}
public CustomerInterface getCustomer()
{
return this.customerInterface;
} 
}