import com.thinking.machines.accounting.dl.*;
class CustomerGetByNameTestCase
{
public static void main(String data[])
{
String name;
name=data[0];
CustomerDAOInterface customerDAOInterface;
customerDAOInterface=new CustomerDAO();
CustomerDTOInterface cao;
try
{
cao= customerDAOInterface.getByName(name);
System.out.println(cao.getCode() +cao.getName()+cao.getOpeningBalance()+cao.getOpeningBalanceType());
}
catch(DAOException daoException)
{
System.out.print(daoException.getMessage());
}
}
}