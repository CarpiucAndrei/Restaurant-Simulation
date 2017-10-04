import java.io.Serializable;

public class Supplier implements Serializable
{
	private String name;
	private int distance;
	
	public Supplier(String name, int distance)
	{
		this.name = name;
		this.distance = distance;
	}
	
	public String getName() {
		return name;
	}
	public int getDistance() {
		return distance;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public void setDistance(int distance){
		this.distance = distance;
	}
}
