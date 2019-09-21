
package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

import remote.RemoteInterface;

/**
 * @author Sebastian Yan
 * @date 21/09/2019
 */
public class Client {
	/** 
	 * Define the host and port number 
	 */
	private String host;
	private String port;
	public RemoteInterface remoteInterface;
	
	/** 
	 * Constructor
	 */
	public Client() {
		// Get the host name and port number
		this.host = JOptionPane.showInputDialog(null, "Please type the host name:\n", "Port number required", JOptionPane.PLAIN_MESSAGE);
		this.port = JOptionPane.showInputDialog(null, "Please type a port number:\n", "Port number required", JOptionPane.PLAIN_MESSAGE);
	
		// Verify the input
		// code to be added...
	}
	
	/**
	 *  Setting up connection to the server	
	 */
	public void buildConnection(){
		try {
			// Connect to registry using host name and port number
			Registry registry = LocateRegistry.getRegistry(this.host, Integer.parseInt(this.port));
			
			//Retrieve the stub/proxy for the remote operation from the registry
			remoteInterface = (RemoteInterface) registry.lookup("RemoteOperation");
			
						
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	/**
     * Main function to test remote services
     */
	public static void main(String[] args) {
		
		// Connect server using RMI
		Client client = new Client();
		client.buildConnection();
			
		
		// Utilize remote functions
		try {
			client.remoteInterface.openCanvas();
			
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
	}
}
