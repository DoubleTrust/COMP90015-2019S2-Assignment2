package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Chaoxian Zhou, Yangyang Long, Jiuzhou Han, Wentao Yan
 * @date 07/10/2019
 */
public interface RemoteInterface extends Remote{
	
	/** interface to create the white board (for manager only)
	 * @throws RemoteException
	 */
	public void createWhiteBoard() throws RemoteException;
	
	/** interface to close manager's white board 
	 * @throws RemoteException
	 */
	public void closeManagerBoard() throws RemoteException;
	
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
	
	/** interface to remove username and corresponding white board
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
