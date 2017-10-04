

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Dish implements Serializable
{
	private String name;
	private String description;
	private int price;
	private ConcurrentHashMap<String, Integer> recipe;
	private Integer restock;
	public Integer currentValue;

	public Dish(String name, String description, int price, ConcurrentHashMap<String,Integer> recipe, Integer restock, Integer initialValue) throws Exception
	{
		this.name = name;
		this.description = description;
		this.price = price;
		this.recipe = recipe;
		
		if(restock < 0)
			throw new IllegalArgumentException("Restocking value can't be negative");
		else
			this.restock = restock;
		
		if(initialValue < 0)
			throw new IllegalArgumentException("The Initial Value can't be negative");
		else
		{
			this.currentValue = initialValue;
		}
	}
	
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
	public String getDescription() {
		return description;
	}
	public ConcurrentHashMap<String, Integer> getRecipe() {
		return recipe;
	}
	public Integer getRestock() {
		return restock;
	}
	
	public void setName(String name) {
		synchronized(this.name) {this.name = name;}
	}
	public void setDescription(String description) {
		synchronized (this.description) {this.description = description;}
	}
	public void setPrice(int price) {
		synchronized (this.currentValue) {this.price = price;}
	}
	public void setRecipe(ConcurrentHashMap<String, Integer> recipe) {
		synchronized (this.recipe) {this.recipe = recipe;}
	}
	public void setRestock(Integer restock) {
		synchronized (this.restock) {this.restock = restock;}
	}
}
