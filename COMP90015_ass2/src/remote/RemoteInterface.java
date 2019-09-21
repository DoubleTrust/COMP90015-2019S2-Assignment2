package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Sebastian Yan
 * @date 21/09/2019
 */
public interface RemoteInterface extends Remote{
	
	/** interface to test remote functions
	 * @throws RemoteException
	 */
	public void testRemoteFunction() throws RemoteException;

	/** interface to open Canvas
	 * @throws RemoteException
	 */
	public void openCanvas() throws RemoteException;
	
}
