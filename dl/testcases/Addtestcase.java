import com.thinking.machines.accounting.dl.*;
class CustomerAddTestCase
{
public static void main (String data [])
{
String name=data[0];
int openingBalance=Integer.parseInt(data[1]);
char openingBalanceType=data[2].charAt(0);
CustomerDTOInterface customerDTOInterface;
customerDTOInterface=new CustomerDTO();
customerDTOInterface.setName(name);
customerDTOInterface.setOpeningBalance(openingBalance);
customerDTOInterface.setOpeningBalanceType(openingBalanceType);
CustomerDAOInterface customerDAOInterface;
customerDAOInterface=new CustomerDAO();
try
{
customerDAOInterface.add(customerDTOInterface);
System.out.println("Customer Added.");
}
catch(DAOException dao)
{
System.out.println(dao.getMessage());
};
}
}