package server;

import remote.RemoteInterface;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

/**
 * @author Sebastian Yan
 * @date 21/09/2019
 */
public class RemoteServer {
	
	/**
	 * Define port number
	 */
	private String port;

	
	/**
	 * Request the port number
	 */
	public RemoteServer() {
		
		this.port = JOptionPane.showInputDialog(null, "Please type a port number:\n", "Port number required", JOptionPane.PLAIN_MESSAGE);
		
		while (this.port.length() == 0) {
			this.port = JOptionPane.showInputDialog(null, "Please type a port number:\n", "Port number required", JOptionPane.PLAIN_MESSAGE);
		}
		
		// Verify user's input
		// ...(Verification code to be added)
	}
	
	/**
	 * Request the port number
	 */
	public boolean initiateRMI() {	
		try {
			// Create the interface of remote service
			// Note: RMI is multi-threaded. What needs to be done is to guarantee the object is thread-safe.
			RemoteInterface remoteMethods = new RemoteImplementation();
			
			// Create the registry (localhost with specified port number) and publish the remote object's stub in the registry under the name "RemoteOperation"
			Registry registry = LocateRegistry.getRegistry(Integer.parseInt(this.port));
			registry.rebind("RemoteOperation", remoteMethods);
			
			// If there is no exception
			return true;
			
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} 
	}
	
}
