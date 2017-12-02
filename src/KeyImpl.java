import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Random;

//must implement all methods of interface

public class KeyImpl implements KeyInterface {

	private BigInteger p;
	private BigInteger g;
	private BigInteger a;
	private BigInteger x;
	private BigInteger key;
	
	public KeyImpl(BigInteger servP, BigInteger servG) throws RemoteException {
		this.p = servP;
		this.g= servG;
	}
	
	public KeyImpl() throws RemoteException {
		p = new BigInteger("191");
		g = new BigInteger("131");
	}

	@Override
	public void calculateServKey() throws RemoteException {
		setA();
		x = g.modPow(a, p);	
		
	}
	
	public void setA() {
		Random rand = new Random();
		a = new BigInteger(5,rand);
	}
	
	public BigInteger receiveX() throws RemoteException {
		return x;
	}
	
	public BigInteger receiveP() throws RemoteException {
		return p;
	}
	
	public BigInteger receiveG() throws RemoteException {
		return g;
	}

	@Override
	public void sendY(BigInteger clientY) throws RemoteException {
		key = clientY.modPow(a, p);
		System.out.println("Shared key: " +key);
		
	}
	
}