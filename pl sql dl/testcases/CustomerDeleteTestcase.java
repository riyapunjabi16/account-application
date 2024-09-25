import com.thinking.machines.accounting.dl.*;
class CustomerDeleteTestCase
{
public static void main(String data[])
{
int code;
code=Integer.parseInt(data[0]);
CustomerDAOInterface customerDAOInterface;
customerDAOInterface=new CustomerDAO();
try
{
customerDAOInterface.delete(code);
System.out.println("Customer Deleted");
}
catch(DAOException daoException)
{
System.out.print(daoException.getMessage());
}
}
}