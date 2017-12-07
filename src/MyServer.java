import java.math.BigInteger;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/*The class generates and registers an instance of the server to receive remote calls from the client
 * 
 *  The server also downloads a stub at runtime from an external server and passes to the instance*/

public class MyServer{

	//server prime and primitive roots
	private static BigInteger p = new BigInteger("191");
	private static BigInteger g = new BigInteger("131");

	public static void main(String[] args) {
		
		try {
			
			//sets the security policy if one isnt already specified
			System.setProperty("java.security.policy", "mypolicy");
			if(System.getSecurityManager()==null) {
				System.setSecurityManager(new SecurityManager());
			}
			
			//creates a new remote server object, instantiates it with server p and g values
			//exports the object (so can receive remote calls) and binds on registry so it can be found
			ServImpl server = new ServImpl(p,g);
			ServInterface stub = (ServInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.getRegistry();			
			registry.rebind("Server", stub);

			//dealing with external server server
			//downloads stub at runtime from specified location
		    Registry ctreg = LocateRegistry.getRegistry( "svm-tjn1f15-comp2207.ecs.soton.ac.uk",12345 );
		    CiphertextInterface ctstub = (CiphertextInterface) ctreg.lookup( "CiphertextProvider" );
		    
		    //sends the object reference to the ciphertext interface so server instance can interact with it
		    server.setCiphertextInterface(ctstub);
			
			
		} catch (Exception e) {
			System.err.println("Server error: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}