import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class MyClient {
	
	public static void main(String[] args) {
		
		String host;
		String user;
		
		if(args.length <1) {
			System.err.println("No host specified");
			return;
		}
		
		host = args[0];
		
		try {
			
			System.setProperty("java.securitypolicy", "mypolicy");
			if(System.getSecurityManager()==null) {
				System.setSecurityManager(new SecurityManager());
			}
			
			ClientImpl client = new ClientImpl();
			ClientInterface clientStub = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);
			
			Registry registry = LocateRegistry.getRegistry(host);
			ServInterface server = (ServInterface) registry.lookup("Server");
			
			server.getConnectionRequest(clientStub);
			
		} catch (Exception e) {
			System.err.println("Client error: " + e.getMessage());
		}
	}
	
}