package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote{
	
	public void testRemoteFunction() throws RemoteException;
	
}
