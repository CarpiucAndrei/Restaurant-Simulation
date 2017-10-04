
public class SetDishNameMessage extends Message
{
	private String dish;
	private String newName;
	
	public SetDishNameMessage(String dish, String newName)
	{
		this.dish = dish;
		this.newName = newName;
	}
	
	public String getDish() {
		return dish;
	}
	public String getNewName() {
		return newName;
	}

}
