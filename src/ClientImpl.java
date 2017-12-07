import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Random;

/*The class implements the remote client interface (ClientInterface)
 * 
 * The class:
 * 		- calculates the shared key
 * 		- decrypts the encrypted text 	*/
public class ClientImpl implements ClientInterface {

	private BigInteger key;		//shared key with server
	private BigInteger y;		//y value used to calculate shared key
	private String username;	//username for getting encrypted text
	
	/*Constructor gets and sets the user id */
	public ClientImpl(String uid) {
		username = uid;
	}
	
	/*Method calculates the server key after receiving the prime, primitive root and x value from server*/
	@Override
	public void getInitialServVal(BigInteger[] values) throws RemoteException {
		//stores server values
		BigInteger x = values[0];
		BigInteger p = values[1];
		BigInteger g = values[2];
		
		//calculates pseudo random b value and uses to calculate y
		BigInteger b = new BigInteger(5, new Random());
		y = g.modPow(b, p);
		
		//calculates the shared key
		key = x.modPow(b, p);
		
	}
	
	/*Method returns the y value for server to calculate shared key*/
	public BigInteger getClientY() throws RemoteException {
		return y;
	}

	/*Method returns the user ID of user who generated client*/
	@Override
	public String getUsername() throws RemoteException {
		return username;
	}

	/*Decrypts the message by calling two rounds of substitution 
	 * and then two rounds of transposition
	 * outputs the decrypted message */
	@Override
	public void sendAndDecrypt(String encrypted) throws RemoteException {
		
		//stores the encrypted text sent from external server 
		String encryptedTxt = encrypted;
		
		//reverses substitution
		for(int i = 0; i<2; i++) {
			encryptedTxt = reverseSubst(encryptedTxt);
		}
		
		//reverses transposition
		for(int i = 0; i<2; i++) {
			encryptedTxt = reverseTransp(encryptedTxt);
		}
		
		//outputs decrypted text
		System.out.println(encryptedTxt);
	}
	
	/*Method reverse substitution (Caesar cipher) by shifting letters left along the alphabet
	 * calculates the shift using the shared key*/
	public String reverseSubst(String encrypted) {
		
		String msg = encrypted;							//the encrypted text
		StringBuilder result = new StringBuilder();		//the result of this stage of decryption
		int shift = key.intValue() % 26;				//the shift calculated by shift mod 26

		String alpha="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		//for each character in the encrypted text, get the unshifted value
		//add each changed character to the end of the partially decrypted string
		for(char c: msg.toCharArray()) {
			
			//calculates alphabet pos of original character = current pos - shift
			int newChar= alpha.indexOf(c) - shift;
			
			//gets the 'wrap around' if new pos is less than 0
			if(newChar < 0) {
				newChar = 26 + newChar;
			}
			
			//appends the character of alphabet in unshifted position
			result.append(alpha.charAt(newChar));
		}
		
		//return the result of this stage of decryption
		return result.toString();
	}
	
	/*Method to reverse the transposition algorithm
	 * considers each block fof 8 characters and shifts their position,
	 * shift calculated using key value*/
	public String reverseTransp(String encrypted) {
		
		int blocks = encrypted.length()/8;	//gets the number of blocks of 8 characters in msg
		int rotate = key.intValue() % 8;	//gets the value the characters were shifted right within block 
		
		String[] msg = new String[blocks];	//original msg divided into blocks of 8
		
		StringBuilder decrypted = new StringBuilder();	//stores result of this stage of decryption
		
		//method splits every 8 characters and stores in seperate indexes in array
		for(int i = 0; (i+1)*8 <= encrypted.length(); i++) {
			msg[i] = encrypted.substring(i*8, ((1+i)*8));
		}
		
		//for each 8 character block in original messgae, reverse the transposition and append to partially decrypted
		for(String block:msg) {
			
			char[] oldblock = block.toCharArray();	//converts the block into a character array
			char[] newblock = new char[8];			//defines array of decrypted equivalent of block
			
			//for each character in the block work out the position before this round of transposition
			//adds the character to newblock at index of previous position
			for(int i=0; i<8;i++) {
				
				int charPos = i - rotate;	//gets the position of car before transposition
				
				//gets the wrap around if the position is less than 0
				if(charPos<0) {
					charPos = 8 + charPos; 
				}
				
				//puts the character into the new block at index of prev pos
				newblock[charPos] = oldblock[i];				
			}
			
			//adds the transposed block of characters to the final result
			decrypted.append(newblock);
		}
		
		//returns the decrypted message
		return decrypted.toString();
	}
	

}
