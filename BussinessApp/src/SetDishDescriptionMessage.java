
public class SetDishDescriptionMessage extends Message
{
	String dish;
	String description;
	public SetDishDescriptionMessage(String dish, String description)
	{
		this.dish = dish;
		this.description = description;
	}
}
