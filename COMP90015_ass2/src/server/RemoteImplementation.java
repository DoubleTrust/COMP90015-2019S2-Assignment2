package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import server.DrawingBoard;
import remote.RemoteInterface;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 07/10/2019
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	
	// Define the an arrayList to store client username, an arrayList to store clients' white boards and a white board for manager only
	private ArrayList<String> clientInfo;
	private DrawingBoard managerWhiteBoard;
	private HashMap<String, DrawingBoard> clientWhiteBoards;
	
	/** 
	 * Default constructor 
	 */
	protected RemoteImplementation() throws RemoteException {
		this.clientInfo = new ArrayList<String>();
		this.clientWhiteBoards = new HashMap<String, DrawingBoard>();
	}
	
	/** 
	 * Implementation of recording the client's username
	 */
	@Override
	public void uploadUserInfo(String username) throws RemoteException {
		this.clientInfo.add(username);
	}
	
	/** 
	 * Implementation of removing the client's username and corresponding white board
	 */
	@Override
	public void RemoveClient(String username) throws RemoteException {
		this.clientInfo.remove(username);
		
		// remove the corresponding white board
		//(to be added...)
	}
	
	/** 
	 * Implementation of removing all clients' info
	 */
	@Override
	public void removeAllInfo() throws RemoteException {
		this.clientInfo.clear();
		this.managerWhiteBoard = null;
		this.clientWhiteBoards = null;
	}
	
	/** 
	 * Implementation of returning the client info
	 */
	@Override
	public ArrayList<String> getUserInfo() {
		return this.clientInfo;
	}
	
	/** 
	 * Implementation of creating the manager's white board 
	 */
	@Override
	public void createWhiteBoard() throws RemoteException{
		try {
			// Determine whether the canvas has been created before
			if(this.managerWhiteBoard == null || this.managerWhiteBoard.getActive() == false) {
				// Create a new white board
				this.managerWhiteBoard = new DrawingBoard();
				
				// Set the active status
				this.managerWhiteBoard.setActive(true);

				// Set the visibility
				this.managerWhiteBoard.setVisible(true);				
			}
			else{			
				this.managerWhiteBoard.setVisible(true);
				
				// Ask user whether he/she wants to save before creating a new white board (white board already created before)
		    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Do you want to save before creating a new one?", "NOTIFICATION", JOptionPane.YES_NO_OPTION);      

		    	if(choice == JOptionPane.YES_OPTION){
		    		// Save the canvas
		    		this.managerWhiteBoard.saveAs();
		    		
		        	// Dispose the frame
		    		this.managerWhiteBoard.dispose();
	        	}
		    	else if (choice == JOptionPane.CANCEL_OPTION){
		    		this.managerWhiteBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		    	}		    	
		    	else {	    		
					// Dispose the previous canvas directly
		    		this.managerWhiteBoard.dispose();
		    	}
				
				// Create a new white board
				this.managerWhiteBoard = new DrawingBoard();
				//DrawingBoard managerNewBoard= new DrawingBoard();
				
				// Set the active status
				this.managerWhiteBoard.setActive(true);
	
				// Set the visibility
				this.managerWhiteBoard.setVisible(true);								
			}
			
			// A listener to keep updating the manager's white board
			Thread canvasListener = new Thread() {
				@Override
				public void run() {
					try {
						while(true) {
							managerWhiteBoard.canvas.repaint();
						} 
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					
				}
			};
			canvasListener.start();
	
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception.");
			
		}
	}
	
	/** 
	 * Implementation of joining the white board (manager only)
	 */
	@Override
	public boolean joinWhiteBoard(String userName) throws RemoteException{
		
		DrawingBoard userBoard = clientWhiteBoards.getOrDefault(userName,null);
		
		// If the client has already joined
		if(userBoard != null) {
			userBoard.setVisible(true);
			return false;
		}
		else {
			// Create a new white board for the client
			DrawingBoard newClientBoard = new DrawingBoard(this.managerWhiteBoard.image);
			newClientBoard.setVisible(true);
			
			// Put the new white board into HashMap
			clientWhiteBoards.put(userName, newClientBoard);
			
			// A listener to keep updating the client's white board
			Thread canvasListener = new Thread() {
				@Override
				public void run() {
					try {
						while(true) {
							newClientBoard.canvas.repaint();
						} 
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
			};
			// Start the listener
			canvasListener.start();			
			return true;
		}	
	}
	
	
	/*class canvasListener implements Runnable{
		@Override
		public void run(Drawin) {
			try {
				while(true) {					
					
				}
			}catch(NullPointerException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	/** 
	 * Implementation of closing manager's white board 
	 */
	@Override
	public void closeManagerBoard() throws RemoteException{
		if(this.managerWhiteBoard != null) {
			this.managerWhiteBoard.dispose();
		}
		
		// Inform other clients here?
		//...
		
	}
	
	/** 
	 * Implementation of opening the white board (manager only) 
	 */
	@Override
	public void openWhiteBoard(String username) throws RemoteException{
		// Create an invisible white board if user chooses to open a white board firstly
		if(this.managerWhiteBoard == null || this.managerWhiteBoard.getActive() == false) {
			this.managerWhiteBoard = new DrawingBoard();
			this.managerWhiteBoard.setVisible(false);	
			this.managerWhiteBoard = this.managerWhiteBoard.openWhiteBoard();
			if(this.managerWhiteBoard != null) {
				this.managerWhiteBoard.hasSaved = 1;
				this.managerWhiteBoard.setVisible(true);
				this.managerWhiteBoard.setActive(true);
				this.managerWhiteBoard.setDefaultCloseOperation(2);
			}
		}
		else if(this.managerWhiteBoard.getActive() != false)  {
			// Ask user whether he/she wants to save before creating a new white board (white board already created before)
	    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Do you want to save before opening another one?", "NOTIFICATION", JOptionPane.YES_NO_OPTION);      

	    	if(choice == JOptionPane.YES_OPTION){
	    		// Save the canvas
	    		this.managerWhiteBoard.saveAs();
	    		//managerBoard.saveAs();
	    		
	        	// Dispose the frame
	    		this.managerWhiteBoard.dispose();
	    		//managerBoard.dispose();
	    		
	    		// Open another one
	    		this.managerWhiteBoard = this.managerWhiteBoard.openWhiteBoard();
	    		if(this.managerWhiteBoard != null) {
					this.managerWhiteBoard.hasSaved = 1;
					this.managerWhiteBoard.setVisible(true);
					this.managerWhiteBoard.setActive(true);
					this.managerWhiteBoard.setDefaultCloseOperation(2);
				}	    		 
        	}
	    	else if (choice == JOptionPane.CANCEL_OPTION){
	    		this.managerWhiteBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    	}
	    	else {	    		
				// Dispose the previous canvas directly
	    		this.managerWhiteBoard.dispose();
	    		
	    		// Open another one
	    		this.managerWhiteBoard = this.managerWhiteBoard.openWhiteBoard();
	    		if(this.managerWhiteBoard != null) {
					this.managerWhiteBoard.hasSaved = 1;
					this.managerWhiteBoard.setVisible(true);
					this.managerWhiteBoard.setActive(true);
					this.managerWhiteBoard.setDefaultCloseOperation(2);
	    		}
	    	}			
		}	
	}
}