import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main 
{
	public static void main(String[] args) 
	{
		StaffManagement sm = new StaffManagement();
		DroneManagement dm = new DroneManagement();
		IngredientStock is = new IngredientStock(dm);
		DishStock ds = new DishStock(is, sm, dm);		
		DataBase db = new DataBase();
		
		Comms comms = new Comms(8000, ds, is, db);
		  comms.start();
			
		try {
		    Thread.sleep(120 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		comms.stopServer();
	}
}