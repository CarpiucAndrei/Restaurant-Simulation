import java.io.Serializable;
import java.util.ArrayList;

public class Drone implements Runnable
{
	private static int speed = 100; //for the purpose of this assignment I assume the same speed for all the drones, the speed is measured in meters/seconds
	private static int restock = 100; // how many ingredients of a kind the drone will carry in one go
	IngredientStock ingredientStock;
	Order order;
	ArrayList<Ingredient> pickUpList; //in the constructor 
	/*this int tells the thread what to do:
	 * 1 -> deliver to user
	 * 2 -> restock ingredients
	 * ... functonality to be added later if the need arises
	 */
	int i; 
	
	public Drone(IngredientStock ingredientStock, Order order, int i)
	{
		this.ingredientStock = ingredientStock;
		this.order = order;
		this.i = i;
	}
	public Drone( Order order)
	{
		this.order = order;
		this.i = 1;
	}
	public Drone( IngredientStock ingredientStock, ArrayList<Ingredient> pickUpList)
	{
		this.ingredientStock = ingredientStock;
		this.pickUpList = pickUpList;
		this.i = 2;
	}
	
	
	public void run()
	{
		if(i == 1)
		{
			System.out.println("Drone is flying to " + order.getPostCode());
			try{	
				Thread.sleep( order.getDistance(order.getPostCode())/speed * 2000); // multipled by 2000 because of the way back
				order.setStatus(true);
				System.out.println("Order delivered at " + order.getPostCode());
			}catch (Exception e)
			{
				System.out.println(e);
			}
		}
		else if(i == 2)
		{
			//System.out.println(pickUpList);
			try { Thread.sleep(pickUpList.get(0).getSupplier().getDistance()*2000); } catch (InterruptedException e){System.out.println("WTF cum ai fost intrerupt?"); }
			
			ingredientStock.deliverStock(pickUpList, restock);	
		}
	}
}
