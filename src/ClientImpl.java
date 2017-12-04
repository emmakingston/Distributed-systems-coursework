import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Random;

public class ClientImpl implements ClientInterface {

	private BigInteger key;
	private BigInteger y;
	
	@Override
	public void getInitialServVal(BigInteger[] values) throws RemoteException {
		BigInteger x = values[0];
		BigInteger p = values[1];
		BigInteger g = values[2];
		
		BigInteger b = new BigInteger(5, new Random());
		y = g.modPow(b, p);
		
		key = x.modPow(b, p);
		System.out.println("Shared key: " +key);
		
	}
	
	
	public BigInteger getClientY() {
		return y;
	}
	

}
