package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Sebastian Yan
 * @date 23/09/2019
 */
public interface RemoteInterface extends Remote{
	

	/** interface to open Canvas
	 * @throws RemoteException
	 */
	public void openCanvas() throws RemoteException;
	
	/** interface to dispose Canvas (for manager only)
	 * @throws RemoteException
	 */
	public void disposeCanvas() throws RemoteException;


	
}
