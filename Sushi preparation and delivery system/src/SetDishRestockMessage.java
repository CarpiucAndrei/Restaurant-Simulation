
public class SetDishRestockMessage extends Message
{
	private String dish;
	private Integer restockValue;
	
	public SetDishRestockMessage(String dish, Integer restockValue)
	{
		this.dish = dish;
		this.restockValue = restockValue;
	}
	
	public String getDish() {
		return dish;
	}
	public Integer getRestockValue() {
		return restockValue;
	}
}