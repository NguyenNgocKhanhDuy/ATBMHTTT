package chat_tcp;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Client {
	SecretKey secretKey;
	IvParameterSpec iv;

	public Client() throws Exception {
		Socket soc = new Socket("127.0.0.1", 12345);
		loadKeyAndIV();

		Cipher cipherIn = Cipher.getInstance("AES/CTR/NoPadding");
		cipherIn.init(Cipher.DECRYPT_MODE, secretKey, iv);

		Cipher cipherOut = Cipher.getInstance("AES/CTR/NoPadding");
		cipherOut.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		BufferedReader br = new BufferedReader(
				new InputStreamReader(new CipherInputStream(soc.getInputStream(), cipherIn)));
		PrintWriter pw = new PrintWriter(new CipherOutputStream(soc.getOutputStream(), cipherOut));

		Scanner scanner = new Scanner(System.in);
		String line;
		while (true) {
			line = scanner.nextLine();
			pw.println(line);
			pw.flush();

			String response = br.readLine();
			System.out.println(response);
			if (".".equalsIgnoreCase(response)) {
				System.out.println("Client Thoat");
				break;
			}

		}

	}

	private void loadKeyAndIV() {
		try (FileInputStream fis = new FileInputStream("./src/chat_tcp/key_iv.dat");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			secretKey = (SecretKey) ois.readObject();
			String ivBase64 = (String) ois.readObject();
			iv = new IvParameterSpec(Base64.getDecoder().decode(ivBase64));
			System.out.println("Secret key and IV loaded from key_iv.dat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		new Client();
	}

}
