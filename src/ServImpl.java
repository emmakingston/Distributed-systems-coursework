import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Random;

//must implement all methods of interface

public class ServImpl implements ServInterface {
	
	private BigInteger p;
	private BigInteger g;
	private BigInteger a;
	private BigInteger key;
	private ClientInterface currentClient;
	private CiphertextInterface ctInterface;
	private HashMap<Remote,BigInteger> clients = new HashMap<Remote,BigInteger>();
	
	public ServImpl(BigInteger p, BigInteger g) {
		this.p = p;
		this.g = g;		
		
	}

	@Override
	public synchronized void getConnectionRequest(ClientInterface client) throws RemoteException {
		a = new BigInteger(5, new Random());
		BigInteger x = g.modPow(a, p);

		currentClient = client;
		
		currentClient.getInitialServVal(new BigInteger[]{x,p,g});		
		BigInteger y = client.getClientY();
		key = y.modPow(a, p);
		
		System.out.println("Shared key: " + key);
		
		getCipher();

	}
	
	public void setCiphertextInterface(CiphertextInterface ctInter) throws RemoteException {
		ctInterface = ctInter;
		
	}
	
	public void getCipher() throws RemoteException{
		
		String uid = currentClient.getUsername();
		String encrypted = ctInterface.get(uid, key.intValue());
		
		//System.out.println(encrypted);
		
		currentClient.sendAndDecrypt(encrypted);
	}
	
	
}