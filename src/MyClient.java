import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/*The class generates and registers an instance of the client to communicate with the server
 * 
 *Client requests a connection with the server and exits when all communication is finished   */ 

public class MyClient {
	
	public static void main(String[] args) {
		
		String host;	//the host address to register the client on
		String user;	//the username to interact with the external server
		
		//checks all required arguments are present
		if(args.length <2) {
			System.err.println("Host and/or user not specified");
			return;
		}
		
		//sets the host and username using arguments from cmd line
		host = args[0];
		user = args[1];
		
		try {
			
			//checks for a security policy and sets one if none already
			System.setProperty("java.security.policy", "mypolicy");
			if(System.getSecurityManager()==null) {
				System.setSecurityManager(new SecurityManager());
			}
			
			//creates a new remote client object and instantiates it with username value
			//exports the object so it can receive remote calls
			ClientImpl client = new ClientImpl(user);
			ClientInterface clientStub = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
			
			//gets the registry of the host specified in cmd line
			//looks up the instance of server and sends a connection request with the remote client object
			Registry registry = LocateRegistry.getRegistry(host);
			ServInterface server = (ServInterface) registry.lookup("Server");			
			server.getConnectionRequest(clientStub);
			
			//client shuts down after communication with the server
			System.exit(0);
			
		} catch (Exception e) {
			System.err.println("Client error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}