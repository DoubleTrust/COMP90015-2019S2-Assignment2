package server;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import server.DrawingBoard;
import remote.RemoteInterface;

/**
 * @author Sebastian Yan
 * @date 23/09/2019
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	
	private ArrayList<String> clientInfo;
	private DrawingBoard canvas;
	
	/** 
	 * Default constructor 
	 */
	protected RemoteImplementation() throws RemoteException {
		this.clientInfo = new ArrayList<String>();
	}
	
	/** 
	 * Constructor with client's username
	 */
	protected RemoteImplementation(String username) throws RemoteException {
		this.clientInfo.add(username);
	}
	
	/** 
	 * Return the client info
	 */
	public ArrayList<String> getUserInfo() {
		return this.clientInfo;
	}

	
	
	
	@Override
	public void openCanvas() throws RemoteException{
		// Determine whether the canvas is created before
		System.out.println("test");
		if(this.canvas == null) {
			// Create a new canvas
			this.canvas= new DrawingBoard();
			canvas.setAlwaysOnTop(true);
			
			// Confirm exit if the user wants to quit
			canvas.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			        if (JOptionPane.showConfirmDialog(canvas, 
			            "Are you sure you want to close this window? \nYou can open it again by clicking 'Open Canvas'.", "Close Window?", 
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			        	canvas.setVisible(false);
			        }
			    }
			});
			
			// Set the visibility
			this.canvas.setVisible(true);
		}
		else {
			this.canvas.setVisible(true);
		}			
	}
	
	@Override
	public void disposeCanvas() throws RemoteException {
		this.canvas.dispose();
		this.canvas = null;
	}

	

	
	

}
