import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

//contains all methods that can be invoked by the client
//all methods must throw RemoteException

public interface ServInterface extends Remote {
	
	/*Gets a secure connection request from a client, computes value of x
	 * sends values x,p and g to client*/
	public void getConnectionRequest(ClientInterface client) 
			throws RemoteException;
	

	
}