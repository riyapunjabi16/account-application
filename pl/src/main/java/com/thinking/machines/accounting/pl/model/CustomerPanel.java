package com.thinking.machines.accounting.pl.model;
import java.lang.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import com.thinking.machines.accounting.pl.model.*;
import com.thinking.machines.common.*;
public class CustomerPanel extends JPanel implements DocumentListener,ListSelectionListener,ActionListener,ItemListener
{
private enum MODE{VIEW_MODE,ADD_MODE,EDIT_MODE,DELETE_MODE,EXPORT_TO_PDF_MODE};
private MODE mode;
private JLabel moduleTitle;
private JLabel searchCaptionLabel;
private JTextField searchTextField;
private JButton clearSearchTextFieldButton;
private JLabel searchErrorLabel;
private ButtonGroup matchTypeGroup;
private JRadioButton left,full,any;
private JCheckBox matchCase,filter;
private JTable table;
private JScrollPane jsp;
private CustomerModel customerModel;
private CustomerDetailsPanel customerDetailsPanel;
public CustomerPanel()
{
customerModel=new CustomerModel();
initComponents();
setAppearance();
addListeners();
setViewMode();
customerDetailsPanel.setViewMode();
}
private void initComponents()
{
moduleTitle=new JLabel("Customers");
searchCaptionLabel=new JLabel("Search");
searchTextField=new JTextField();
clearSearchTextFieldButton=new JButton("X");
searchErrorLabel=new JLabel("");
left=new JRadioButton("Left");
full=new JRadioButton("Full");
any=new JRadioButton("Any");
matchTypeGroup=new ButtonGroup();
matchTypeGroup.add(left);
matchTypeGroup.add(full);
matchTypeGroup.add(any);
matchCase=new JCheckBox("Match Case");
filter=new JCheckBox("Filter");
table=new JTable(customerModel);
jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
customerDetailsPanel=new CustomerDetailsPanel();
setLayout(null);
int lm=5;
moduleTitle.setBounds(lm+5,5,400,50);
searchCaptionLabel.setBounds(lm+5,60,60,30);
searchTextField.setBounds(lm+65,60,400,30);
clearSearchTextFieldButton.setBounds(lm+467,60,30,30);
searchErrorLabel.setBounds(lm+405,40,75,20);
matchCase.setBounds(lm+65,100,150,30);
filter.setBounds(lm+65+150,100,80,30);
left.setBounds(lm+65+150+150,100,80,30);
full.setBounds(lm+65+150+150+80,100,80,30);
any.setBounds(lm+65+150+150+160,100,80,30);
jsp.setBounds(lm+5,150,663,250);
customerDetailsPanel.setBounds(lm+5,400,663,250);
add(moduleTitle);
add(searchCaptionLabel);
add(searchTextField);
add(clearSearchTextFieldButton);
add(searchErrorLabel);
add(matchCase);
add(filter);
add(left);
add(full);
add(any);
add(jsp);
add(customerDetailsPanel);
}
private void setAppearance()
{
Font moduleTitleFont=new Font("Verdana",Font.BOLD,20);
moduleTitle.setFont(moduleTitleFont);
Font font=new Font("Verdana",Font.PLAIN,16);
Font searchErrorFont=new Font("Verdana",Font.BOLD,10);
searchCaptionLabel.setFont(font);
searchTextField.setFont(font);
searchErrorLabel.setFont(searchErrorFont);
searchErrorLabel.setForeground(new Color(111,0,0));
matchCase.setFont(font);
filter.setFont(font);
left.setFont(font);
full.setFont(font);
any.setFont(font);
table.setRowHeight(30);
table.setFont(font);
Font tableTitleFont=new Font("Verdana",Font.BOLD,16);
table.getTableHeader().setFont(tableTitleFont);
table.getColumnModel().getColumn(0).setPreferredWidth(100);
table.getColumnModel().getColumn(1).setPreferredWidth(560);
table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
table.getTableHeader().setResizingAllowed(false);
table.getTableHeader().setReorderingAllowed(false);
}
private void addListeners()
{
searchTextField.getDocument().addDocumentListener(this);
table.getSelectionModel().addListSelectionListener(this);
clearSearchTextFieldButton.addActionListener(this);
filter.addItemListener(this);
matchCase.addItemListener(this);
left.addActionListener(this);
full.addActionListener(this);
any.addActionListener(this);
}
private void setAddMode()
{
this.mode=MODE.ADD_MODE;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
matchCase.setEnabled(false);
filter.setEnabled(false);
left.setEnabled(false);
full.setEnabled(false);
any.setEnabled(false);
table.setEnabled(false);
}
private void setDeleteMode()
{
this.mode=MODE.DELETE_MODE;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
matchCase.setEnabled(false);
filter.setEnabled(false);
left.setEnabled(false);
full.setEnabled(false);
any.setEnabled(false);
table.setEnabled(false);
}
private void setEditMode()
{
this.mode=MODE.EDIT_MODE;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
matchCase.setEnabled(false);
filter.setEnabled(false);
left.setEnabled(false);
full.setEnabled(false);
any.setEnabled(false);
table.setEnabled(false);
}
private void setViewMode()
{
this.mode=MODE.VIEW_MODE;
if(customerModel.getRowCount()==0)
{
searchTextField.setText("");
searchErrorLabel.setText("");
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
matchCase.setEnabled(false);
filter.setEnabled(false);
left.setEnabled(false);
full.setEnabled(false);
any.setEnabled(false);
table.setEnabled(false);
}
else
{
searchTextField.setEnabled(true);
clearSearchTextFieldButton.setEnabled(true);
matchCase.setEnabled(true);
filter.setEnabled(true);
left.setEnabled(true);
full.setEnabled(true);
any.setEnabled(true);
table.setEnabled(true);
}
}
//done
private void search()
{
searchErrorLabel.setText("");
String searchWhat=searchTextField.getText().trim();
if(searchWhat.length()==0) return;
int index=-1;
try
{
if(left.isSelected())  index=customerModel.findCustomer(searchWhat,matchCase.isSelected(),com.thinking.machines.accounting.pl.model.CustomerModelInterface.MatchType.LEFT);
else
{
if(any.isSelected())  index=customerModel.findCustomer(searchWhat,matchCase.isSelected(),com.thinking.machines.accounting.pl.model.CustomerModelInterface.MatchType.ANY);
else  index=customerModel.findCustomer(searchWhat,matchCase.isSelected(),com.thinking.machines.accounting.pl.model.CustomerModelInterface.MatchType.FULL);
}
}catch(ModelException modelException)
{
}
if(index==-1)
{
searchErrorLabel.setText("Not found");
return;
}
customerModel.fireTableDataChanged();
table.setRowSelectionInterval(index,index);
table.scrollRectToVisible(new Rectangle(table.getCellRect(index,0,false)));
}
public void insertUpdate(DocumentEvent ev)
{
search();
}
public void removeUpdate(DocumentEvent ev)
{
search();
}
public void changedUpdate(DocumentEvent ev)
{
search();
}
public void itemStateChanged(ItemEvent ev)
{
if(ev.getItemSelectable()==filter)
{	
if(filter.isSelected()) 
{
customerModel.applyFilter();
search();
}
else
{
customerModel.clearFilter();
search();
}
}
if(ev.getItemSelectable()==matchCase)
{
}
}
//
public void actionPerformed(ActionEvent ev)
{
if(ev.getSource()==clearSearchTextFieldButton)
{
searchTextField.setText("");
searchErrorLabel.setText("");
searchTextField.requestFocus();
}
if(ev.getSource()==left)
{
if(left.isSelected()) search();
else
{
searchTextField.setText("");
searchErrorLabel.setText("");
searchTextField.requestFocus();
}
}
if(ev.getSource()==full)
{
if(full.isSelected()) search();
else
{
searchTextField.setText("");
searchErrorLabel.setText("");
searchTextField.requestFocus();
}
}
if(ev.getSource()==any)
{
if(any.isSelected()) search();
else
{
searchTextField.setText("");
searchErrorLabel.setText("");
searchTextField.requestFocus();
}
}
}
public void valueChanged(ListSelectionEvent ev)
{
int selectedIndex=table.getSelectedRow();
if(selectedIndex==-1) 
{
customerDetailsPanel.setCustomer(null);
return;
}
try
{
CustomerInterface selectedCustomer=customerModel.getCustomerAt(selectedIndex);
customerDetailsPanel.setCustomer(selectedCustomer);
}catch(ModelException modelException)
{
customerDetailsPanel.setCustomer(null);
}
}
// inner class begins
class CustomerDetailsPanel extends JPanel implements ActionListener
{
private CustomerInterface customer;
private JLabel  nameCaptionLabel;
private JTextField nameTextField;
private JLabel nameLabel;
private JButton clearNameTextFieldButton;
private JLabel openingBalanceCaptionLabel;
private JTextField openingBalanceTextField;
private JButton clearOpeningBalanceTextFieldButton;
private JLabel openingBalanceLabel;
private JLabel openingBalanceTypeCaptionLabel;
private JLabel openingBalanceTypeLabel;
private ButtonGroup openingBalanceTypeGroup;
private JRadioButton crOpeningBalanceTypeRadioButton;
private JRadioButton deOpeningBalanceTypeRadioButton;
//private JRadioButton consumableRadioButton;
private JButton addButton;
private JButton editButton;
private JButton deleteButton;
private JButton cancelButton;
private JButton exportToPDFButton;
CustomerDetailsPanel()
{
initComponents();
setAppearance();
addListeners();
}
private void initComponents()
{
nameCaptionLabel=new JLabel("Customer : ");
nameTextField=new JTextField();
nameLabel=new JLabel("");
clearNameTextFieldButton=new JButton("X");
openingBalanceCaptionLabel=new JLabel("Opening Balance : ");
openingBalanceTextField=new JTextField();
openingBalanceLabel=new JLabel("");
openingBalanceTypeCaptionLabel=new JLabel("Opening Balance Type: ");
openingBalanceTypeGroup=new ButtonGroup();
crOpeningBalanceTypeRadioButton=new JRadioButton("Credit");
deOpeningBalanceTypeRadioButton=new JRadioButton("Debit");
//consumableRadioButton=new JRadioButton("Consumable");
openingBalanceTypeGroup.add(crOpeningBalanceTypeRadioButton);
openingBalanceTypeGroup.add(deOpeningBalanceTypeRadioButton);
//categoryGroup.add(consumableRadioButton);
openingBalanceTypeLabel=new JLabel("");
clearOpeningBalanceTextFieldButton=new JButton("X");
addButton=new JButton("add");
editButton=new JButton("edit");
deleteButton=new JButton("delete");
cancelButton=new JButton("cancel");
exportToPDFButton=new JButton("pdf");
setLayout(null);
int lm,tm;
lm=21;
tm=21;
nameCaptionLabel.setBounds(lm+5,tm+10,100,30);
nameTextField.setBounds(lm+5+100+5,tm+10,400,30);
nameLabel.setBounds(lm+5+100+5,tm+10,400,30);
clearNameTextFieldButton.setBounds(lm+5+100+5+400+2,tm+10,30,30);
openingBalanceCaptionLabel.setBounds(lm+5,tm+10+30+10,200,30);
openingBalanceTextField.setBounds(lm+5+150+5,tm+10+30+10,200,30);
openingBalanceLabel.setBounds(lm+5+150+5,tm+10+30+10,200,30);
clearOpeningBalanceTextFieldButton.setBounds(lm+5+150+5+200+2,tm+10+30+10,30,30);
openingBalanceTypeCaptionLabel.setBounds(lm+5,tm+10+30+10+30+10,200,30);
crOpeningBalanceTypeRadioButton.setBounds(lm+5+200+5,tm+10+30+10+30+10,100,30);
deOpeningBalanceTypeRadioButton.setBounds(lm+5+200+5+100+5,tm+10+30+10+30+10,140,30);
//consumableRadioButton.setBounds(lm+5+100+5+150+5+140+5,tm+10+30+10+30+10,150,30);
openingBalanceTypeLabel.setBounds(lm+5+200+5,tm+10+30+10+30+10,450,30);

JPanel p1=new JPanel();
p1.setLayout(null);
p1.setBorder(BorderFactory.createLineBorder(Color.gray));
p1.setBounds(663/2-310/2,tm+10+30+10+30+10+30+10,310,70);
p1.setLayout(null);
addButton.setBounds(10,10,50,50);
editButton.setBounds(70,10,50,50);
deleteButton.setBounds(130,10,50,50);
cancelButton.setBounds(190,10,50,50);
exportToPDFButton.setBounds(250,10,50,50);
p1.add(addButton);
p1.add(editButton);
p1.add(deleteButton);
p1.add(cancelButton);
p1.add(exportToPDFButton);
add(nameCaptionLabel);
add(nameTextField);
add(clearNameTextFieldButton);
add(nameLabel);
add(openingBalanceCaptionLabel);
add(openingBalanceTextField);
add(clearOpeningBalanceTextFieldButton);
add(openingBalanceLabel);
add(openingBalanceTypeCaptionLabel);
add(crOpeningBalanceTypeRadioButton);
add(deOpeningBalanceTypeRadioButton);
//add(consumableRadioButton);
add(openingBalanceTypeLabel);
add(p1);
}
private void setAppearance()
{
setBorder(BorderFactory.createLineBorder(Color.gray));
Font font=new Font("Verdana",Font.PLAIN,16);
nameCaptionLabel.setFont(font);
nameLabel.setFont(font);
nameTextField.setFont(font);
openingBalanceCaptionLabel.setFont(font);
openingBalanceLabel.setFont(font);
openingBalanceTextField.setFont(font);
openingBalanceTypeCaptionLabel.setFont(font);
crOpeningBalanceTypeRadioButton.setFont(font);
deOpeningBalanceTypeRadioButton.setFont(font);
//consumableRadioButton.setFont(font);
openingBalanceTypeLabel.setFont(font);
}
private void addListeners()
{
addButton.addActionListener(this);
editButton.addActionListener(this);
cancelButton.addActionListener(this);
deleteButton.addActionListener(this);
exportToPDFButton.addActionListener(this);
clearNameTextFieldButton.addActionListener(this);
clearOpeningBalanceTextFieldButton.addActionListener(this);
}// yhaa se
public void actionPerformed(ActionEvent ev)
{
//ye nhi kiya hai
if(ev.getSource()==clearNameTextFieldButton)
{
nameTextField.setText("");
nameTextField.requestFocus();
}
if(ev.getSource()==clearOpeningBalanceTextFieldButton)
{
openingBalanceTextField.setText("");
openingBalanceTextField.requestFocus();
}
if(ev.getSource()==addButton)
{
if(CustomerPanel.this.mode==MODE.VIEW_MODE)
{
this.setAddMode();
CustomerPanel.this.setAddMode();
}
else
{
String name=nameTextField.getText().trim();
if(name.length()==0)
{
JOptionPane.showMessageDialog(this,"Name required");
nameTextField.requestFocus();
return;
}
String openingBalance=openingBalanceTextField.getText().trim();
if(openingBalance.length()==0)
{
JOptionPane.showMessageDialog(this,"Opening Balance required");
openingBalanceTextField.requestFocus();
return;
}
try
{
Integer.parseInt(openingBalance);
}catch(NumberFormatException numberFormatException)
{
JOptionPane.showMessageDialog(this,"Opening Balance should be numeric");
openingBalanceTextField.requestFocus();
return;
}
// && consumableRadioButton.isSelected()==false
if(crOpeningBalanceTypeRadioButton.isSelected()==false && deOpeningBalanceTypeRadioButton.isSelected()==false)
{
JOptionPane.showMessageDialog(this,"Opening Balance Type required");
return;
}
CustomerInterface customer=new Customer();
customer.setName(name);
customer.setOpeningBalance(Integer.parseInt(openingBalance));
if(crOpeningBalanceTypeRadioButton.isSelected()) customer.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.CREDIT);
if(deOpeningBalanceTypeRadioButton.isSelected()) customer.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
//if(consumableRadioButton.isSelected()) item.setCategory("C");
try
{
customerModel.addCustomer(customer);
}catch(ModelException modelException)
{
JOptionPane.showMessageDialog(this,modelException.getMessage());
return;
}catch(ValidationException ve)
{
}catch(ProcessException processException)
{
JOptionPane.showMessageDialog(this,processException.getMessage());
return;
}
int index=0;
try
{
index=customerModel.findCustomer(name,true,com.thinking.machines.accounting.pl.model.CustomerModelInterface.MatchType.FULL);
}catch(ModelException me)
{
}
customerModel.fireTableDataChanged();
table.setRowSelectionInterval(index,index);
table.scrollRectToVisible(new Rectangle(table.getCellRect(index,0,false)));
this.setViewMode();
CustomerPanel.this.setViewMode();
nameLabel.setText(customer.getName());
openingBalanceLabel.setText(customer.getOpeningBalance()+"/-");
openingBalanceTypeLabel.setText((customer.getOpeningBalanceType().charAt(0)=='C')?"Credit":"Debit");
}
}
if(ev.getSource()==editButton)
{
if(CustomerPanel.this.mode==MODE.VIEW_MODE)
{
int selectedRowIndex=table.getSelectedRow();
if(selectedRowIndex<0)
{
JOptionPane.showMessageDialog(this,"Select an Customer to edit");
return;
}
this.setEditMode();
CustomerPanel.this.setEditMode();
}
else
{
String name=nameTextField.getText().trim();
if(name.length()==0)
{
JOptionPane.showMessageDialog(this,"Name required");
nameTextField.requestFocus();
return;
}
String openingBalance=openingBalanceTextField.getText().trim();
if(openingBalance.length()==0)
{
JOptionPane.showMessageDialog(this,"Opening Balance required");
openingBalanceTextField.requestFocus();
return;
}
try
{
Integer.parseInt(openingBalance);
}catch(NumberFormatException numberFormatException)
{
JOptionPane.showMessageDialog(this,"Opening Balance should be numeric");
openingBalanceTextField.requestFocus();
return;
}
// && consumableRadioButton.isSelected()==false
if(crOpeningBalanceTypeRadioButton.isSelected()==false && deOpeningBalanceTypeRadioButton.isSelected()==false)
{
JOptionPane.showMessageDialog(this,"Opening Balance Type required");
return;
}
CustomerInterface customer=new Customer();
customer.setCode(this.customer.getCode());
customer.setName(name);
customer.setOpeningBalance(Integer.parseInt(openingBalance));
if(crOpeningBalanceTypeRadioButton.isSelected()) customer.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.CREDIT);
if(deOpeningBalanceTypeRadioButton.isSelected()) customer.setOpeningBalanceType(CustomerInterface.BALANCE_TYPE.DEBIT);
//if(consumableRadioButton.isSelected()) item.setCategory("C");
try
{
customerModel.updateCustomer(customer);
}catch(ModelException modelException)
{
JOptionPane.showMessageDialog(this,modelException.getMessage());
return;
}catch(ValidationException ve)
{
}catch(ProcessException processException)
{
JOptionPane.showMessageDialog(this,processException.getMessage());
return;
}
int index=0;
try
{
index=customerModel.findCustomer(name,true,com.thinking.machines.accounting.pl.model.CustomerModelInterface.MatchType.FULL);
}catch(ModelException me)
{
}
customerModel.fireTableDataChanged();
table.setRowSelectionInterval(index,index);
table.scrollRectToVisible(new Rectangle(table.getCellRect(index,0,false)));
this.setViewMode();
CustomerPanel.this.setViewMode();
nameLabel.setText(customer.getName());
openingBalanceLabel.setText(customer.getOpeningBalance()+"/-");
openingBalanceTypeLabel.setText(customer.getOpeningBalanceType());
}
}
//yhaa se
if(ev.getSource()==deleteButton)
{
int selectedIndex=table.getSelectedRow();
if(selectedIndex<0)
{
JOptionPane.showMessageDialog(this,"Select an Customer to delete");
return;
}
this.setDeleteMode();
CustomerPanel.this.setDeleteMode();
int selectedOption;
selectedOption=JOptionPane.showConfirmDialog(this,"Delete : "+this.customer.getName(),"Delete Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.YES_OPTION)
{
try
{
String name=this.customer.getName();
customerModel.deleteCustomer(this.customer.getCode());
this.setCustomer(null);
JOptionPane.showMessageDialog(this,"Customer : "+name+" deleted");
}catch(ModelException modelException)
{
JOptionPane.showMessageDialog(this,modelException.getMessage());
}catch(ValidationException ve)
{
}catch(ProcessException processException)
{
JOptionPane.showMessageDialog(this,processException.getMessage());
}
}
customerModel.fireTableDataChanged();
this.setViewMode();
CustomerPanel.this.setViewMode();
nameLabel.setText("");
openingBalanceLabel.setText("");
openingBalanceTypeLabel.setText("");
}
if(ev.getSource()==cancelButton)
{
this.setViewMode();
CustomerPanel.this.setViewMode();
}
//yhaa vala nhi kr rhe abhi
if(ev.getSource()==exportToPDFButton)
{
JFileChooser fc=new JFileChooser();
FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
fc.setFileFilter(filter);
int us=fc.showSaveDialog(null);
if(us==JFileChooser.APPROVE_OPTION)
{
File file=fc.getSelectedFile();
String name=file.getName();
String ext=null;
int i=name.lastIndexOf('.');
 if (i > 0 &&  i < name.length() - 1) 
{
ext = name.substring(i+1).toLowerCase();
}
if(ext!=null || i==name.length()-1)
{
if(ext==null)
{
file.renameTo(new File(name+".pdf"));
try
{
customerModel.createPDF(file);
}catch(ModelException me)
{
}
}
else
{
if(ext.equals("pdf"))
{
try
{
customerModel.createPDF(file);
}catch(ModelException me)
{
}
}
else
{
JOptionPane.showMessageDialog(this,"Invalid Extension.");
return;
}
}
}
else
{
JOptionPane.showMessageDialog(this,"Valid Extension Required.");
return;
}
}
}
}
private void setAddMode()
{
CustomerPanel.this.mode=MODE.ADD_MODE;
nameTextField.setText("");
openingBalanceTextField.setText("");
crOpeningBalanceTypeRadioButton.setSelected(false);
deOpeningBalanceTypeRadioButton.setSelected(false);
//consumableRadioButton.setSelected(false);
nameLabel.setVisible(false);
openingBalanceLabel.setVisible(false);
openingBalanceTypeLabel.setVisible(false);
nameTextField.setVisible(true);
clearNameTextFieldButton.setVisible(true);
openingBalanceTextField.setVisible(true);
clearOpeningBalanceTextFieldButton.setVisible(true);
crOpeningBalanceTypeRadioButton.setVisible(true);
deOpeningBalanceTypeRadioButton.setVisible(true);
//consumableRadioButton.setVisible(true);
addButton.setIcon(new ImageIcon(this.getClass().getResource("/images/save.jpg")));
editButton.setEnabled(false);
cancelButton.setEnabled(true);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
private void setDeleteMode()
{
CustomerPanel.this.mode=MODE.DELETE_MODE;
addButton.setEnabled(false);
editButton.setEnabled(false);
cancelButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
private void setEditMode()
{
CustomerPanel.this.mode=MODE.EDIT_MODE;
nameTextField.setText(customer.getName());
openingBalanceTextField.setText(String.valueOf(customer.getOpeningBalance()));
crOpeningBalanceTypeRadioButton.setSelected(false);
deOpeningBalanceTypeRadioButton.setSelected(false);
//consumableRadioButton.setSelected(false);
if(customer.getOpeningBalanceType().equals("Dr.")) deOpeningBalanceTypeRadioButton.setSelected(true);
if(customer.getOpeningBalanceType().equals("Cr.")) crOpeningBalanceTypeRadioButton.setSelected(true);
//if(item.getCategory().equals("R")) rawMaterialRadioButton.setSelected(true);
nameLabel.setVisible(false);
openingBalanceLabel.setVisible(false);
openingBalanceTypeLabel.setVisible(false);
nameTextField.setVisible(true);
clearNameTextFieldButton.setVisible(true);
openingBalanceTextField.setVisible(true);
clearOpeningBalanceTextFieldButton.setVisible(true);
crOpeningBalanceTypeRadioButton.setVisible(true);
deOpeningBalanceTypeRadioButton.setVisible(true);
//consumableRadioButton.setVisible(true);
editButton.setIcon(new ImageIcon(this.getClass().getResource("/images/save.jpg")));
addButton.setEnabled(false);
cancelButton.setEnabled(true);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
private void setViewMode()
{
CustomerPanel.this.mode=MODE.VIEW_MODE;
nameTextField.setVisible(false);
clearNameTextFieldButton.setVisible(false);
nameLabel.setVisible(true);
openingBalanceTextField.setVisible(false);
clearOpeningBalanceTextFieldButton.setVisible(false);
openingBalanceLabel.setVisible(true);
crOpeningBalanceTypeRadioButton.setVisible(false);
deOpeningBalanceTypeRadioButton.setVisible(false);
//consumableRadioButton.setVisible(false);

openingBalanceTypeLabel.setVisible(true);
cancelButton.setEnabled(false);
addButton.setText("A");
editButton.setText("E");
addButton.setEnabled(true);
if(customerModel.getRowCount()==0)
{
editButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
else
{
editButton.setEnabled(true);
deleteButton.setEnabled(true);
exportToPDFButton.setEnabled(true);
}
}
public void setCustomer(CustomerInterface customer)
{
this.customer=customer;
if(this.customer==null)
{
nameLabel.setText("");
openingBalanceLabel.setText("");
openingBalanceTypeLabel.setText("");
}
else
{
nameLabel.setText(this.customer.getName());
openingBalanceLabel.setText(String.valueOf(this.customer.getOpeningBalance()));
String openingBalanceType=customer.getOpeningBalanceType();
if(openingBalanceType.equals("Dr."))
{
openingBalanceTypeLabel.setText("Debit");
}
if(openingBalanceType.equals("Cr."))
{
openingBalanceTypeLabel.setText("Credit");
}
/*if(category.equals("C"))
{
categoryLabel.setText("Consumable");
}*/
}
}
}// inner class ends
}
