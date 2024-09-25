package com.thinking.machines.accounting.pl.model;
import java.io.*;
import com.thinking.machines.accounting.pl.model.event.*;
import com.thinking.machines.common.*;
public interface CustomerModelInterface
{
public enum MatchType { LEFT,FULL,ANY };
public static final MatchType 
MATCH_LEFT=MatchType.LEFT;
public static final MatchType 
MATCH_FULL=MatchType.FULL;
public static final MatchType 
MATCH_ANY=MatchType.ANY;
public int getRowCount();
public int getColumnCount();
public String getColumnName(int columnIndex); 
public boolean isCellEditable(int rowIndex,int columnIndex);
public Object getValueAt(int rowIndex,int columnIndex);
public Class getColumnClass(int ColumnIndex);
default void setValueAt(Object data,int rowIndex,int columnIndex)
{
}
public void addCustomer(CustomerInterface customerInterface) throws ModelException,ValidationException,ProcessException;
public void updateCustomer(CustomerInterface customerInterface) throws ModelException,ValidationException,ProcessException;
public void deleteCustomer(int code) throws ModelException,ValidationException,ProcessException;
public int findCustomer(String name,boolean performCaseSensitiveSearch,MatchType matchType) throws ModelException;
public void applyFilter();
public void clearFilter();
public void select(int rowIndex) throws ModelException;
public void setModelListener(ModelListener modelListener);
public void createPDF(File file) throws ModelException;
public void createPDF(File file,int pageNumber) throws ModelException;
}
