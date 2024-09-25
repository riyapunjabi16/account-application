package com.thinking.machines.accounting.dl;
import java.util.*;
public interface CustomerDAOInterface 
{
public void add(CustomerDTOInterface customerDTOInterface)throws DAOException;
public void update(CustomerDTOInterface customerDTOInterface)throws DAOException;
public void delete(int code)throws DAOException;
public List<CustomerDTOInterface> getAll()throws DAOException;
public CustomerDTOInterface getByCode(int code)throws DAOException;
public CustomerDTOInterface getByName(String name)throws DAOException;
public int getCount();
}