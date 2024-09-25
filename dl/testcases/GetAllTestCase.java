import com.thinking.machines.accounting.dl.*;
import java.util.*;
class CustomerGetAllTestCase
{
public static void main(String gg[])
{
try
{
List<CustomerDTOInterface> customers =new CustomerDAO().getAll();
CustomerDTOInterface c;
for(int i=0;i<customers.size();i++)
{
c=customers.get(i);
System.out.println(c.getCode()+c.getName()+c.getOpeningBalance()+c.getOpeningBalanceType());
}
}
catch(Exception e){}
}
}