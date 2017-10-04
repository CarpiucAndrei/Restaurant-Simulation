import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BusinessGUI extends JFrame
{
	 final static String newline = "\n";
	 CopyOnWriteArrayList<User> users;
 	 ArrayList<Dish> dishes;
 	 ArrayList<String> dishesName = new ArrayList<String>();
	 ArrayList<Ingredient> ingredients;
 	 ArrayList<String> ingredientsName = new ArrayList<String>();
	 JPanel dishStockPanel = new JPanel();
	 JComboBox<String> dishBox = new JComboBox();
	 JPanel ingredientStockPanel = new JPanel();
	 JComboBox<String> ingredientBox = new JComboBox();
	JTextArea orderArea = new JTextArea();

	 private class Refresh extends Thread 
	 {
		boolean execute = true;
		 
		public void stopExecute()
		{
			execute = false;
		}
			
		public void run()
		{
			while(execute)
			{
				BusinessGUI.this.refreshAllStock();
				try{ this.sleep(5*1000); }catch (Exception e) { System.out.println(e);}
			}
		}
	 }
	 
	 private class RefreshOrder extends Thread 
	 {
		boolean execute = true;
		 
		public void stopExecute()
		{
			execute = false;
		}
			
		public void run()
		{
			while(execute)
			{
				BusinessGUI.this.refreshOrder();
				try{ this.sleep(5*1000); }catch (Exception e) { System.out.println(e);}
			}
		}
	 }
	
	public BusinessGUI()
	{
		try{
		    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
		    
		    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		    
			outToServer.writeObject(new RefreshMessage());
			dishes = (ArrayList<Dish>) inFromServer.readObject();
		} catch (Exception e)
			{
				System.out.println(e);
			}
		for(Dish dish : dishes)
			dishesName.add(dish.getName());
		for(String ing : dishesName)
			dishBox.addItem(ing);
		
		try{
		    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
		    
		    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			
			outToServer.writeObject(new RefreshIngredientMessage());
			ingredients = (ArrayList<Ingredient>) inFromServer.readObject();
		} catch (Exception e)
			{
				System.out.println(e);
			}
		for(Ingredient ingredient : ingredients)
			ingredientsName.add(ingredient.getName());
		for(String ing : ingredientsName)
			ingredientBox.addItem(ing);
		
		orderArea.setEditable(false);
		
		Refresh r = new Refresh();
		r.start();
		
		RefreshOrder rOrder = new RefreshOrder();
		rOrder.start();
			
		this.addWindowListener(new WindowAdapter()
								{
									public void windowClosing(WindowEvent e)
									{
										r.stopExecute();
										rOrder.stopExecute();;
										e.getWindow().dispose();
									}
								});	
		this.setTitle("The Bizz");
		
		
		
		dishStockPanel.setLayout(new BoxLayout(dishStockPanel, BoxLayout.Y_AXIS));
		ingredientStockPanel.setLayout(new BoxLayout(ingredientStockPanel, BoxLayout.Y_AXIS));

		
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel tab1 = new JPanel(new GridLayout(2,2));
		
			JScrollPane dishStockScroll = new JScrollPane(dishStockPanel);
		tab1.add(dishStockScroll);
		
			JScrollPane ingredientStockScroll = new JScrollPane(ingredientStockPanel);
		tab1.add(ingredientStockPanel);
			
			JPanel setDishRestockPanel = new JPanel();
			setDishRestockPanel.setLayout(new BoxLayout(setDishRestockPanel, BoxLayout.Y_AXIS));
			setDishRestockPanel.add(dishBox);
				JTextField dishRestockValueField = new JTextField();
			setDishRestockPanel.add(dishRestockValueField);
				JButton setDishRestockButton = new JButton("Set Restock Value");
				setDishRestockButton.addActionListener(new ActionListener() {
														public void actionPerformed(ActionEvent e)
														{
															try{
															    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
																    
															    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
																outToServer.writeObject(new SetDishRestockMessage((String)dishBox.getSelectedItem(), Integer.parseInt(dishRestockValueField.getText())));
																}
															catch (Exception ex)
															{
																System.out.println(ex);
															}
														}
													});
			setDishRestockPanel.add(setDishRestockButton);
		tab1.add(setDishRestockPanel);
		
			JPanel setIngredientRestockPanel = new JPanel();
			setIngredientRestockPanel.setLayout(new BoxLayout(setIngredientRestockPanel, BoxLayout.Y_AXIS));
			setIngredientRestockPanel.add(ingredientBox);
				JTextField ingredientRestockValueField = new JTextField();
			setIngredientRestockPanel.add(ingredientRestockValueField);
				JButton setIngredientRestockButton = new JButton("Set Restock Value");
				setIngredientRestockButton.addActionListener(new ActionListener() {
														public void actionPerformed(ActionEvent e)
														{
															try{
															    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
															    
															    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
																outToServer.writeObject(new SetIngredientRestockMessage((String)ingredientBox.getSelectedItem(), Integer.parseInt(ingredientRestockValueField.getText())));
																}
															catch (Exception ex)
															{
																System.out.println(ex);
															}
														}
													});
			setIngredientRestockPanel.add(setIngredientRestockButton);
		tab1.add(setIngredientRestockPanel);
		
		tabs.add("Restock", tab1);
		
		JPanel tab2 = new JPanel(new GridLayout(3,3));
			JPanel panel1 = new JPanel();
			panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
				JComboBox<String> dishBox2 = new JComboBox(dishesName.toArray());
			panel1.add(dishBox2);
				JPanel inner1 = new JPanel(new FlowLayout());
					JLabel nameLabel = new JLabel("The name of the Dish");
				inner1.add(nameLabel);
					JTextField nameField = new JTextField(20);
				inner1.add(nameField);
					JButton setName = new JButton("Change name");
					//To note, can appear bugs if the same name is set to two Dishes/Ingredients
					setName.addActionListener(new ActionListener() {
												public void actionPerformed(ActionEvent e)
												{
													try{
													    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
													    
													    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
														outToServer.writeObject(new SetDishNameMessage((String)dishBox2.getSelectedItem(), nameField.getText()) );
														}
													catch (Exception ex)
													{
														System.out.println(ex);
													}
													
													try{
													    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
													    
													    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
													    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
													    
														outToServer.writeObject(new RefreshMessage());
														dishes = (ArrayList<Dish>) inFromServer.readObject();
													} catch (Exception exc)
														{
															System.out.println(exc);
														}
													synchronized(dishesName){
													dishesName.removeAll(dishesName);
													for(Dish dish : dishes)
														dishesName.add(dish.getName());
													}
													
													synchronized(dishBox){
													dishBox.removeAllItems(); 
													for(String ing : dishesName)
														dishBox.addItem(ing);
													}
													
													synchronized(dishBox2){
														dishBox2.removeAllItems(); 
													for(String ing : dishesName)
														dishBox2.addItem(ing);
													}
													inner1.revalidate();
												}
											});
					inner1.add(setName);
				panel1.add(inner1);	
				
			JPanel inner2 = new JPanel(new FlowLayout());
				JLabel priceLabel = new JLabel("The price of the Dish");
			inner2.add(priceLabel);
				JTextField priceField = new JTextField(20);
			inner2.add(priceField);
				JButton setPrice = new JButton("Change price");
				setPrice.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e)
											{
												try{
												    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
												    
												    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
													outToServer.writeObject(new SetDishPriceMessage((String)dishBox2.getSelectedItem(), Integer.parseInt(priceField.getText())) );
													}
												catch (Exception ex)
												{
													System.out.println(ex);
												}
											}
										});
				inner2.add(setPrice);
			panel1.add(inner2);
			
		JPanel inner3 = new JPanel(new FlowLayout());
			JLabel descriptionLabel = new JLabel("The description of the Dish");
		inner3.add(descriptionLabel);
			JTextField descriptionField = new JTextField(20);
		inner3.add(descriptionField);
			JButton setDescription = new JButton("Change description");
			setDescription.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e)
										{
											try{
											    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
											    
											    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
												outToServer.writeObject(new SetDishDescriptionMessage((String)dishBox2.getSelectedItem(), descriptionField.getText()) );
												}
											catch (Exception ex)
											{
												System.out.println(ex);
											}
										}
									});
			inner3.add(setDescription);
		panel1.add(inner3);
		
		//here should heave been an inner4 with the components to change the recipe, but I did not have the time....
			
			tab2.add(panel1);
		tabs.add("Restock2", tab2);
	
		JPanel tab3 = new JPanel(new BorderLayout());
			JScrollPane orderScroll = new JScrollPane(orderArea);
		tab3.add(orderScroll, BorderLayout.CENTER);
			JButton deleteDelivered = new JButton("Delete Delivered Orders");
			deleteDelivered.addActionListener(new ActionListener() {
												public void actionPerformed(ActionEvent e)
												{
													try{
													    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
													    
													    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
														outToServer.writeObject(new DeleteCompletedMessage() );
														}
													catch (Exception ex)
													{
														System.out.println(ex);
													}
												}
											});	
		tab3.add(deleteDelivered, BorderLayout.SOUTH);
		tabs.add("Orders", tab3);
					
		JPanel tab4 = new JPanel(new FlowLayout());
			JLabel staffLable = new JLabel("Set staff numbers ");
		tab4.add(staffLable);
			JTextField staffNumber = new JTextField(3);
		tab4.add(staffNumber);
			JButton setStaffButton = new JButton("Set staff numbers ");
			setStaffButton.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e)
											{
												try{
												    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
												    
												    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
													outToServer.writeObject(new SetStaffMessage(Integer.parseInt(staffNumber.getText())) );
													}
												catch (Exception ex)
												{
													System.out.println(ex);
												}
											}
										});
		tab4.add(setStaffButton);
		
			JLabel droneLable = new JLabel("Set drone numbers ");
		tab4.add(droneLable);
			JTextField droneNumber = new JTextField(3);
		tab4.add(droneNumber);
			JButton setDroneButton = new JButton("Set drone numbers ");
			setDroneButton.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e)
											{
												try{
												    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
												    
												    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
													outToServer.writeObject(new SetDroneMessage(Integer.parseInt(staffNumber.getText())) );
													}
												catch (Exception ex)
												{
													System.out.println(ex);
												}
											}
										});
		tab4.add(setDroneButton);
		tabs.add("Resource Management", tab4);
			
		
		this.getContentPane().add(tabs);
		this.setMinimumSize(new Dimension(1000, 400));
		this.pack();
		this.setVisible(true);
	}
	
	public void refreshAllStock()
	{
		try{
		    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
		    
		    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		    
			outToServer.writeObject(new RefreshMessage());
			dishes = (ArrayList<Dish>) inFromServer.readObject();
		} catch (Exception e)
			{
				System.out.println(e);
			}
		
		dishStockPanel.removeAll();

		for(Dish dish : dishes)
		{
			//System.out.println(1);
			JPanel panel = new JPanel(new FlowLayout());
				JLabel nameLabel = new JLabel(dish.getName());	
			panel.add(nameLabel);
				JLabel priceLabel = new JLabel("Price: " + String.valueOf(dish.getPrice()));
			panel.add(priceLabel);
				JLabel valueLabel = new JLabel("Current Stock: " + String.valueOf(dish.currentValue));
			panel.add(valueLabel);
			
			dishStockPanel.add(panel);
		}
		//System.out.println(dishesName);
		synchronized(dishStockPanel){dishStockPanel.revalidate();}
//		synchronized(dishBox){
//			dishBox.removeAllItems(); 
//			for(String ing : dishesName)
//				dishBox.addItem(ing);
//			}

		try{
		    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
		    
		    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			
			outToServer.writeObject(new RefreshIngredientMessage());
			ingredients = (ArrayList<Ingredient>) inFromServer.readObject();
		} catch (Exception e)
			{
				System.out.println(e);
			}
		
		ingredientStockPanel.removeAll();
		ingredientsName = new ArrayList<String>();
		
		for(Ingredient ingredient : ingredients)
		{
			JPanel panel = new JPanel(new FlowLayout());
				JLabel nameLabel = new JLabel(ingredient.getName());	
			panel.add(nameLabel);
				JLabel valueLabel = new JLabel("Current Stock: " + String.valueOf(ingredient.currentValue) + " " + ingredient.getUnit());
			panel.add(valueLabel);
			
			ingredientStockPanel.add(panel);
		}
		synchronized(ingredientStockPanel){ingredientStockPanel.revalidate();}
//		synchronized(ingredientBox){
//			ingredientBox.removeAllItems(); 
//			for(String ing : ingredientsName)
//				ingredientBox.addItem(ing);
//			}

	}
	
	public void refreshOrder()
	{
		try{
		    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
		    
		    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		    
			outToServer.writeObject(new RefreshOrderMessage());
			users = (CopyOnWriteArrayList<User>) inFromServer.readObject();
			
			synchronized (orderArea)
			{
				orderArea.setText(null);
				for (User user : users) {
					for (int i = 0; i < user.orders.size(); i++) {
						if (user.orders.get(i).getStatus() == true)
							orderArea.append(user.getUserName() + " Order " + i + " Status: Delivered" + newline);
						else
							orderArea.append(user.getUserName() + " Order " + i + " Status: Delivering..." + newline);
					}
				}
				orderArea.revalidate();
			}
		} catch (Exception e)
			{
				System.out.println(e);
			}
	}
}
