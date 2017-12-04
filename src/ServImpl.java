import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Random;

//must implement all methods of interface

public class ServImpl implements ServInterface {
	
	private BigInteger p;
	private BigInteger g;
	private BigInteger a;
	private BigInteger key;
	private ClientInterface currentClient;
	
	public ServImpl(BigInteger p, BigInteger g) {
		this.p = p;
		this.g = g;		
		
	}

	@Override
	public void getConnectionRequest(ClientInterface client) throws RemoteException {
		a = new BigInteger(5, new Random());
		BigInteger x = g.modPow(a, p);

		currentClient = client;
		
		client.getInitialServVal(new BigInteger[]{x,p,g});		
		BigInteger y = client.getClientY();
		key = y.modPow(a, p);
		
		System.out.println("Shared key: " + key);

	}
	
}