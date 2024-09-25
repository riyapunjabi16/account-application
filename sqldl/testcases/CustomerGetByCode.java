import com.thinking.machines.accounting.dl.*;
class CustomerGetByCodeTestCase
{
public static void main(String data[])
{
int code;
code=Integer.parseInt(data[0]);
CustomerDAOInterface customerDAOInterface;
customerDAOInterface=new CustomerDAO();
CustomerDTOInterface cao;
try
{
cao= customerDAOInterface.getByCode(code);
System.out.println(code +cao.getName()+cao.getOpeningBalance()+cao.getOpeningBalanceType());
}
catch(DAOException daoException)
{
System.out.print(daoException.getMessage());
}
}
}