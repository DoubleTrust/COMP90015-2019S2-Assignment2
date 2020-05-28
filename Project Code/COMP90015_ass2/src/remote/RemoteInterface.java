package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

	/**
	 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
	 * @date 19/10/2019
	 */
	public interface RemoteInterface extends Remote{
		
	/** interface to create the white board (for manager only)
	 * @throws RemoteException
	 */
	public void createWhiteBoard() throws RemoteException;
	
	/** interface to get the status of the white board 
	 * @throws RemoteException
	 */
	public byte[] getBoardStatus() throws RemoteException;
	
	/** interface to set the status of the white board 
	 * @throws RemoteException
	 */
	public void updateBoardStatus(byte[] imageBytes) throws RemoteException;
	
	/** interface to close manager's white board 
	 * @throws RemoteException
	 */
	public void closeManagerBoard() throws RemoteException;
	
	/** interface to join the white board (for ordinary client only)
	 * @throws RemoteException
	 */
	public byte[] joinWhiteBoard() throws RemoteException;
	
	/** interface to open the white board
	 * @throws RemoteException
	 */
	public void openWhiteBoard(byte[] imageBytes) throws RemoteException;
	
	/** interface to record username(s) 
	 * @throws RemoteException
	 */
	public void uploadUserInfo(String username) throws RemoteException;
	
	/** interface to remove username and corresponding white board
	 * @throws RemoteException
	 */
	public void RemoveUser(String username) throws RemoteException;
	
	/** interface to remove all clients' info
	 * @throws RemoteException
	 */
	public void removeAllInfo() throws RemoteException;
	
	/** interface to return username(s) 
	 * @throws RemoteException
	 */
	public ArrayList<String> getUserInfo() throws RemoteException;
	
	/** interface to set kick username
	 * @throws RemoteException
	 */
	public void setKickUsername(String kickname) throws RemoteException;
	
	/** interface to kick a user
	 * @throws RemoteException
	 */
	public String KickUser() throws RemoteException;

	/** interface to return whether the manager tries to open a white board
	 * @throws RemoteException
	 */
	public boolean canSynchronize() throws RemoteException;
	
	/** 
	 * interface to change the synchronization status when the manager tries to open a white board
	 */
	public void changeSynchronization(boolean bool) throws RemoteException;
	
	/** 
	 * interface to clear the content of a white board
	 */
	public boolean clearContent() throws RemoteException;

	// ----------------------------------------------------------------------------
	/** interface to record dialogue 
	 * @throws RemoteException
	 */
	public void updateDialogue(String dialogue, String username) throws RemoteException;
		
	/** interface to return dialogue
	 * @throws RemoteException
	 */
	public ArrayList<String> getDialogue() throws RemoteException;
	// ----------------------------------------------------------------------------
	
	/** interface to get() and set() the parameter of allow and require
	 * the following 4 functions 
	 * @throws RemoteException
	 */
	public int getAllow() throws RemoteException;
	public int getRequire() throws RemoteException;
	public void setAllow(int allow) throws RemoteException;
	public void setRequire(int require)throws RemoteException;	
	
    /** client uses this interface to change the value of require to 1
	 * @throws RemoteException
	 */
	public void AllowClient()throws RemoteException;
	/**
	 * interface to return the value of allow to the client
	 * @throws RemoteException
	 */
	public int returnAllow() throws RemoteException;
}
