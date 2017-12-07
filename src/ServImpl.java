import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*The class implements the remote server interface (ServInterface)
 * 
 * This class establishes a connection with clients and calculates the shared key
 *  
 * Gets and sends ciphertext to clients to decrypt*/

public class ServImpl implements ServInterface {
	
	private BigInteger p;							//server prime number
	private BigInteger g;							//server primitive root
	private CiphertextInterface ctInterface;		//interface to external server
	private Map<Remote,BigInteger> clients = new HashMap<Remote,BigInteger>();	//stores each client with shared key
	
	/*constructs new instance of remote server and sets prime and primitive root*/
	public ServImpl(BigInteger p, BigInteger g) {
		this.p = p;
		this.g = g;		
		
	}

	/*Gets a connection request from a remote client
	 *Calculates the shared key and adds client to list of current clients
	 *Calls the callback method to send encrypted text to the client 
	 *Synchronized as a and key  must be specific to each client, can't interrupt*/
	@Override
	public synchronized void getConnectionRequest(ClientInterface client) throws RemoteException {
		
		ClientInterface currentClient = client;
		
		//generates pseudo random a and uses this to calculate x
		BigInteger a = new BigInteger(5, new Random());
		BigInteger x = g.modPow(a, p);
		
		//sends the x value, prime and primitive root of server to client
		currentClient.getInitialServVal(new BigInteger[]{x,p,g});	
		
		//gets client y and calculates the shared key
		BigInteger y = client.getClientY();
		BigInteger key = y.modPow(a, p);
		
		//adds the client and key to the hashmap of clients
		clients.put(client, key);
		
		//calls back to clients with the encrypted text
		callBackToClients();

	}
	
	/*Sets the interface to the remote ciphertext interface to get encrypted text*/
	public void setCiphertextInterface(CiphertextInterface ctInter) throws RemoteException {
		ctInterface = ctInter;
		
	}
	
	/*Gets the encrypted text using the client username and key
	 * Sends the encrypted text back to the client to be decrypted
	 * Removes reference to the client*/
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
			}

		} catch (Exception e) {
			System.err.println("Error: failed to complete callback to remote object");
			System.out.println(e.getMessage());
		}
		
	}
	
	
}