import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

	/* gets value of x p and g from server after making connection request */
	public void getInitialServVal(BigInteger[] values) 
			throws RemoteException;
	
	/*returns the calculated value of y to the server */
	public BigInteger getClientY() 
			throws RemoteException;
}
