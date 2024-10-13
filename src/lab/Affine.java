package lab;

public class Affine {
	static int m = 26;
	
	public static int getIndexChar(char ch) {
		char base = Character.isUpperCase(ch) ? 'A' : 'a';
		int x = ch - base;
		return x;
	}

	public static int getIndexCharInAscii(char ch) {
		char base = Character.isUpperCase(ch) ? 'A' : 'a';
		int x = ch + base;
		return x;
	}
	
	public static String encrypt(String plaintext, int a, int b) {
        String result = "";
        
        for (int i = 0; i < plaintext.length(); i++) {
            char ch = plaintext.charAt(i);
            if (Character.isLetter(ch)) {
                int indexChar = getIndexChar(ch);
                char encryptedChar = (char) getIndexCharInAscii((char) (((a * indexChar + b) % m)));
                result += encryptedChar;
            } else {
                result += ch; 
            }
        }
        
        return result;
    }
	
	public static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1; 
    }
	
	 public static String decrypt(String ciphertext, int a, int b) {
	        String result = "";
	        int a_inv = modInverse(a, m);
	        
	        for (int i = 0; i < ciphertext.length(); i++) {
	            char ch = ciphertext.charAt(i);
	            if (Character.isLetter(ch)) {
	                int indexChar = getIndexChar(ch);
	                char decryptedChar = (char) getIndexCharInAscii((char) ((a_inv * (indexChar - b + m)) % m));
	                result += decryptedChar;
	            } else {
	                result += ch; 
	            }
	        }
	        
	        return result;
	    }
	
	public static void main(String[] args) {
		int a = 7;
		int b = 3;
		String encrypt = encrypt("KHOA CONG NGHE THONG TIN", a, b);
		System.out.println(encrypt);
		System.out.println(decrypt(encrypt, a, b));
	}
}
