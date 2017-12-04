import java.math.BigInteger;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MyServer{

	private static BigInteger p = new BigInteger("191");
	private static BigInteger g = new BigInteger("131");

	public static void main(String[] args) {
		
		try {
			
			System.setProperty("java.securitypolicy", "mypolicy");
			if(System.getSecurityManager()==null) {
				System.setSecurityManager(new SecurityManager());
			}
			
			ServImpl server = new ServImpl(p,g);
			ServInterface stub = (ServInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.getRegistry();
			
			registry.rebind("Server", stub);

			
			
			
		} catch (Exception e) {
			System.err.println("Server error: " + e.getMessage());
		}
		
	}
	
}