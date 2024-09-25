package com.thinking.machines.accounting.pl.model;
import java.awt.*;
import javax.swing.*;
class CustomerFrame extends JFrame
{
private CustomerPanel customerPanel;
private Container container;
CustomerFrame()
{
initComponents();
setAppearance();
}
private void initComponents()
{
container=getContentPane();
container.setLayout(null);
customerPanel=new CustomerPanel();
customerPanel.setBounds(1,1,682,608);

container.add(customerPanel);
setSize(700,650);
Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
setLocation(dimension.width/2-getWidth()/2,dimension.height/2-getHeight()/2);
setVisible(true);
}
private void setAppearance()
{
setTitle("customers");
//ImageIcon appIcon=new ImageIcon("app.png");
//setIconImage(appIcon.getImage());
customerPanel.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));
}
public static void main(String gg[])
{
CustomerFrame customerFrame=new CustomerFrame();
}
}
