import java.math.BigInteger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class MyClient {
	
	public static void main(String[] args) {
		
		String host;
		BigInteger y;
		BigInteger b;	
		BigInteger p;
		BigInteger g;
		BigInteger x;
		BigInteger key;
		
		if(args.length>0) {
			host = args[0];
		} else {
			System.err.println("No host specified");
			return;
		}
		
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			KeyInterface stub = (KeyInterface) registry.lookup("Key");
			
			stub.calculateServKey();
			
			p = stub.receiveP();
			x = stub.receiveX();
			g = stub.receiveG();
			
			//generate psuedo random b
			Random rand = new Random();
			b = new BigInteger(5,rand);
			
			y = g.modPow(b, p);
			
			stub.sendY(y);
			
			key = x.modPow(b, p);
			System.out.println("Shared key: " + key);
			
			
		} catch (Exception e) {
			System.err.println("Client error: " + e.getMessage());
		}
	}
	
}