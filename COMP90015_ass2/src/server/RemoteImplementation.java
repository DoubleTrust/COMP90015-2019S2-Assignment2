package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import remote.RemoteInterface;


public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	// Define client amount
	//private int connectClients;
	
	// Constructor
	protected RemoteImplementation() throws RemoteException {
		//this.connectClients = 0;
	}
	
	@Override
	public void testRemoteFunction() throws RemoteException{
		System.out.println("remote function executed.");
	}
	

	

	
	

}
