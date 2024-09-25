package com.thinking.machines.accounting.pl.model;
import java.io.*;
import com.thinking.machines.accounting.pl.model.event.*;
import com.thinking.machines.accounting.bl.*;
import javax.swing.table.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.stream.*;
import java.util.*;
import com.thinking.machines.common.*;
import com.thinking.machines.common.util.*;
public class CustomerModel extends AbstractTableModel implements CustomerModelInterface
{
private com.thinking.machines.accounting.bl.CustomerManager customerManager;
private ModelListener modelListener;
private String []titles={"S.No.","Customer"};
private java.util.List<com.thinking.machines.accounting.bl.CustomerInterface> customersList;
private int selectedIndex;
private boolean isFilterApplied;
private java.util.List<com.thinking.machines.accounting.bl.CustomerInterface> list;
public CustomerModel()
{
this.customerManager=new com.thinking.machines.accounting.bl.CustomerManager();
populateModel();
this.selectedIndex=-1;
this.modelListener=null;
this.isFilterApplied=false;
}
private void populateModel()
{
this.customersList=this.customerManager.getListOrderedByName();
// more code for other data structures vo tree vala krna
}
public int getRowCount()
{
if(isFilterApplied) return this.list.size();
return this.customersList.size();
}
public int getColumnCount()
{
return this.titles.length;
}
public String getColumnName(int columnIndex)
{
return this.titles[columnIndex];
}
public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0) return Integer.class;
return String.class;
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0)
{
return rowIndex+1;
}
return customersList.get(rowIndex).getName();
}
public int search(String name)
{
int found=0;
int index=0;
for(com.thinking.machines.accounting.bl.CustomerInterface blCustomer:customersList)
{
if(blCustomer.getName().equalsIgnoreCase(name))
{
found=1;
break;
}
index++;
}
if(found==0) return -1;
return index;
}
public CustomerInterface getCustomerAt(int index) throws ModelException
{
CustomerInterface customer=new Customer();
com.thinking.machines.accounting.bl.CustomerInterface blCustomer=customersList.get(index);
if(blCustomer==null) throw new ModelException("Invalid Index");
try
{
PojoCopier.copy(customer,blCustomer);
customer.setOpeningBalanceType((blCustomer.getOpeningBalanceType().charAt(0)=='D')?CustomerInterface.BALANCE_TYPE.DEBIT:CustomerInterface.BALANCE_TYPE.CREDIT);
}catch(Throwable t)
{
}
return customer;
}

public void addCustomer(CustomerInterface customerInterface) throws ModelException,ValidationException,ProcessException
{

try
{
customersList.forEach(ci->{ if(ci.getName().equalsIgnoreCase(customerInterface.getName())){
throw new RuntimeException("Customer : "+customerInterface.getName()+" exists.");
}});
}catch(RuntimeException runtimeException)
{
throw new ModelException(runtimeException.getMessage());
}
com.thinking.machines.accounting.bl.CustomerInterface blCustomer;
blCustomer=new com.thinking.machines.accounting.bl.Customer();
blCustomer.setName(customerInterface.getName());
blCustomer.setOpeningBalance(customerInterface.getOpeningBalance());
blCustomer.setOpeningBalanceType((customerInterface.getOpeningBalanceType().charAt(0)=='D')?com.thinking.machines.accounting.bl.CustomerInterface.BALANCE_TYPE.DEBIT:com.thinking.machines.accounting.bl.CustomerInterface.BALANCE_TYPE.CREDIT);
customerManager.add(blCustomer);
customerInterface.setCode(blCustomer.getCode());
int insertAtIndex;
String upperCasedName=blCustomer.getName().toUpperCase();
insertAtIndex=0;
if(customersList.size()>0)
{
if(upperCasedName.compareTo(customersList.get(0).getName().toUpperCase())<0)
{
insertAtIndex=0;
}
else if(upperCasedName.compareTo(customersList.get(customersList.size()-1).getName().toUpperCase())>0)
{
insertAtIndex=customersList.size();
}
else 
{
int low,high,index;
low=0;
high=customersList.size()-1;
com.thinking.machines.accounting.bl.CustomerInterface previous,mid;
int previousWeight,midWeight;
while(low<=high)
{
index=(int)(low+high)/2;
if(index==low || index==high) break;
mid=customersList.get(index);
previous=customersList.get(index-1);
midWeight=mid.getName().toUpperCase().compareTo(upperCasedName);
previousWeight=previous.getName().toUpperCase().compareTo(upperCasedName);
if(midWeight>0 && previousWeight<0)
{
insertAtIndex=index;
break;
}
if(previousWeight>0)
{
high=index-1;
}
else
{
low=index+1;
}
} // loop
} // else
} // size()>0 

customersList.add(insertAtIndex,blCustomer);
raiseCustomerAddedEvent(insertAtIndex,blCustomer);
}
public void updateCustomer(CustomerInterface customerInterface) throws ModelException,ValidationException,ProcessException
{
boolean found=customersList.stream().anyMatch(p->p.getCode()==customerInterface.getCode());
if(found==false) throw new ModelException("Invalid Code");
found=customersList.stream().anyMatch(p->(p.getCode()!=customerInterface.getCode() && p.getName().equalsIgnoreCase(customerInterface.getName())));
if(found==true) throw new ModelException("Name Exists on different Code");
Iterator<com.thinking.machines.accounting.bl.CustomerInterface> i=customersList.iterator();
com.thinking.machines.accounting.bl.CustomerInterface ci;
ci=customersList.get(0);
int insertAtIndex=0;
while(i.hasNext())
{
ci=i.next();
if(ci.getCode()==customerInterface.getCode())
{
break;
}
insertAtIndex++;
}
com.thinking.machines.accounting.bl.CustomerInterface blCustomer;
blCustomer=new com.thinking.machines.accounting.bl.Customer();
blCustomer.setName(customerInterface.getName());
blCustomer.setOpeningBalance(customerInterface.getOpeningBalance());
blCustomer.setOpeningBalanceType((customerInterface.getOpeningBalanceType().charAt(0)=='D')?com.thinking.machines.accounting.bl.CustomerInterface.BALANCE_TYPE.DEBIT:com.thinking.machines.accounting.bl.CustomerInterface.BALANCE_TYPE.CREDIT);
blCustomer.setCode(customerInterface.getCode());
String upperCasedName=customerInterface.getName().toUpperCase();
if(ci.getName().equalsIgnoreCase(customerInterface.getName()))
{
ci.setOpeningBalance(customerInterface.getOpeningBalance());
ci.setOpeningBalanceType((customerInterface.getOpeningBalanceType().charAt(0)=='D')?com.thinking.machines.accounting.bl.CustomerInterface.BALANCE_TYPE.DEBIT:com.thinking.machines.accounting.bl.CustomerInterface.BALANCE_TYPE.CREDIT);
}
else
{
customersList.remove(ci);
if(upperCasedName.compareTo(customersList.get(0).getName().toUpperCase())<0)
{
insertAtIndex=0;
}
else if(upperCasedName.compareTo(customersList.get(customersList.size()-1).getName().toUpperCase())>0)
{
insertAtIndex=customersList.size();
}
else 
{
int low,high,index;
low=0;
high=customersList.size()-1;
com.thinking.machines.accounting.bl.CustomerInterface previous,mid;
int previousWeight,midWeight;
while(low<=high)
{
index=(int)(low+high)/2;
if(index==low || index==high) break;
mid=customersList.get(index);
previous=customersList.get(index-1);
midWeight=mid.getName().toUpperCase().compareTo(upperCasedName);
previousWeight=previous.getName().toUpperCase().compareTo(upperCasedName);
if(midWeight>0 && previousWeight<0)
{
insertAtIndex=index;
break;
}
if(previousWeight>0)
{
high=index-1;
}
else
{
low=index+1;
}
} // loop 
} // else vala loop
customersList.add(insertAtIndex,blCustomer);
}//if ! wali condition
customerManager.update(blCustomer);
raiseCustomerUpdatedEvent(insertAtIndex,blCustomer);
}
public void deleteCustomer(int code) throws ModelException,ValidationException,ProcessException
{
boolean found=customersList.stream().anyMatch(p->p.getCode()==code);
if(found==false) throw new ModelException("Invalid Code");
this.customerManager.remove(code);
com.thinking.machines.accounting.bl.CustomerInterface customerInterface=null;
int index=0;
for(com.thinking.machines.accounting.bl.CustomerInterface c:customersList)
{
if(c.getCode()==code)
{
customerInterface=c;
break;
}
index++;
}
customersList.remove(customerInterface);
raiseCustomerDeletedEvent(index,customerInterface);
}
//return index of customer
public int findCustomer(String name,boolean performCaseSensitiveSearch,MatchType matchType) throws ModelException
{
if(matchType==MatchType.LEFT)
{
//aab agar filter bhi apply he toh
if(isFilterApplied)
{
if(!performCaseSensitiveSearch) 
this.list=customersList.stream().filter(p->p.getName().startsWith(name)).collect(Collectors.toList());
else this.list=customersList.stream().filter(p->p.getName().toUpperCase().startsWith(name.toUpperCase())).collect(Collectors.toList());
}
int index=0,found=0;
for(com.thinking.machines.accounting.bl.CustomerInterface c:customersList)
{
if(!performCaseSensitiveSearch)
{
if(c.getName().startsWith(name))
{
found=1;
break;
}
}
else
{
if(c.getName().toUpperCase().startsWith(name.toUpperCase()))
{
found=1;
break;
}
}
index++;
}
if(found==0) return -1;
return index;
}
if(matchType==MatchType.FULL)
{
if(isFilterApplied)
{
if(performCaseSensitiveSearch)  this.list=customersList.stream().filter(p->p.getName().equals(name)).collect(Collectors.toList());
else this.list=customersList.stream().filter(p->p.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
}
int index=0,found=0;
for(com.thinking.machines.accounting.bl.CustomerInterface c:customersList)
{
if(performCaseSensitiveSearch)
{
if(c.getName().equals(name))
{
found=1;
break;
}
}
else
{
if(c.getName().equalsIgnoreCase(name))
{
found=1;
break;
}
}
index++;
}
if(found==0) return -1;
return index;
}
if(matchType==MatchType.ANY)
{
if(isFilterApplied)
{
if(performCaseSensitiveSearch)  this.list=customersList.stream().filter(p->p.getName().contains(name)).collect(Collectors.toList());
else this.list=customersList.stream().filter(p->p.getName().toUpperCase().contains(name.toUpperCase())).collect(Collectors.toList());
}
int index=0,found=0;
for(com.thinking.machines.accounting.bl.CustomerInterface c:customersList)
{
if(performCaseSensitiveSearch)
{
if(c.getName().contains(name))
{
found=1;
break;
}
}
else
{
if(c.getName().toUpperCase().contains(name.toUpperCase()))
{
found=1;
break;
}
}
index++;
}
if(found==0) return -1;
return index;
}
return -1;
}
public void applyFilter()
{
isFilterApplied=true;
}
public void clearFilter()
{
isFilterApplied=false;
}
public void select(int rowIndex) throws ModelException
{
throw new ModelException("Yet not implemented.");
}
public void setModelListener(ModelListener modelListener)
{
this.modelListener=modelListener;
}
public void createPDF(File file,int pageNumber) throws ModelException
{
throw new ModelException("yet not implemented");
}
public void createPDF(File file) throws ModelException
{
try
{
int pageSz=40;
Document document = new Document();
PdfWriter.getInstance(document, new FileOutputStream(file.getName()));
document.open();
boolean newPage=true;
int x=0;
int pageNo=0;
Paragraph paragraph=new Paragraph();
PdfPTable table=null;
Paragraph paragraph1=new Paragraph();
Paragraph paragraph2=new Paragraph();
CustomerInterface customer=null;
PdfPCell cell=null;
while(x<customersList.size())
{
if(newPage)
{
pageNo++;
document.add(new Paragraph("Prayukti Jain"));
paragraph.add("CUSTOMERS LIST");
paragraph.setFont(new Font());
paragraph.setAlignment(Element.ALIGN_CENTER);
document.add(paragraph);
paragraph2.add("Page No. : "+pageNo);
paragraph2.setFont(new Font());
paragraph2.setAlignment(Element.ALIGN_RIGHT);
document.add(paragraph2);
document.add( Chunk.NEWLINE );
newPage=false;
table=new PdfPTable(5);
table.setWidthPercentage(100);
table.setSpacingBefore(0f);
table.setSpacingAfter(0f);
cell=new PdfPCell(new Paragraph("S. No."));
table.addCell(cell);
cell=new PdfPCell(new Paragraph("Name"));
table.addCell(cell);
cell=new PdfPCell(new Paragraph("Code"));
table.addCell(cell);
cell=new PdfPCell(new Paragraph("Opening Balance"));
table.addCell(cell);
cell=new PdfPCell(new Paragraph("Opening Balance Type"));
table.addCell(cell);
}
customer=this.getCustomerAt(x);
cell=new PdfPCell(new Paragraph((x+1)+"."));
table.addCell(cell);
cell=new PdfPCell(new Paragraph(customer.getName()));
table.addCell(cell);
cell=new PdfPCell(new Paragraph(String.valueOf(customer.getCode())));
table.addCell(cell);
cell=new PdfPCell(new Paragraph(customer.getOpeningBalance()+"/-"));
table.addCell(cell);
cell=new PdfPCell(new Paragraph((customer.getOpeningBalanceType().charAt(0)=='C')?"Credit":"Debit"));
table.addCell(cell);
x++;
if(x%pageSz==0 || x==customersList.size())
{
document.add( Chunk.NEWLINE );
document.add(table);
paragraph1.add("Date : ");
paragraph1.setFont(new Font());
paragraph1.setAlignment(Element.ALIGN_LEFT);
document.add(paragraph1);
if(x<customersList.size())
{
newPage=true;
document.newPage();
}
}
} 
document.close();
}catch(FileNotFoundException e) 
{
throw new ModelException(e.getMessage());
}
catch(DocumentException e) 
{
throw new ModelException(e.getMessage());
}
}


// event raisers
private void raiseCustomerUpdatedEvent(int insertedAt,com.thinking.machines.accounting.bl.CustomerInterface blCustomer)
{
if(this.modelListener==null) return;
CustomerInterface customer=new Customer();
try
{
PojoCopier.copy(customer,blCustomer);
ModelEvent modelEvent;
modelEvent=new ModelEvent(customer,insertedAt);
this.modelListener.customerUpdated(modelEvent);
}catch(Throwable throwable)
{
System.out.println(throwable); // remove this and the next 2 lines after checking
System.out.println("POJO Copy failed in CustomerModel.java");
System.exit(0);
}
}

private void raiseCustomerDeletedEvent(int insertedAt,com.thinking.machines.accounting.bl.CustomerInterface blCustomer)
{
if(this.modelListener==null) return;
CustomerInterface customer=new Customer();
try
{
PojoCopier.copy(customer,blCustomer);
ModelEvent modelEvent;
modelEvent=new ModelEvent(customer,insertedAt);
this.modelListener.customerDeleted(modelEvent);
}catch(Throwable throwable)
{
System.out.println(throwable); // remove this and the next 2 lines after checking
System.out.println("POJO Copy failed in CustomerModel.java");
System.exit(0);
}
}
private void raiseCustomerAddedEvent(int insertedAt,com.thinking.machines.accounting.bl.CustomerInterface blCustomer)
{
if(this.modelListener==null) return;
CustomerInterface customer=new Customer();
try
{
PojoCopier.copy(customer,blCustomer);
ModelEvent modelEvent;
modelEvent=new ModelEvent(customer,insertedAt);
this.modelListener.customerAdded(modelEvent);
}catch(Throwable throwable)
{
System.out.println(throwable); // remove this and the next 2 lines after checking
System.out.println("POJO Copy failed in CustomerModel.java");
System.exit(0);
}
}
}
