package lab;

import java.util.ArrayList;
import java.util.List;

public class Vigenere {

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

	public static List<Integer> genKey(String key) {
		List<Integer> keyIndex = new ArrayList<>();
		for (int i = 0; i < key.length(); i++) {
			keyIndex.add(getIndexChar(key.charAt(i)));
		}
		return keyIndex;
	}

	public static String encrypt(String text, String key) {
		String result = "";

		List<Integer> keysIndex = genKey(key);

		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (!Character.isLetter(c)) {
				result += c;
				continue;
			}

			int indexEncrypt = (getIndexChar(c) + keysIndex.get(j)) % m;

			result += (char) getIndexCharInAscii((char) indexEncrypt);

			j++;

			if (j == keysIndex.size()) {
				j = 0;
			}
		}

		return result;
	}

	public static String decrypt(String ciphertext, String key) {
		String result = "";

		List<Integer> keysIndex = genKey(key);

		for (int i = 0, j = 0; i < ciphertext.length(); i++) {
			char c = ciphertext.charAt(i);

			if (!Character.isLetter(c)) {
				result += c;
				continue;
			}

			int indexDecrypt = (getIndexChar(c) - keysIndex.get(j) + m) % m;

			result += (char) getIndexCharInAscii((char) indexDecrypt);

			j++;

			if (j == keysIndex.size()) {
				j = 0;
			}
		}

		return result;
	}

	public static void main(String[] args) {
		String key = "CIPHER";
		String encrypt = encrypt("thiscryptosystemisnotsecure", key);
		System.out.println(encrypt);
		System.out.println(decrypt(encrypt, key));
	}
}
