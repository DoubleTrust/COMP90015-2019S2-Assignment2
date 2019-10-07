package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import remote.RemoteInterface;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 07/10/2019
 */
public class Client {

	//Define the host and port number 
	private String host;
	private String port;
	public String username;
	public RemoteInterface remoteInterface;
	
	/** 
	 * Constructor to record the login information
	 */
	public Client(String hostname, String port, String username) {
		// Get the host name and port number
		this.host = hostname;
		this.port = port;
		this.username = username;
		
	}
	
	/**
	 *  Setting up connection to the server	
	 */
	public boolean buildConnection(){
		try {
			// Connect to registry using host name and port number
			Registry registry = LocateRegistry.getRegistry(this.host.trim().toString(), Integer.parseInt(this.port));
			
			// Retrieve the stub/proxy for the remote operation from the registry
			this.remoteInterface = (RemoteInterface) registry.lookup("RemoteOperation");
			
			return true;
						
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
     * Display username(s)
     */
	public String displayUserInfo() {
		// (Similar to auto-update dictionary info)
		try {
			// Get user lists
			ArrayList<String> userList = this.remoteInterface.getUserInfo();
			
			// Convert it to String
			StringBuilder userString = new StringBuilder(); 
			for(String user: userList) {
				userString.append(user);
				userString.append("\n");
			}
			
			// Return the user name
			return userString.toString();

			
		} catch (RemoteException e) {
			System.out.println("RemoteException");
			e.printStackTrace();		
			return "ERROR";
		} catch (NullPointerException e1) {
			System.out.println("NULLPointerException");
			e1.printStackTrace();
			return "";
			
		}
	}
	
	/*
	 * Disconnect from server
	 */
	public void disconnect() {
		this.remoteInterface = null;
	}
}
