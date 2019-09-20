
package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

import remote.RemoteInterface;

/**
 * @author Sebastian Yan
 * @date 20/09/2019
 */
public class Client {
	/** 
	 * Define the host and port number 
	 */
	private String host;
	private String port;
	public RemoteInterface remoteInterface;
	
	public Client() {
		this.host = JOptionPane.showInputDialog(null, "Please type the host name:\n", "Port number required", JOptionPane.PLAIN_MESSAGE);
		this.port = JOptionPane.showInputDialog(null, "Please type a port number:\n", "Port number required", JOptionPane.PLAIN_MESSAGE);
	}
	
	/**
	 *  Client request for setting up connection	
	 */
	public void buildConnection(){
		try {
			// Set up connections
			Registry registry = LocateRegistry.getRegistry(this.host, Integer.parseInt(this.port));
			
			//Retrieve the stub/proxy for the remote operation from the registry
			remoteInterface = (RemoteInterface) registry.lookup("RemoteOperation");
			
						
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	/**
     * Main function
     */
	public static void main(String[] args) {
		// Test connections with server using RMI
		Client client = new Client();
		client.buildConnection();
			
		
		// Test remote function after setting up the connection
		try {
			client.remoteInterface.testRemoteFunction();
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
	}
}
