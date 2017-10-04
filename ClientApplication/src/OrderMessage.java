
import java.util.HashMap;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderMessage extends Message
{
	private Order order;
	public User user;
	
	public OrderMessage (Order order, User user)
	{
		this.order = order;
		this.user = user;
	}
	
	public Order getOrder()
	{
		return order;
	}
}

