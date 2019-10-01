package server;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import server.DrawingBoard;
import remote.RemoteInterface;

/**
 * @author Sebastian Yan
 * @date 23/09/2019
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	
	private static final boolean True = false;
	private ArrayList<String> clientInfo;
	//private HashMap<String, DrawingBoard> whiteBoards;
	private DrawingBoard drawingBoard;
	
	/** 
	 * Default constructor 
	 */
	protected RemoteImplementation() throws RemoteException {
		this.clientInfo = new ArrayList<String>();
	}
	
	/** 
	 * Implementation of recording the client's username
	 */
	@Override
	public void uploadUserInfo(String username) throws RemoteException {
		this.clientInfo.add(username);
	}
	
	/** 
	 * Implementation of removing the client's username
	 */
	@Override
	public void RemoveClient(String username) throws RemoteException {
		this.clientInfo.remove(username);
	}
	
	/** 
	 * Implementation of removing all clients' info
	 */
	@Override
	public void removeAllInfo() throws RemoteException {
		this.clientInfo.clear();
		this.drawingBoard = null;
		//this.whiteBoards = null;
	}
	
	/** 
	 * Implementation of returning the client info
	 */
	@Override
	public ArrayList<String> getUserInfo() {
		return this.clientInfo;
	}
	
	/** 
	 * Implementation of creating the white board (manager only)
	 */
	@Override
	public void createWhiteBoard() throws RemoteException{
		try {
			// Determine whether the canvas has been created before
			if(this.drawingBoard == null || this.drawingBoard.getActive() == false) {
				// Create a new white board
				this.drawingBoard = new DrawingBoard();
				//DrawingBoard managerNewBoard= new DrawingBoard();
				
				// Set the active status
				this.drawingBoard.setActive(true);

				// Set the visibility
				drawingBoard.setVisible(true);
			}
			else{
				// If the frame is is created by clicking 'New Whiteboard' first
				//if(this.drawingBoard.getActive() != false) {
					// Find the manager's white board  (should be the manager's board only)
					//DrawingBoard managerBoard = whiteBoards.get("manager");
					
					this.drawingBoard.setVisible(true);
					
					// Ask user whether he/she wants to save before creating a new white board (white board already created before)
			    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Do you want to save before creating a new one?", "NOTIFICATION", JOptionPane.YES_NO_OPTION);      
	
			    	if(choice == JOptionPane.YES_OPTION){
			    		// Save the canvas
			    		this.drawingBoard.saveAs();
			    		//managerBoard.saveAs();
			    		
			        	// Dispose the frame
			    		this.drawingBoard.dispose();
			    		//managerBoard.dispose();
		        	}
			    	else if (choice == JOptionPane.CANCEL_OPTION){
			    		this.drawingBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			    		//managerBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			    	}
			    	
			    	else {	    		
						// Dispose the previous canvas directly
			    		this.drawingBoard.dispose();
			    		//managerBoard.dispose();
			    	}
				
					// Initialize the white board
					//this.whiteBoards = new HashMap<>();
					
					// Create a new white board
					this.drawingBoard = new DrawingBoard();
					//DrawingBoard managerNewBoard= new DrawingBoard();
					
					// Set the active status
					this.drawingBoard.setActive(true);
		
					// Set the visibility
					drawingBoard.setVisible(true);
					//managerNewBoard.setVisible(true);
					
					// Save the board to HashMap
					//whiteBoards.put("manager", managerNewBoard);
			}
//				// If the frame is created by clicking 'Open Whiteboard' first but the user did not load any white board
//				else {
//					this.drawingBoard.dispose();
//					
//					// Create a new white board
//					this.drawingBoard = new DrawingBoard();
//					this.drawingBoard.setActive(true);
//					//DrawingBoard managerNewBoard= new DrawingBoard();
//		
//					// Set the visibility
//					drawingBoard.setVisible(true);
//					//managerNewBoard.setVisible(true);
//					
//					// Save the board to HashMap
//					//whiteBoards.put("manager", managerNewBoard);
//				}
		
//			}		
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
		return true;
		/*DrawingBoard userBoard = whiteBoards.getOrDefault(userName,null);
		
		// If the client has already joined
		if(userBoard != null) {
			//System.out.println("userBoard not null");
			userBoard.setVisible(true);
			return false;
		}
		else {
			//System.out.println("userBoard  null");
			DrawingBoard managerBoard = whiteBoards.get("manager");
			userBoard = managerBoard;
			userBoard.setVisible(true);
			
			return true;
		}*/

		
	}
	
	/** 
	 * Implementation of closing all white boards (manager only)
	 */
	@Override
	public void closeWhiteBoard() throws RemoteException{
		if(this.drawingBoard != null) {
			this.drawingBoard.dispose();
		}
		//DrawingBoard managerBoard = whiteBoards.get("manager");
		//managerBoard.dispose();
		
		// Inform other clients
		//...
		
	}
	
	/** 
	 * Implementation of opening the white board (manager only) 
	 */
	@Override
	public void openWhiteBoard(String username) throws RemoteException{
		// Create an invisible white board if user chooses to open a white board firstly
		if(this.drawingBoard == null || this.drawingBoard.getActive() == false) {
			this.drawingBoard = new DrawingBoard();
			this.drawingBoard.setVisible(false);	
			this.drawingBoard = this.drawingBoard.openWhiteBoard();
			if(this.drawingBoard != null) {
				this.drawingBoard.hasSaved = 1;
				this.drawingBoard.setVisible(true);
				this.drawingBoard.setActive(true);
				this.drawingBoard.setDefaultCloseOperation(2);
			}

		}
		else if(this.drawingBoard.getActive() != false)  {
			// Ask user whether he/she wants to save before creating a new white board (white board already created before)
	    	int choice = JOptionPane.showConfirmDialog(new DrawingBoard(), "Do you want to save before opening another one?", "NOTIFICATION", JOptionPane.YES_NO_OPTION);      

	    	if(choice == JOptionPane.YES_OPTION){
	    		// Save the canvas
	    		this.drawingBoard.saveAs();
	    		//managerBoard.saveAs();
	    		
	        	// Dispose the frame
	    		this.drawingBoard.dispose();
	    		//managerBoard.dispose();
	    		
	    		// Open another one
	    		this.drawingBoard = this.drawingBoard.openWhiteBoard();
	    		if(this.drawingBoard != null) {
					this.drawingBoard.hasSaved = 1;
					this.drawingBoard.setVisible(true);
					this.drawingBoard.setActive(true);
					this.drawingBoard.setDefaultCloseOperation(2);
				}	
	    		 
        	}
	    	else if (choice == JOptionPane.CANCEL_OPTION){
	    		this.drawingBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    		//managerBoard.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    	}
	    	else {	    		
				// Dispose the previous canvas directly
	    		this.drawingBoard.dispose();
	    		//managerBoard.dispose();
	    		
	    		// Open another one
	    		this.drawingBoard = this.drawingBoard.openWhiteBoard();
	    		if(this.drawingBoard != null) {
					this.drawingBoard.hasSaved = 1;
					this.drawingBoard.setVisible(true);
					this.drawingBoard.setActive(true);
					this.drawingBoard.setDefaultCloseOperation(2);
	    		}

	    	}
			
		}
	
	}

	

	
	

}
