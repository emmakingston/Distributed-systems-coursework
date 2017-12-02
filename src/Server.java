import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends CommImplementation{
	
	private BigInteger p;
	private BigInteger g;
	private BigInteger a;

	public static void main(String[] args) {
		
		try {
			CommImplementation server = new CommImplementation();
			RemInterface stub = (RemInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.getRegistry();
			
			registry.rebind("Key", server);
			
		} catch (Exception e) {
			System.err.println("Server error: " + e.getMessage());
		}
		
	}
	
}