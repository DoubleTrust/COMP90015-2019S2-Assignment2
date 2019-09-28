package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Sebastian Yan
 * @date 23/09/2019
 */
public interface RemoteInterface extends Remote{
	
	/** interface to create the white board
	 * @throws RemoteException
	 */
	public boolean createWhiteBoard() throws RemoteException;
	
	/** interface to shut down the white board
	 * @throws RemoteException
	 */
	public void shutDownWhiteBoard() throws RemoteException;
	
	/** interface to open the white board
	 * @throws RemoteException
	 */
	public void openWhiteBoard() throws RemoteException;
	
	/** interface to dispose the white board (for manager only)
	 * @throws RemoteException
	 */
	public void disposeCanvas() throws RemoteException;
	
	/** interface to record username(s) 
	 * @throws RemoteException
	 */
	public void RecordUserInfo(String username) throws RemoteException;
	
	/** interface to remove username(s) 
	 * @throws RemoteException
	 */
	public void RemoveClient(String username) throws RemoteException;
	
	/** interface to return username(s) 
	 * @throws RemoteException
	 */
	public ArrayList<String> getUserInfo() throws RemoteException;


	
}
