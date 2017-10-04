

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class LogInRegisterGUI extends JFrame
{
	public LogInRegisterGUI ()
	{
		this.setTitle("LogIn or Register");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel logInPanel = new JPanel();
			logInPanel.setLayout(new BoxLayout(logInPanel, BoxLayout.Y_AXIS));
			logInPanel.setAlignmentX( Component.CENTER_ALIGNMENT );
			
		JLabel usernameLabel1 = new JLabel("UserName");
			logInPanel.add(usernameLabel1);
		JTextField userNameField1 = new JTextField(50);
			userNameField1.setMaximumSize( userNameField1.getPreferredSize() );
			logInPanel.add(userNameField1);
		JLabel passwordLabel1 = new JLabel("Password");
			logInPanel.add(passwordLabel1);
		JTextField passwordField1 = new JTextField(50);
			passwordField1.setMaximumSize( passwordField1.getPreferredSize() );
			logInPanel.add(passwordField1);
			
		JButton logInButton = new JButton("Log In");
			logInButton.addActionListener(new ActionListener(){
											public void actionPerformed(ActionEvent e)
											{
												User user = LogIn(new LogInMessage(userNameField1.getText(), passwordField1.getText()));
												if( user != null )
												{
													LogInRegisterGUI.this.dispose();
													TheApplication app = new TheApplication(user);
												}
												else {														
												JFrame frame = new JFrame("Log In failed");
												frame.getContentPane().setLayout(new FlowLayout());
												frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
												frame.getContentPane().add(new JLabel("Username or password wrong."));
												frame.pack();
												frame.setVisible(true);		
												}
												userNameField1.setText(" ");
												passwordField1.setText(" ");
											}
										});
				logInPanel.add(logInButton);
			
		tabs.add("Log In", logInPanel);
		
	JPanel registerPanel = new JPanel();
		registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
		registerPanel.setAlignmentX( Component.CENTER_ALIGNMENT );
		
	JPanel panel1 = new JPanel();	
			panel1.setLayout(new FlowLayout());
		JLabel usernameLabel2 = new JLabel("UserName");
			panel1.add(usernameLabel2);
		JTextField userNameField2 = new JTextField(20);
			userNameField2.setMaximumSize( userNameField2.getPreferredSize() );
			panel1.add(userNameField2);
	registerPanel.add(panel1);
		
	JPanel panel2 = new JPanel();	
		panel2.setLayout(new FlowLayout());
		JLabel passwordLabel2 = new JLabel("Password");
			panel2.add(passwordLabel2);
		JTextField passwordField2 = new JTextField(20);
			passwordField2.setMaximumSize( passwordField2.getPreferredSize() );
			panel2.add(passwordField2);
	registerPanel.add(panel2);
	
	JPanel panel3 = new JPanel();	
		panel3.setLayout(new FlowLayout());
		JLabel adressLabel = new JLabel("Adress");
			panel3.add(adressLabel);
		JTextField adressField = new JTextField(22);
			adressField.setMaximumSize( adressField.getPreferredSize() );
			panel3.add(adressField);
	registerPanel.add(panel3);	
	
	JPanel panel4 = new JPanel();	
		panel4.setLayout(new FlowLayout());
		JLabel postCodeLabel = new JLabel("Post Code");
			panel4.add(postCodeLabel);
		String[] postcodes =new String[3];
			postcodes[0] = "SO15 RX69";
			postcodes[1] = "1234";
			postcodes[2] = "1235";
		JComboBox poseCodeField = new JComboBox(postcodes);
		poseCodeField.setMaximumSize( poseCodeField.getPreferredSize() );
			panel4.add(poseCodeField);
		registerPanel.add(panel4);
		
//	JLabel nothing = new JLabel(" ");
//		registerPanel.add(nothing);
		
	JButton signUpButton = new JButton("Sign Up");
		signUpButton.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e)
										{
											if( contactServer(new RegisterMessage(userNameField2.getText(), passwordField2.getText(), adressField.getText(), (String) poseCodeField.getSelectedItem())) == true )
											{
												JFrame frame = new JFrame("Registered");
												frame.getContentPane().setLayout(new FlowLayout());
												frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
												frame.getContentPane().add(new JLabel("You have been registered."));
												frame.pack();
												frame.setVisible(true);		
											} else {														
												JFrame frame = new JFrame("SignUp failed");
												frame.getContentPane().setLayout(new FlowLayout());
												frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
												frame.getContentPane().add(new JLabel("This username is already in use."));
												frame.pack();
												frame.setVisible(true);		
											}	
											userNameField2.setText(" ");
											passwordField2.setText(" ");
											adressField.setText(" ");
										}
									 });
		registerPanel.add(signUpButton);
	tabs.add("Sign Up", registerPanel);
			
		this.add(tabs);
		
		this.setSize(new Dimension(320, 270));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public static boolean contactServer(Message message)
	{
		boolean bool = false;
		try{
		    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
		    
		    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		    DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
		    
			outToServer.writeObject(message);
			bool = inFromServer.readBoolean();
		} catch (Exception e)
			{
				System.out.println(e);
			}
		
		return bool;
	}
	
	public static User LogIn(LogInMessage message)
	{
		User user = null;
		try{
		    Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), 8000); //here you put the HostName (the computer from which the server is running)
		    
		    ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		    ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		    
			outToServer.writeObject(message);
			user = (User) inFromServer.readObject();
			//System.out.println(user.getUserName());
		} catch (Exception e)
			{
				System.out.println(e);
			}
		
		return user;
	}
}
