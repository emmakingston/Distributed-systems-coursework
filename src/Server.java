import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends CommImplementation{
	
	private static BigInteger p = new BigInteger("191");
	private static BigInteger g = new BigInteger("131");

	public static void main(String[] args) {
		
		try {
			//KeyImpl server = new KeyImpl(p,g);
			//KeyImpl server = new KeyImpl(new BigInteger("191"),new BigInteger("131"));
			KeyImpl server = new KeyImpl();
			KeyInterface stub = (KeyInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.getRegistry();
			
			registry.rebind("Key", server);
			
		} catch (Exception e) {
			System.err.println("Server error: " + e.getMessage());
		}
		
	}
	
}