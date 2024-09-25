import com.thinking.machines.accounting.dl.*;
class GetCount
{
public static void main(String gg[])
{
CustomerDAOInterface customer;
customer=new CustomerDAO();
int count=customer.getCount();
System.out.print(count);
}
}