import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

//contains all methods that can be invoked by the client
//all methods must throw RemoteException

public interface RemInterface extends Remote{
	
	public void calculateServKey() throws RemoteException;
	
	public BigInteger receiveX() throws RemoteException;
	public BigInteger receiveP() throws RemoteException;
	public BigInteger receiveG() throws RemoteException;
	
	public void sendY(BigInteger clientY) throws RemoteException;
}