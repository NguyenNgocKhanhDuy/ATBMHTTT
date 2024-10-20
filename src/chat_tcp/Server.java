package chat_tcp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Server {
	SecretKey secretKey;
	IvParameterSpec iv;
	String uname;
	String pass;

	UserDAO uDAO = new UserDAO();

	public Server() throws Exception {
		ServerSocket serverSocket = new ServerSocket(12345);
		System.out.println("Chờ kết nối...");
		generateKeyAndIv();

		while (true) {
			Socket finalSoc = serverSocket.accept();
			new Thread(() -> {
				try {
					System.out.println("Kết Nối Thành Công.");

					Cipher cipherIn = Cipher.getInstance("AES/CTR/NoPadding");
					cipherIn.init(Cipher.DECRYPT_MODE, secretKey, iv);

					Cipher cipherOut = Cipher.getInstance("AES/CTR/NoPadding");
					cipherOut.init(Cipher.ENCRYPT_MODE, secretKey, iv);

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new CipherInputStream(finalSoc.getInputStream(), cipherIn)));
					PrintWriter pw = new PrintWriter(new CipherOutputStream(finalSoc.getOutputStream(), cipherOut));

					loginPro(br, pw);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();

		}
	}

	private void loginPro(BufferedReader br, PrintWriter pw) throws IOException {
		String line = "";
		StringTokenizer token;
		System.out.println("User Login");

		while (true) {
			String cm = "";
			line = br.readLine();
			token = new StringTokenizer(line);
			cm = token.nextToken();
			System.out.println(cm);

			if (cm.equalsIgnoreCase("uname")) {
				System.out.println("un");
				String uname = token.nextToken();
//				if (uDAO.checkUname(uname)) {
					pw.println("Ok");
					this.uname = uname;
//				} else {
//					pw.println("Username Khong Ton Tai");
//				}
//				continue;

			} else if (cm.equalsIgnoreCase("pass")) {
				String pass = token.nextToken();
				this.pass = pass;
				pw.println("Ok");
			} else if (cm.equalsIgnoreCase("login")) {
				if (uDAO.checkUname(uname)) {					
					if (uDAO.login(uname, pass)) {
						pw.println(".");
						pw.flush();
						break;
					} else {
						pw.println("Login Khong Thanh Cong");
					}
				}else {
					pw.println("Username Khong Ton Tai");
				}
//				continue;
			}else if (cm.equalsIgnoreCase("register")) {
				uDAO.register(uname, pass);
				pw.println("Dang Ky Thanh Cong");
			} else if (cm.equalsIgnoreCase("logout")) {
				uname = "";
				pass = "";
				pw.println("Logout Thanh Cong");
			}else {
				pw.println("Loi Cu Phap");
				System.out.println("Loi");
			}
			
			pw.flush();
		}

	}
	
	private void generateKeyAndIv() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		secretKey = keyGen.generateKey();

		byte[] ivBytes = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(ivBytes);
		iv = new IvParameterSpec(ivBytes);

		try (FileOutputStream fos = new FileOutputStream("./src/chat_tcp/key_iv.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(secretKey);
			oos.writeObject(Base64.getEncoder().encodeToString(iv.getIV()));
		}

		System.out.println("Secret key and IV saved to key_iv.dat");

	}
	
	public static void main(String[] args) throws Exception {
		new Server();
	}
}
