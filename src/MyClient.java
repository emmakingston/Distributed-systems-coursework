import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class MyClient {
	
	public static void main(String[] args) {
		
		String host;
		String user;
		
		if(args.length <2) {
			System.err.println("Host and/or user not specified");
			return;
		}
		
		host = args[0];
		user = args[1];
		
		try {
			
			System.setProperty("java.security.policy", "mypolicy");
			if(System.getSecurityManager()==null) {
				System.setSecurityManager(new SecurityManager());
			}
			
			ClientImpl client = new ClientImpl(user);
			ClientInterface clientStub = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
			
			Registry registry = LocateRegistry.getRegistry(host);
			ServInterface server = (ServInterface) registry.lookup("Server");
			
			server.getConnectionRequest(clientStub);
			
		} catch (Exception e) {
			System.err.println("Client error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}