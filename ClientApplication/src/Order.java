

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable
{
	private static HashMap<String, Integer> postCodeDistances;//the Integer represents the distance in meters
	private ArrayList<String> content;
	private boolean status;
	private String postCode;
	
	static{
		Order.postCodeDistances = new HashMap<String, Integer>();
		postCodeDistances.put("SO15 RX69", 500);
		postCodeDistances.put("1234", 1000);
		postCodeDistances.put("1235", 300);
	}
	
	public Order(ArrayList<String> content, String postCode)
	{
		this.content = content;
		this.postCode = postCode;
		this.status = false;
	}


	public boolean getStatus() {
		return status;
	}	
	public void setStatus(boolean status)
	{
		this.status = status;
	}
	public String getPostCode()
	{
		return postCode;
	}
	public ArrayList<String> getContent()
	{
		return content;
	}
	
	public Integer getDistance(String postalCode)
	{
		return postCodeDistances.get(postalCode);
	}

}
