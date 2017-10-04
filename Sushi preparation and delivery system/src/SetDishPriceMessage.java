
public class SetDishPriceMessage extends Message
{
	String dish;
	Integer price;
	public SetDishPriceMessage(String dish, Integer price)
	{
		this.dish = dish;
		this.price = price;
	}
}
