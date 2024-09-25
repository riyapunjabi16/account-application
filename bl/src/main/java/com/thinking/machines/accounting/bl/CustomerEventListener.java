package com.thinking.machines.accounting.bl;
public interface CustomerEventListener
{
public void customerAdded(CustomerEvent customerEvent);
public void customerUpdated(CustomerEvent customerEvent);
public void customerRemoved(CustomerEvent customerEvent); 
}