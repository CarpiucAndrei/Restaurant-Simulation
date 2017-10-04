

import java.io.Serializable;

public class RegisterMessage extends Message 
{
	private String userName;
	private String password;
	private String adress;
	private String postCode;
	
	public RegisterMessage(String userName, String password, String adress, String postCode)
	{
		this.userName = userName;
		this.password = password;
		this.adress = adress;
		this.postCode = postCode;
	}
	
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return  password;
	}
	public String getAdress() {
		return adress;
	}
	public String getPostCode() {
		return postCode;
	}
}
