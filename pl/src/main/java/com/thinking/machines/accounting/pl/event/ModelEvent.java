package com.thinking.machines.accounting.pl.model.event;
import com.thinking.machines.accounting.pl.model.*;
import java.io.*;
public class ModelEvent
{
private CustomerInterface customerInterface;
private int index;
private File pdfFile;
public ModelEvent(int index)
{
this.index=index;
}
public ModelEvent(CustomerInterface customerInterface)
{
this.customerInterface=customerInterface;
}
public ModelEvent(File pdfFile)
{
this.pdfFile=pdfFile;
this.index=-1;
}
public ModelEvent(CustomerInterface customerInterface,int index)
{
this.customerInterface=customerInterface;
this.index=index;
}

public CustomerInterface getCustomer()
{
return this.customerInterface;
}
public Integer getIndex()
{
return this.index;
}
public File getPdfFile()
{
return this.pdfFile;
}
}
