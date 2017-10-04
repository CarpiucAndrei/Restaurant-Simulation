

import java.io.Serializable;

public class Ingredient implements Serializable
{
	private String name;
	private String unit;
	private Supplier supplier;
	private Integer restockVal;
	public Integer currentValue;
	
	public Ingredient(String name, String unit, Supplier supplier, Integer restock, Integer initialValue) throws Exception
	{
		this.name = name;
		this.unit = unit;
		this.supplier = supplier;
		
		if(restock < 0)
			throw new IllegalArgumentException("Restocking value can't be negative");
		else
			this.restockVal = restock;
	
		if(initialValue < 0)
			throw new IllegalArgumentException("The Initial Value can't be negative");
		else
			this.currentValue = initialValue;
	}
	
	public String getName() {
		return name;
	}
	public String getUnit() {
		return unit;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public Integer getRestock() {
		return restockVal;
	}
	public void setName(String name) {
		synchronized (this.name) {this.name = name;}
	}
	public void setUnit(String unit) {
		synchronized (this.unit) {this.unit = unit;}
	}
	public void setSupplier(Supplier supplier) {
		synchronized (this.supplier) {this.supplier = supplier;}
	}
	public void setRestock(Integer restock) {
		synchronized (this.restockVal) {this.restockVal = restockVal;}
	}
}
