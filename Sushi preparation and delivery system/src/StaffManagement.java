

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StaffManagement
{
	private ExecutorService executorService;
	
	public StaffManagement()
	{
		executorService = Executors.newFixedThreadPool(10);
	}
	
	public void changeStaff(int n)
	{
		executorService.shutdown();
		executorService = Executors.newFixedThreadPool(n);
	}
	
	public void addOrder(DishStock dishStock, Dish dish, IngredientStock ingredientStock)
	{
		executorService.execute(new KitchenStaff(dishStock, dish, ingredientStock));
	}
	public void stop()
	{
		executorService.shutdown();
	}
}
