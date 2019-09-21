package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;

import server.DrawPictureFRame;
import remote.RemoteInterface;

/**
 * @author Sebastian Yan
 * @date 21/09/2019
 */
public class RemoteImplementation extends UnicastRemoteObject implements RemoteInterface{
	
	/** 
	 * Constructor
	 */
	protected RemoteImplementation() throws RemoteException {

	}
	
	@Override
	public void testRemoteFunction() throws RemoteException{
		System.out.println("remote function executed.");
	}
	
	
	@Override
	public void openCanvas() throws RemoteException{
		DrawPictureFRame pic1= new DrawPictureFRame();
		pic1.setVisible(true);
	}

	

	
	

}
