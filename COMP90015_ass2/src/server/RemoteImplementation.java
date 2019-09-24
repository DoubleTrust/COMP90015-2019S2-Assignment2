package server;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import server.DrawingBoard;
import remote.RemoteInterface;

/**
 * @author Sebastian Yan
 * @date 23/09/2019
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	
	private ArrayList<String> clientInfo;
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
	public void RecordUserInfo(String username) throws RemoteException {
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
	 * Implementation of returning the client info
	 */
	@Override
	public ArrayList<String> getUserInfo() {
		return this.clientInfo;
	}
	
	/** 
	 * Implementation of creating the white board
	 */
	@Override
	public boolean createWhiteBoard() throws RemoteException{
		
		// Determine whether the canvas has been created before
		if(this.drawingBoard == null) {
			// Create a new canvas
			this.drawingBoard= new DrawingBoard();
				
			// Set the visibility
			this.drawingBoard.setVisible(true);
			
			return true;
		}
		else {
			return false;
		}			
	}

	/** 
	 * Implementation of opening the white board
	 */
	@Override
	public void openWhiteBoard() throws RemoteException{
		this.drawingBoard.setVisible(true);		
	}
	
	/** 
	 * Implementation of disposing the white board (manager only)
	 */
	@Override
	public void disposeCanvas() throws RemoteException {
		this.drawingBoard.dispose();
		this.drawingBoard = null;
	}

	

	
	

}
