package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Demo class RemoteInterface
 * @author Sebastian Yan
 * @date 20/09/2019
 */
public interface RemoteInterface extends Remote{
	
	public void testRemoteFunction() throws RemoteException;
	
}
