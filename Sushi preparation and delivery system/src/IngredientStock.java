import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class IngredientStock 	
{
	public ArrayList<Ingredient> currentStock = new ArrayList<Ingredient>();
	public CopyOnWriteArrayList<Ingredient> restock = new CopyOnWriteArrayList<Ingredient>();
	private DroneManagement drones;
	
	public IngredientStock(DroneManagement drones)
	{
		try
		{ 
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File( "CurrentIngredientStock.txt" )));
			currentStock = (ArrayList<Ingredient>) ois.readObject();
			ois.close();
		} catch(Exception e)
			{
				System.out.println(e);
			}
		try
		{ 
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File( "IngredientRestock.txt" )));
			restock = (CopyOnWriteArrayList<Ingredient>) ois.readObject();
			ois.close();
		} catch(Exception e)
			{
				System.out.println(e);
			}
		
		this.drones = drones;
	}
	
	public ArrayList<Ingredient> getCurrentStock()
	{
		return currentStock;
	}
	public CopyOnWriteArrayList<Ingredient> getRestock()
	{
		return restock;
	}
	
	public void updateCurrentStock( ConcurrentHashMap<String, Integer> recipe) throws Exception
	{
		ArrayList<ArrayList<Ingredient>> somsing = new ArrayList<ArrayList<Ingredient>>(); 
		for (Map.Entry<String, Integer> entry : recipe.entrySet()) 
		{
			boolean exista = false;
			String key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    for(Ingredient ingredient : this.currentStock)
		    {
		    	//System.out.println(ingredient.getName()+ " " + key);
		    	if(ingredient.getName().equals(key) && (ingredient.currentValue - value >= 0) )
		    	{
		    		synchronized(ingredient.currentValue) {ingredient.currentValue -= value;}
		    		
		    		boolean found = false;
		    		for(ArrayList<Ingredient> pickUps : somsing)
		    		{
		    			if(pickUps.get(0).getSupplier().getName().equals(ingredient.getSupplier().getName()))
		    			{
		    				found = true;
		    				if(ingredient.currentValue < ingredient.getRestock())
		    					pickUps.add(ingredient);
		    			}
		    		}
		    		
		    		if(found == false && ingredient.currentValue < ingredient.getRestock())
		    		{
		    			ArrayList<Ingredient> a = new ArrayList<Ingredient>();
		    					a.add(ingredient);
		    			somsing.add(a);
		    		}
		    		exista = true;
		    	}
		    }
	    	if(exista == false)
	    		throw new Exception("No products");
		}
		//System.out.println(somsing);
		
		for(ArrayList<Ingredient> pickUps : somsing)
		{
			//System.out.println(pickUps);
			drones.pickIngredients(this, pickUps);
		}
	}
	
	public void deliverStock(ArrayList<Ingredient> ingredients, int value)
	{
		for(Ingredient current : ingredients)
		{
			for(Ingredient ingredient : currentStock)
			{
				if (current.getName().equals(ingredient.getName()))
					synchronized(ingredient.currentValue) {ingredient.currentValue += value; 
															System.out.println(ingredient.getName() + " " + ingredient.currentValue);}
			}
		}
	}
	
	public void addIngredientToStock(Ingredient ingredient)
	{
			boolean contains = false;
			for (Ingredient stock: this.restock)
				{
					if(ingredient.getName().equals(stock.getName()))
						contains = true;
				}
			if(contains == false)
			{
				this.restock.add(ingredient);
				synchronized(this.currentStock){this.currentStock.add(ingredient); }
			}
			checkIngredient(ingredient);
	}
	public void checkIngredient(Ingredient ingredient) {
		if(ingredient.currentValue < ingredient.getRestock())
		{
			ArrayList<Ingredient> ar = new ArrayList<Ingredient>(); 
			ar.add(ingredient);
			drones.pickIngredients(this, ar);
		}
	}
	
	public void save()
	{
		drones.stop();
		try{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("CurrentIngredientStock.txt")) );
		oos.writeObject(currentStock);
		oos.flush();
		oos.close();
		}catch(Exception e)
		{
			System.out.println(e);
		}
		
		try{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("IngredientRestock.txt")) );
		oos.writeObject(restock);
		oos.flush();
		oos.close();
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
