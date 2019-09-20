package server;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import remote.RemoteInterface;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

public class RemoteServer {
	// Define server socket and port number
	private ServerSocket serverScoket;
	private String port;
	
	// Prepare for multi-threading service
	private ExecutorService service;
	
	
	// Request the port number
	public RemoteServer() {
		this.port = JOptionPane.showInputDialog(null, "Please type a port number:\n", "Port number required", JOptionPane.PLAIN_MESSAGE);
		
		// Verify user's input
		// ...(Verification code to be added)
	}
	
	// Initiate RMI
	public boolean initiateRMI() {	
		try {
			// Create the interface of remote service
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
	
	// Initialize the server (using remote methods?)
	public boolean initiateSocket() {
		try {
			// Set up server socket
			serverScoket = new ServerSocket(Integer.parseInt(port));
			
			// Set up threads
			service = Executors.newFixedThreadPool(5);	
			
			// Create a thread to keep listening to the client connection request
			Thread listenLoop = new Thread(new Listener());
			listenLoop.start();
	
			
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Server Initiation error.");
			return false;
		}
		
	}
	
	/**
	 * Create this thread(as an inner class) to prevent GUI block because of the while loop (keep listening)
	 */
	class Listener implements Runnable{
		public void run() {
			while(true) {
				try {
					// Create a socket to accept the client
					Socket sServer = serverScoket.accept();
					
					// executes thread to handle that client
					service.execute(new connectClients(sServer));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Create a thread for each connection (thread-per-connection)
	 */
	class connectClients implements Runnable{	
		private Socket connect_socket;
		private OutputStream sOut;
		private DataOutputStream msgOut;
		private InputStream sIn;
		private DataInputStream msgIn;
		
		// Pass the socket info
		public connectClients(Socket socket) {
			this.connect_socket = socket;
			
		}
		
		@Override
		public void run() {
			// If this statement is executed
			RemoteServer_GUI.statusArea.append("A client connected. Thread " + Thread.currentThread().getId() + " starts working...\n");
			
			
		}
	}
	
	
}
