import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Random;

public class ClientImpl implements ClientInterface {

	private BigInteger key;
	private BigInteger y;
	private String username;
	
	public ClientImpl(String uid) {
		username = uid;
	}
	
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
	
	
	public BigInteger getClientY() throws RemoteException {
		return y;
	}

	@Override
	public String getUsername() throws RemoteException {
		return username;
	}

	@Override
	public void sendAndDecrypt(String encrypted) throws RemoteException {
		String encryptedTxt = encrypted;
		
		for(int i = 0; i<2; i++) {
			encryptedTxt = reverseSubst(encryptedTxt);
		}
		
		for(int i = 0; i<2; i++) {
			encryptedTxt = reverseTransp(encryptedTxt);
		}
		
		System.out.println(encryptedTxt);
	}
	
	public String reverseSubst(String encrypted) {
		String msg = encrypted;
		StringBuilder result = new StringBuilder();
		int shift = key.intValue() % 26;

		String alpha="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		for(char c: msg.toCharArray()) {
			int newChar= alpha.indexOf(c)  - shift;
			
			if(newChar < 0) {
				newChar = 26 + newChar;
			}
			
			char replace = alpha.charAt(newChar); 
			result.append(replace);
		}
		return result.toString();
	}
	
	public String reverseTransp(String encrypted) {
		int blocks = encrypted.length()/8;
		int rotate = key.intValue() % 8;
		
		String[] msg = new String[blocks];
		String[] result = new String[blocks];
		
		StringBuilder decrypted = new StringBuilder();
		
		for(int i = 0; (i+1)*8 <= encrypted.length(); i++) {
			msg[i] = encrypted.substring(i*8, ((1+i)*8));
		}
		

		for(String block:msg) {
			
			char[] oldblock = block.toCharArray();
			char[] newblock = new char[8];
			
			/*for each character add it into newmsg at i-shift*/
			for(int i=0; i<8;i++) {
				int charPos = i - rotate;
				if(charPos<0) {
					charPos = 8 + charPos; 
				}
				
				newblock[charPos] = oldblock[i];
				
			}
			decrypted.append(newblock);
		}
		
		return decrypted.toString();
	}
	

}
