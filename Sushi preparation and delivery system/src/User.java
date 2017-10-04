

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
	
	private String userName;
	private String password;
	private String adress;
	private String postCode;
	public ArrayList<Order> orders = new ArrayList<Order>();
	
	public User(String userName, String password, String adress, String postCode)
	{
		this.userName = userName;
		this.password = password;
		this.adress = adress;
		this.postCode = postCode;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
}