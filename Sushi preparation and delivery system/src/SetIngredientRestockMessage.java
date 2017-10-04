public class SetIngredientRestockMessage extends Message
{
	private String ingredient;
	private Integer restockValue;
	
	public SetIngredientRestockMessage(String ingredient, Integer restockValue)
	{
		this.ingredient = ingredient;
		this.restockValue = restockValue;
	}
	
	public String getIngredient() {
		return ingredient;
	}
	public Integer getRestockValue() {
		return restockValue;
	}
}