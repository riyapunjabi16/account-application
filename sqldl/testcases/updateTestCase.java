import com.thinking.machines.accounting.dl.*;
class updateTestCase
{
public static void main(String data [])
{
int code=Integer.parseInt(data[0]);
String name=data[1];
int openingBalance=Integer.parseInt(data[2]);
char openingBalanceType=data[3].charAt(0);
CustomerDTOInterface customerDTOInterface;
customerDTOInterface=new CustomerDTO();

customerDTOInterface.setCode(code);
customerDTOInterface.setName(name);

customerDTOInterface.setOpeningBalance(openingBalance);
customerDTOInterface.setOpeningBalanceType(openingBalanceType);
CustomerDAOInterface customerDAOInterface;
customerDAOInterface=new CustomerDAO();
try
{
customerDAOInterface.update(customerDTOInterface);
System.out.println("Customer Updated.");
}
catch(DAOException dao)
{
System.out.println(dao.getMessage());
}
}
}