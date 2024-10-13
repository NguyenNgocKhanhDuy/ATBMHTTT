package lab;

public class Hill {
	static int m = 26;

	public static int getCharIndex(char ch) {
		char base = Character.isUpperCase(ch) ? 'A' : 'a';
		int x = ch - base;
		return x;
	}

	public static int getCharIndexInAscii(char ch) {
		char base = Character.isUpperCase(ch) ? 'A' : 'a';
		int x = ch + base;
		return x;
	}

	public static String encrypt(String text, int[][] key) {
		String result = "";
		int length = key.length;
		boolean last = false;
		int[] indexChar = new int[length];
		char[] c = new char[length];
		
		
		for (int i = 0; i < text.length(); i += length) {
			int temp = i;
			
			for (int m = 0; m < c.length; m++) {
				c[m] = ' ';
				if(temp < text.length()) {
					c[m] = text.charAt(temp);
				}else {
					last = true;
				}
//				System.out.println("c = "+c[m]);
				
				if (!Character.isLetter(c[m]) && last == false) {
					result += c[m];
					continue;
				}
				
				temp++;
				indexChar[m] = c[m] == ' ' ? 0 : getCharIndex(c[m]);
//				System.out.println("in = "+indexChar[m]);
			}
			

			for (int j = 0; j < key.length; j++) {
				int indexEncypt = 0;
				for (int k = 0; k < key.length; k++) {
//					System.out.println("j = "+j);
//					System.out.println("k = "+k);
//					System.out.println("indexChar = "+indexChar[k]+", key = "+key[k][j]);
					indexEncypt += (indexChar[k] * key[k][j]);
//					System.out.println("en = "+indexEncypt);
				}
//				System.out.println("en = "+indexEncypt%m);
				result += (char) getCharIndexInAscii((char) (indexEncypt % m));
				if (result.length() == text.length()) {
					break;
				}
			}

		}
		return result;
	}
	
	
	

	public static void main(String[] args) {
		int[][] key = { { 11, 8 }, { 3, 7 } };
		String encrypt = encrypt("DHNONGLAM", key);
		System.out.println(encrypt);
		
	}

}
