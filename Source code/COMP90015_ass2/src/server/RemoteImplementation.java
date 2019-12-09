package server;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import server.DrawingBoardMonitor;
import remote.RemoteInterface;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 19/10/2019
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	
	// Define the an arrayList to store client username, an arrayList to store clients' white boards and a white board for manager only
	private ArrayList<String> clientInfo;
	private DrawingBoardMonitor BoardMonitor;
	private String kickUsername;
	
	//Define the require and return allow to the client
	public static int require=0;
	
	public int getRequire() throws RemoteException{
		return require;
	}
	public void setRequire(int require)throws RemoteException {
		this.require = require;	
	}
	
	public static int allow=0;
	
	public int getAllow() throws RemoteException{
		return allow;
	}
	public void setAllow(int allow) throws RemoteException {
		this.allow = allow;
	}
	
	// ----------------------------------------------------------------
	private ArrayList<String>  dialogueList;
	// ----------------------------------------------------------------
	
	/** 
	 * Default constructor 
	 */
	protected RemoteImplementation() throws RemoteException {
		this.clientInfo = new ArrayList<String>();
		this.dialogueList = new ArrayList<String>();
	}
	
	/** 
	 * Implementation of recording the client's username
	 */
	@Override
	public void uploadUserInfo(String username) throws RemoteException {
		this.clientInfo.add(username);
		
		System.out.println("Current users:");
		for(String name:this.clientInfo) {
            System.out.print(name+ " ");
        }
		 System.out.println();
	}
	
	
	// ------------------------------------------------------------------------
	/**
	 * Implementation of recording the dialogue
	 */
	@Override
	public void updateDialogue(String dialogue, String username) throws RemoteException {
		this.dialogueList.add("["+username+"]: "+dialogue);
	}
	// ------------------------------------------------------------------------
	
	
	/** 
	 * Implementation of removing the client's username and corresponding white board
	 */
	@Override
	public void RemoveUser(String username) throws RemoteException {
		this.clientInfo.remove(username);
		
		System.out.println("Current users:");
		for(String name:this.clientInfo) {
            System.out.print(name+ " ");
        }
		 System.out.println();
		
	}
	
	/** 
	 * Implementation of removing all clients' info
	 */
	@Override
	public void removeAllInfo() throws RemoteException {
		this.clientInfo.clear();
		this.BoardMonitor = null;
		System.out.println("All users have left the system.");
	}
	
	/** 
	 * Implementation of setting kick username
	 */
	@Override
	public void setKickUsername(String kickname) throws RemoteException {
		this.kickUsername = kickname;
	}
	
	/** 
	 * Implementation of kicking the user
	 */
	@Override
	public String KickUser() throws RemoteException {
		// If no user is selected or user list cannot find the name
		if(this.kickUsername.length() == 0 || this.clientInfo.contains(this.kickUsername) == false) {
			return "NOTSELECTED";
		}
		else if(this.clientInfo.contains(this.kickUsername) == false){
			return "NOTINTHELIST";
		}
		else {
			// Remove the user from user list 
			this.clientInfo.remove(this.kickUsername);
			
			System.out.println("Current users:");
			for(String name:this.clientInfo) {
	            System.out.print(name+ " ");
	        }
			 System.out.println();
			
			return "";
		}
	}
	
	/** 
	 * Implementation of returning the client info
	 */
	@Override
	public ArrayList<String> getUserInfo() {
		return this.clientInfo;
	}
	
	// ------------------------------------------------------------------------
	/** 
	 * Implementation of returning the dialogue
	 */
	@Override
	public ArrayList<String> getDialogue() {
		return this.dialogueList;
	}
	// ------------------------------------------------------------------------
	
	/** 
	 * Implementation of creating the manager's white board 
	 */
	@Override
	public void createWhiteBoard() throws RemoteException{

		try {
			// Determine whether the canvas has been created before
			if(this.BoardMonitor == null) {
				// Create a new white board
				this.BoardMonitor = new DrawingBoardMonitor();

				// Set the visibility and title
				this.BoardMonitor.setTitle("Board Monitor");
				this.BoardMonitor.setVisible(true);				
			}
			else {
				// Dispose the previous one
				this.BoardMonitor.dispose();
				
				// Create a new white board
				this.BoardMonitor = new DrawingBoardMonitor();

				// Set the visibility and title
				this.BoardMonitor.setTitle("Board Monitor");
				this.BoardMonitor.setVisible(true);	
			}	
	
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception.");
			//return null;
		}
	}
	
	
	
	/** 
	 * Implementation of getting the status of the white board  
	 */
	public byte[] getBoardStatus() throws RemoteException{	
		try {
			// Convert the buffered image to bytes and return
			BufferedImage outputImg = this.BoardMonitor.image;
			byte[] imageBytes = null;			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(outputImg, "jpg", bos);
			bos.flush();
			imageBytes = bos.toByteArray();
			bos.close();
			
			return imageBytes;
			
			
		} catch (IOException e) {
			System.out.println("IOException caught");
			e.printStackTrace();
			return null;
		}
	}
	
	/** Implementation of setting the status of the white board 
	 * 
	 */
	public void updateBoardStatus(byte[] imageBytes) throws RemoteException{	
		try {
			// Convert to buffer image
			InputStream in = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(in);
			in.close();			
		
			this.BoardMonitor.setCanvas(image);
			
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	} 
	
	/** 
	 * Implementation of returning whether the manager tries to open a white board   
	 */
	public boolean canSynchronize() throws RemoteException{
		return this.BoardMonitor.synchronize;
	}
	
	/** 
	 * Implementation of changing the synchronization status when the manager tries to open a white board
	 */
	public void changeSynchronization(boolean bool) throws RemoteException{
		this.BoardMonitor.synchronize = bool;
	}
	
	/** 
	 * interface to clear the content of a white board
	 */
	public boolean clearContent() throws RemoteException{
		return this.BoardMonitor.clearContent();
	}	
	
	/** 
	 * Implementation of joining the white board (client only)
	 */
	@Override
	public byte[] joinWhiteBoard() throws RemoteException{
		try {
			if(this.BoardMonitor == null) {
				return null;
			}
			else {
				
				// Convert the buffered image of monitor board to bytes and return
				byte[] imageBytes = null;			
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(this.BoardMonitor.image, "jpg", bos);
				bos.flush();
				imageBytes = bos.toByteArray();
				bos.close();
				
				return imageBytes;
			}
		} catch (Exception e) {
			System.out.println("Exception caught.");
			e.printStackTrace();
			return null;
		}	
	}
	
	/** 
	 * Implementation of closing manager's white board 
	 */
	@Override
	public void closeManagerBoard() throws RemoteException{
		if(this.BoardMonitor != null) {
			this.BoardMonitor.dispose();
		}		
	}
	
	/**
	 * allow or not
	 */
	public void AllowClient() throws RemoteException{
		require=1;
	}
	/**
	 * return the result of allow or not
	 */
	public int returnAllow() throws RemoteException{
		while(getRequire()==1){
	       System.out.println("");
		}
		return getAllow();
	}
	/** 
	 * Implementation of opening the white board (manager only) 
	 */
	@Override
	public void openWhiteBoard(byte[] imageBytes) throws RemoteException{
		try {
			// Convert the byte to buffer image
			InputStream in = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(in);
			in.close();	
			
			if(this.BoardMonitor != null) {
				this.BoardMonitor.synchronize = false;
				this.BoardMonitor.openTriger = true;
				
				// Update the server's board monitor
				this.BoardMonitor.setCanvas(image);
	
	
				this.BoardMonitor.openTriger = false;
				this.BoardMonitor.synchronize = true;
			}
			else {
				this.BoardMonitor = new DrawingBoardMonitor(image);
				this.BoardMonitor.setTitle("Board Monitor");
				this.BoardMonitor.setVisible(true);
				
				this.BoardMonitor.openTriger = false;
				this.BoardMonitor.synchronize = true;
			}
		
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}