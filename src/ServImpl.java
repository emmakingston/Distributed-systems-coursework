import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//must implement all methods of interface

public class ServImpl implements ServInterface {
	
	private BigInteger p;
	private BigInteger g;
	private BigInteger a;
	private CiphertextInterface ctInterface;
	private Map<Remote,BigInteger> clients = new HashMap<Remote,BigInteger>();
	
	public ServImpl(BigInteger p, BigInteger g) {
		this.p = p;
		this.g = g;		
		
	}

	@Override
	public synchronized void getConnectionRequest(ClientInterface client) throws RemoteException {
		a = new BigInteger(5, new Random());
		BigInteger x = g.modPow(a, p);

		ClientInterface currentClient = client;
		
		currentClient.getInitialServVal(new BigInteger[]{x,p,g});		
		BigInteger y = client.getClientY();
		BigInteger key = y.modPow(a, p);
		
		clients.put(client, key);
		
		System.out.println("Shared key: " + key);
		
		callBackToClients();

	}
	
	public void setCiphertextInterface(CiphertextInterface ctInter) throws RemoteException {
		ctInterface = ctInter;
		
	}
	
	public synchronized void callBackToClients() throws RemoteException{
		
		ClientInterface c;
		
		String uid;
		int key;
		String encrypted;

		try {
			for(Remote r:clients.keySet()) {
				c = (ClientInterface) r;
				key = clients.get(c).intValue();
				uid = c.getUsername();
				encrypted = ctInterface.get(uid, key);
				clients.remove(r);

				c.sendAndDecrypt(encrypted);
				//c.closeClient();		
			}

		} catch (Exception e) {
			System.err.println("Error: failed to complete callback to remote object");
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
	}
	
	
}