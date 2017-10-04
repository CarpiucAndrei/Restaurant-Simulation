

import java.io.Serializable;
import java.util.Map;

public class KitchenStaff implements Runnable
{
	IngredientStock ingredientStock;
	DishStock dishStock;
	Dish dish;
	
	public KitchenStaff(DishStock dishStock, Dish dish, IngredientStock ingredientStock)
	{
		this.dishStock = dishStock;
		this.dish = dish;
		this.ingredientStock = ingredientStock;
	}

	public void run()
	{	
		try{
			ingredientStock.updateCurrentStock(dish.getRecipe());
//			for (Map.Entry<String, Integer> entry : dish.getRecipe().entrySet()) 
//			{
//				boolean exista = false;
//				String key = entry.getKey();
//			    Integer value = entry.getValue();
//			    for(Ingredient ingredient : ingredientStock.currentStock)
//			    {
//			    	//System.out.println(ingredient.getName()+ " " + key);
//			    	if(ingredient.getName().equals(key) && (ingredient.currentValue - value >= 0) )
//			    	{
//			    		synchronized(ingredient.currentValue) {ingredient.currentValue -= value;}
//			    		exista = true;
//			    	}
//			    }
//		    	if(exista == false)
//		    		throw new Exception("No products");
//			}
			System.out.println("Starts dish " + dish.getName());
			try { Thread.sleep(5*1000); } catch (InterruptedException e){System.out.println("WTF cum ai fost intrerupt?"); }
			dishStock.finishDish(dish);
			System.out.println("Prepared " + dish.getName());
		} catch (Exception e)
			{
			  System.out.println(e);
			}
		
	}
}
