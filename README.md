# Restaurant-Simulation
A simulation of a Restaurant and the client and admin application.  
This project was made to illustrate the principles of multithreading in java, utilizing the tools in the Executors package and the server-client model, for a distributed application.  
# Running the Applications
First off all the server needs to be started, by executing the main.class in Restaurant-Simulation/Sushi preparation and delivery system/bin/Main.class  
To start the Client or Admin applications execute the Restaurant-Simulation/ClientApplication/bin/Main.class or the Restaurant-Simulation/BussinessApp/bin/Main.class respectively.  
# Features
The "Sushi preparation and delivery system" contains the "Stock and staff" basically the variables that are manipulated in the Client/Admin Apps. Each cook and drone is a separate thread that simulate cooking and delivering goods by sleeping for a certain period of time.  
The Comms class contains the server code that manages the communication layer between the applications and the data.  
The stocks and orders are saved at shutdown.
The Client application connects to the server to ask for authentication details or to register another costumer and allows to place and view your orders.  
The Admin App provides the ability to edit the "staff number", recipe, and restocking values.

