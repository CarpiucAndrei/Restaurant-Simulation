

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Comms extends Thread
{
	   private DataBase db;
	   private DishStock ds;
	   private IngredientStock is;
	   protected int          serverPort   = 8080;
	   protected ServerSocket serverSocket = null;
	   protected boolean      isStopped    = false;
	   protected Thread       runningThread= null;
	   protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
	
	public Comms(int port, DishStock ds, IngredientStock is, DataBase db)
	{
		this.db = db;
		this.serverPort = port;
		this.ds = ds;
		this.is = is;
	}
	
	public void run()
	{
		 openServerSocket();
	        while(! isStopped())
	        {
	            Socket clientSocket = null;
	            try {
	                clientSocket = this.serverSocket.accept();
	                System.out.println("Socket Established...");
	            } catch (IOException e) 
	            {
	                if(isStopped()) 
	                {
	                    System.out.println("Server Stopped.") ;
	                    break;
	                }
	                throw new RuntimeException("Error accepting client connection", e);
	            }
	            this.threadPool.execute( new WorkerRunnable(clientSocket, ds, is, db));
	        }
	        this.threadPool.shutdown();
	        System.out.println("Server Stopped.") ;
	}

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }
    
    public synchronized void stopServer(){
        this.isStopped = true;
        try {
    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("DataBase.txt")) );
	    		oos.writeObject(db.users);
				oos.flush();
				oos.close();
				
			ds.save();
			is.save();
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public ServerSocket getSocket() {
    	return this.serverSocket;
    }
}
