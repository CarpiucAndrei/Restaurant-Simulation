# Restaurant-Simulation
A simulation of a Restaurant and the client and admini application.  
This project was made to ilustrate the principles of multithreading in java, utilising the tools in the Executors package and the server-client model, for a distributed application.  
# Running the Applications
First off all the server needs to be started, by running the main.class in
Each cook is a thread that takes a time to prepare a certain dish and than a drone (represented by a thread) delivers it to a client.The comunication between the client app, admin app and the "restaurant" is done using a server in the comms class. At shut down the state of the system is saved.  
This project is a proof of concept for a possible real world system.
