

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DishStock 
{
//	private ArrayList<Observer> observers;
	public ArrayList<Dish> currentStock = new ArrayList<Dish>();
	public CopyOnWriteArrayList<Dish> restock = new CopyOnWriteArrayList<Dish>();
	private DroneManagement drones;
	private StaffManagement staff;
	private static IngredientStock ingredientStock;

	public DishStock(IngredientStock ingredientStock, StaffManagement staff, DroneManagement drones) 
	{
		try
		{ 
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File( "CurrentDishStock.txt" )));
			currentStock = (ArrayList<Dish>) ois.readObject();
			ois.close();
		} catch(Exception e)
			{
				System.out.println(e);
			}
		try
		{ 
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File( "RestockDish.txt" )));
			restock = (CopyOnWriteArrayList<Dish>) ois.readObject();
			ois.close();
		} catch(Exception e)
			{
				System.out.println(e);
			}

		this.ingredientStock = ingredientStock;
		this.staff = staff;
		this.drones = drones;
	}
	
	public ArrayList<Dish> getCurrentStock()
	{
		return currentStock;
	}
	public CopyOnWriteArrayList<Dish> getRestock()
	{
		return restock;
	}
	
	public static void setIngredientStock(IngredientStock ingredientStock) {
		DishStock.ingredientStock = ingredientStock;
	}
	
	public void sendDish(Dish dish)
	{
			while(this.getCurrentStock().get(this.getCurrentStock().indexOf(dish)).currentValue == 0)
			{}
			synchronized(this.getCurrentStock().get(this.getCurrentStock().indexOf(dish)).currentValue) {this.getCurrentStock().get(this.getCurrentStock().indexOf(dish)).currentValue --; }
		
		if(this.getCurrentStock().get(this.getCurrentStock().indexOf(dish)).currentValue < this.getCurrentStock().get(this.getCurrentStock().indexOf(dish)).getRestock())
			staff.addOrder(this, dish, ingredientStock);
	}
	
	public void sendOrder(Order order)
	{
		Dish dish;
		for (String item : order.getContent())
		{
		    boolean gasit = false;
		    
		    for(int i=0; i<currentStock.size() && gasit == false; i++)
		    {
		    	dish = currentStock.get(i);
		    	if (item.equals(dish.getName()) )
		    	{
		    		gasit = true;
		    		sendDish(dish);
		    	}
		    }
		}
		drones.deliverOrder( order);
	}
	
	public void addDishToStock(Dish dish)
	{
		boolean contains = false;
		for (Dish stock : this.restock)
			{
				if(dish.getName().equals(stock.getName()))
					contains = true;
			}
		if(contains == false)
		{
			this.restock.add(dish);
			synchronized(this.currentStock) {this.currentStock.add(dish);}
		}
		checkDish(dish);
	}
	public void checkDish(Dish dish) {
		if(dish.currentValue < dish.getRestock())
		{
			for(int i=0; i<dish.getRestock()-dish.currentValue; i++)
				staff.addOrder(this, dish, ingredientStock);
		}
	}

	public void finishDish(Dish dish)
	{
		for(Dish somsing : currentStock)
		{
			if (somsing.getName().equals( dish.getName()) )
				synchronized(somsing.currentValue) {somsing.currentValue ++;}
		}
	}
	
	public void setStaff(Integer i)
	{
		staff.changeStaff(i);
	}
	public void save()
	{
		staff.stop();
		drones.stop();
		try{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("CurrentDishStock.txt")) );
		oos.writeObject(currentStock);
		oos.flush();
		oos.close();
		}catch(Exception e)
		{
			System.out.println(e);
		}
		
		try{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("RestockDish.txt")) );
		oos.writeObject(restock);
		oos.flush();
		oos.close();
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
		
//	public void updateCurrentStock(HashMap<String, Integer> ingredients)
//	{
//		for (Map.Entry<String, Integer> entry : ingredients.entrySet())
//		{
//			String key = entry.getKey();
//			Integer value = entry.getValue();
//			
//			if(this.currentStock.containsKey(key))
//			{
//				currentStock.replace(key, currentStock.get(key) + value);
//			}
//		}
//	}
	
//	public void attachObserver(Observer o)
//	{
//		observers.add(o);
//	}
//	public void detachObserver(Observer o)
//	{
//		observers.add(o);
//	}
//	public void notifyObservers()
//	{
//	   for (Observer observer : observers) 
//	   {
//	        observer.update();
//	   }
//	}
}
