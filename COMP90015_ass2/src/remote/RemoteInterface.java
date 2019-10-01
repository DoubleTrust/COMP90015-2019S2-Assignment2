package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Sebastian Yan
 * @date 23/09/2019
 */
public interface RemoteInterface extends Remote{
	
	/** interface to create the white board (for manager only)
	 * @throws RemoteException
	 */
	public void createWhiteBoard() throws RemoteException;
	
	/** interface to close the white boards (for manager only)
	 * @throws RemoteException
	 */
	public void closeWhiteBoard() throws RemoteException;
	
	/** interface to join the white board (for ordinary client only)
	 * @throws RemoteException
	 */
	public boolean joinWhiteBoard(String userName) throws RemoteException;
	
	/** interface to open the white board
	 * @throws RemoteException
	 */
	public void openWhiteBoard(String username) throws RemoteException;
	
	/** interface to record username(s) 
	 * @throws RemoteException
	 */
	public void uploadUserInfo(String username) throws RemoteException;
	
	/** interface to remove username(s) 
	 * @throws RemoteException
	 */
	public void RemoveClient(String username) throws RemoteException;
	
	/** interface to remove all clients' info
	 * @throws RemoteException
	 */
	public void removeAllInfo() throws RemoteException;
	
	/** interface to return username(s) 
	 * @throws RemoteException
	 */
	public ArrayList<String> getUserInfo() throws RemoteException;


	
}
