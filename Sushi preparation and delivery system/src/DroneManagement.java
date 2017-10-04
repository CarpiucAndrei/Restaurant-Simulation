import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DroneManagement 
{
		private static ExecutorService executorService = Executors.newFixedThreadPool(10);;
		
		public DroneManagement()
		{}
		
		public static void changeDrones(int n)
		{
			executorService.shutdown();
			executorService = Executors.newFixedThreadPool(n);
			System.out.println(n);
		}
		
		public void deliverOrder( Order order)
		{
			executorService.execute(new Drone(order));
		}
		public void pickIngredients( IngredientStock ingredientStock, ArrayList<Ingredient> pickUpList)
		{
			executorService.execute(new Drone(ingredientStock, pickUpList));
		}
		public void stop()
		{
			executorService.shutdown();
		}
}
