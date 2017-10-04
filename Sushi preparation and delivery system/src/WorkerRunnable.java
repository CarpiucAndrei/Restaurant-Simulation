

import java.io.DataOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;

public class WorkerRunnable implements Runnable
{
	private Socket clientSocket = null;
	private static DishStock ds;
    private static IngredientStock is;
    private static String usersFileName = "users.txt";
    private static File usersFile = new File(usersFileName);
    private static DataBase db;
    //private static Scanner scanner = new Scanner(usersFile);
	
	WorkerRunnable(Socket clientSocket, DishStock ds, IngredientStock is, DataBase db)
	{
		WorkerRunnable.ds = ds;
		WorkerRunnable.is = is;
		this.clientSocket = clientSocket;
		this.db = db;
	}
	
	public void run()
	{
		try{
		ObjectInputStream inFromClient = new ObjectInputStream ( clientSocket.getInputStream() );
		DataOutputStream outToClient = new DataOutputStream ( clientSocket.getOutputStream() );
		ObjectOutputStream objectToClient = new ObjectOutputStream ( clientSocket.getOutputStream() );


		Message message = (Message) inFromClient.readObject();
				
		//Probabil ca pt final e bine sa schimbi toate exceptile sa nu te scoata din loop?
		if(message instanceof OrderMessage)
		{
			for(User user : db.users)
			{
				if(user.getUserName().equals(((OrderMessage) message).user.getUserName()))
				{
					db.users.get(db.users.indexOf(user)).orders.add(((OrderMessage) message).getOrder());
				}
			}
			//db.users.get(db.users.indexOf( ((OrderMessage) message).user)).orders.add(((OrderMessage) message).getOrder());
			ds.sendOrder(((OrderMessage) message).getOrder());
			outToClient.writeBoolean(true);
		}
		else if(message instanceof RefreshMessage)
		{
			objectToClient.writeObject(ds.currentStock);
		}
		else if(message instanceof RefreshOrderMessage)
		{
			objectToClient.writeObject(db.users);
		}
		else if(message instanceof RefreshIngredientMessage)
		{
			objectToClient.writeObject(is.currentStock);
		}
		else if (message instanceof DeleteCompletedMessage)
		{
			for(User user : db.users)
			{
				for (Iterator<Order> iterator = user.orders.iterator(); iterator.hasNext();) {
				    Order order = iterator.next();
						if(order.getStatus() == true)
							iterator.remove();
				}
			}
		}
		else if(message instanceof SetStaffMessage)
		{
			ds.setStaff(((SetStaffMessage) message).num);
		}
		else if(message instanceof SetStaffMessage)
		{
			DroneManagement.changeDrones(((SetStaffMessage) message).num);
		}
		else if(message instanceof SetDishRestockMessage)
		{
			for(Dish dish : ds.restock)
			{
				if(((SetDishRestockMessage) message).getDish().equals(dish.getName()))
				{
					dish.setRestock(((SetDishRestockMessage) message).getRestockValue());
					ds.checkDish(dish);
						
				}
			}
		}
		else if(message instanceof SetDishNameMessage)
		{
			for(Dish dish : ds.restock)
			{
				if(((SetDishNameMessage) message).getDish().equals(dish.getName()))
				{
					dish.setName(((SetDishNameMessage) message).getNewName());
					ds.checkDish(dish);
				}
			}
			for(Dish dish : ds.restock)
			{
				if(((SetDishNameMessage) message).getDish().equals(dish.getName()))
				{
					dish.setName(((SetDishNameMessage) message).getNewName());
					ds.checkDish(dish);
						
				}
			}
		}
		else if(message instanceof SetDishPriceMessage)
		{
			for(Dish dish : ds.restock)
			{
				if(((SetDishPriceMessage) message).dish.equals(dish.getName()))
				{
					dish.setPrice(((SetDishPriceMessage) message).price);
					ds.checkDish(dish);
				}
			}
			for(Dish dish : ds.restock)
			{
				if(((SetDishPriceMessage) message).dish.equals(dish.getName()))
				{
					dish.setPrice(((SetDishPriceMessage) message).price);
					ds.checkDish(dish);		
				}
			}
		}
		else if(message instanceof SetDishDescriptionMessage)
		{
			for(Dish dish : ds.restock)
			{
				if(((SetDishDescriptionMessage) message).dish.equals(dish.getName()))
				{
					dish.setDescription(((SetDishDescriptionMessage) message).description);
					ds.checkDish(dish);
				}
			}
			for(Dish dish : ds.restock)
			{
				if(((SetDishDescriptionMessage) message).dish.equals(dish.getName()))
				{
					dish.setDescription(((SetDishDescriptionMessage) message).description);
					ds.checkDish(dish);		
				}
			}
		}
		else if(message instanceof SetIngredientRestockMessage)
		{
			for(Ingredient ingredient : is.restock)
			{
				if(((SetIngredientRestockMessage) message).getIngredient().equals(ingredient.getName()))
				{
					ingredient.setRestock(((SetIngredientRestockMessage) message).getRestockValue());
					is.checkIngredient(ingredient);
				}
			}
		}
		else if(message instanceof ViewMessage)
		{
			for(User user : db.users)
			{
				if(user.getUserName().equals(((ViewMessage) message).user.getUserName()))
				{
					objectToClient.writeObject( db.users.get(db.users.indexOf(user)).orders);
				}
			}
			//objectToClient.writeObject(db.users.get(db.users.indexOf( ((ViewMessage) message).user )).orders);
		}
		else if(message instanceof RegisterMessage)
			{
			boolean exista = false;
				try{
					for(User user : db.users)
					{
						if (user.getUserName().equals(((RegisterMessage) message).getUserName()))
							exista = true;
					}
					if (exista == false)
					{
						User u1 = new User(((RegisterMessage) message).getUserName(), ((RegisterMessage) message).getPassword(), ((RegisterMessage) message).getAdress(), ((RegisterMessage) message).getPostCode());
						db.users.add(u1);
					    outToClient.writeBoolean(true);
					}
					else 
					    outToClient.writeBoolean(false);
				}catch(Exception e)
					{
						System.out.println(e);
					}
			}
		else if(message instanceof LogInMessage)
		{
			//System.out.println("asta ii " + message);
		User user = null;
			try{
				for(int i=0; i<db.users.size() && user == null; i++)
				{
					//System.out.println(db.users.get(i).getUserName() + " " + ((LogInMessage) message).getUserName() + " " + db.users.get(i).getUserName().equals(((LogInMessage) message).getUserName()) + " " + db.users.get(i).getPassword() + " " + ((LogInMessage) message).getPassword() + " " + db.users.get(i).getPassword().equals(((LogInMessage) message).getPassword() ));
					if ( db.users.get(i).getUserName().equals(((LogInMessage) message).getUserName()) && db.users.get(i).getPassword().equals(((LogInMessage) message).getPassword() ))
						user = db.users.get(i);

				}
					objectToClient.writeObject(user);

					//fa sa returneze authenticated la gui si daca e false sa dea eroare "Username or Password wrong."
			}catch(Exception e)
				{
					System.out.println(e);
				}
		}
		//aici faci ifuri pt toate tipurile de mesaje

		}catch (Exception e){
			System.out.println(e);
		}
		
	}
}
