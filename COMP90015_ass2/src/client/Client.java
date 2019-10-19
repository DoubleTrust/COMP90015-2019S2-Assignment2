package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import remote.RemoteInterface;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 19/10/2019
 */
public class Client {

	//Define the host and port number 
	private String host;
	private String port;
	public String username;	
	public RemoteInterface remoteInterface;
	public DrawingBoard whiteBoard;
	
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
			Registry registry = LocateRegistry.getRegistry(this.host.trim(), Integer.parseInt(this.port));
			
			// Retrieve the stub/proxy for the remote operation from the registry
			this.remoteInterface = (RemoteInterface) registry.lookup("RemoteOperation");
			
			return true;
						
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
     * Get usernames to be displayed
     */
	public String[] getDisplayUserInfo() {
		try {
			// Get user lists
			ArrayList<String> userList = this.remoteInterface.getUserInfo();
			
			// Mark the specific user name (this client's name)
			userList.set(userList.indexOf(this.username), this.username + " (you)");
			
			// Return the user name
			return (String[])userList.toArray(new String[0]);

			
		} catch (RemoteException e) {
			System.out.println("RemoteException");
			e.printStackTrace();		
			return null;
		} catch (NullPointerException e1) {
			System.out.println("NULLPointerException");
			e1.printStackTrace();
			// Return null if the user has been kicked.
			return null;			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException");
			return new String[]{"Being kicked."};
		} 
		
	}

  	/**
     * Get the whole usernames to judge if it is null
     */
	public String[] getUserInfo() {
		// Get user lists
		try {
			ArrayList<String> userList = this.remoteInterface.getUserInfo();	
			return (String[])userList.toArray(new String[0]);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	// --------------------------------------------------------------------------------
	/**
	 *  Get dialogue to be displayed
	 */
	public ArrayList<String> getDialogue() {
		try {
			// Get dialogue list
			ArrayList<String> dialogueList = this.remoteInterface.getDialogue();
			return dialogueList;
			
		} catch (RemoteException e) {
			System.out.println("RemoteException");
			e.printStackTrace();		
			return null;
		} catch (NullPointerException e1) {
			System.out.println("NULLPointerException");
			e1.printStackTrace();
			return null;			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException");
			return new ArrayList<String> ();
		} 
	}
	
	// -----------------------------------------------------------------------------------
	
	

	/*
	 * Disconnect from server
	 */
	public void disconnect() {
		this.remoteInterface = null;
	}
}
