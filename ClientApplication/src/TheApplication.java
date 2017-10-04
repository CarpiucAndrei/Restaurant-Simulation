

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class TheApplication extends JFrame
{
	final static String newline = "\n";
	User user;
	ArrayList<Dish> dishes = new ArrayList<Dish>();
	ArrayList<String> order = new ArrayList<String>();
	int price;
	JPanel orderPanel = new JPanel();
	JTextArea basket = new JTextArea();
	JLabel priceField = new JLabel();
//	JScrollPane scroll = new JScrollPane();
	
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
				TheApplication.this.refreshStock();
				try{ this.sleep(5*1000); }catch (Exception e) { System.out.println(e);}
			}
		}
	 }
	
	public TheApplication(User user)
	{
		Refresh r = new Refresh();
			r.start();
						
		this.addWindowListener(new WindowAdapter()
				        	{
				            public void windowClosing(WindowEvent e)
				            {
				                r.stopExecute();
				                e.getWindow().dispose();
				            }
				        });			
		
		this.user = user;
		this.setTitle("The App");
		
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel panel1 = new JPanel(new GridLayout(1, 3));
		
		JButton orderButton = new JButton("Send Order");
			orderButton.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e)
											{
												try{
												    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
												    
												    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
												    DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
												    Order orderCurrent = new Order(order, user.getPostCode());
												    
													outToServer.writeObject(new OrderMessage(orderCurrent, user));
													if(inFromServer.readBoolean() == true)
													{
														TheApplication.this.user.orders.add(orderCurrent);
													}
												} catch (Exception ex)
													{
														System.out.println(ex);
													}
											}
									});
		panel1.add(orderButton);
		
		JPanel basketPanel = new JPanel();
			basket = new JTextArea();
			basket.setEditable(false);
			priceField = new JLabel();
		basketPanel.add(basket);
		basketPanel.add(priceField);
		
		panel1.add(basketPanel);
		orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scroll = new JScrollPane(orderPanel);
		//scroll.setMinimumSize(new Dimension(700, 700));
		panel1.add(scroll);
		
		tabs.add("Order", panel1);
		
		JPanel p = new JPanel(new GridLayout(1,2));
			JTextArea orderDisplay = new JTextArea();
			orderDisplay.setEditable(false);
				for(int i=user.orders.size() - 1; i>=0; i--)
				{
					if(user.orders.get(i).getStatus())
						orderDisplay.append("Order" + i + " " + "Status: Delivered" + newline);
					else
						orderDisplay.append("Order" + i + " " + "Status: Delivering..." + newline);
				}
			JScrollPane scrollyyyy = new JScrollPane(orderDisplay);
		p.add(scrollyyyy);
		
		
		JButton ref = new JButton("Refresh");
			ref.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e)
									{
										orderDisplay.setText(null);
										try{
										    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
										    
										    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
										    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
										    
											outToServer.writeObject(new ViewMessage(user));
											ArrayList<Order> updatedOrder = (ArrayList<Order>) inFromServer.readObject();
											
											for(int i=updatedOrder.size() - 1; i>=0; i--)
											{
												//System.out.println(user.orders.size());
												if(updatedOrder.get(i).getStatus() == true)
													orderDisplay.append("Order" + i + " " + "Status: Delivered" + newline);
												else
													orderDisplay.append("Order" + i + " " + "Status: Delivering..." + newline);
											}
										} catch (Exception exc)
											{
												System.out.println(exc);
											}
									}
							});
		p.add(ref);
		
		tabs.add("History", p);	
		
		this.getContentPane().add(tabs);
		this.setMinimumSize(new Dimension(1000, 400));
		this.pack();
		this.setVisible(true);
	}
	
	public void refreshStock()
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
		
		orderPanel.removeAll();
		
		for(Dish dish : dishes)
		{
			JPanel panel = new JPanel(new FlowLayout());
				JLabel nameLabel = new JLabel(dish.getName());	
			panel.add(nameLabel);
				JLabel priceLabel = new JLabel(String.valueOf(dish.getPrice()));
			panel.add(priceLabel);
				JLabel valueLabel = new JLabel("Current Stock: " + String.valueOf(dish.currentValue));
			panel.add(valueLabel);
				
			JButton addButton = new JButton("Add");
				addButton.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e)
											{
												order.add(nameLabel.getText());
												price += Integer.parseInt(priceLabel.getText());
												basket.append(nameLabel.getText() + newline);
												priceField.setText(String.valueOf(price) );
											}
										 });
			panel.add(addButton);
			
			JButton removeButton = new JButton("Remove");
			removeButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e)
										{
											if(order.contains(nameLabel.getText()) )
											{
											order.remove(nameLabel.getText());
											price -= Integer.parseInt(priceLabel.getText());
											basket.setText(null);
											for(String dish : order)
												basket.append(dish + newline);
											priceField.setText(String.valueOf(price) );
											}
										}
									 });
			panel.add(removeButton);
			orderPanel.add(panel);
		}
		//orderPanel.add(panel);
		orderPanel.setMinimumSize(new Dimension(700, 700));
		synchronized(orderPanel){orderPanel.revalidate();}
	}
}
