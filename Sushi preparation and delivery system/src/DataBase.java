

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataBase 
{
	public CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<User>();
	
	public DataBase()
	{
		synchronized(users)
		{
			try
			{ 
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File( "DataBase.txt" )));
					users = (CopyOnWriteArrayList<User>) ois.readObject();
				ois.close();
			} catch(Exception e)
				{
					System.out.println(e);
				}
		}
//		for(User u : users)
//			System.out.println(u.getUserName() + " " + u.getPassword());
	}
}
