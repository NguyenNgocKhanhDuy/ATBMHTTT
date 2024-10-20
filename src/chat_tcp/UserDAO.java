package chat_tcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class UserDAO {
	HashMap<String, String> accounts;
	
	public UserDAO() throws FileNotFoundException, IOException {
		loadAccounts();
	}

	public boolean checkUname(String uname) {
		return accounts.containsKey(uname);
	}

	public boolean login(String uname, String pass) {
		return accounts.get(uname).equals(pass);
	}
	
	public void register(String uname, String pass) throws FileNotFoundException, IOException {
		accounts.put(uname, pass);
		saveAccounts();
	}

	public void saveAccounts() throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream("./src/chat_tcp/accounts.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(accounts);
		}
	}

	public void loadAccounts() throws FileNotFoundException, IOException {
		File file = new File("./src/chat_tcp/accounts.dat");
		if (!file.exists()) {
			saveAccounts();	
		}
		
		try (FileInputStream fis = new FileInputStream("./src/chat_tcp/accounts.dat");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			accounts = (HashMap<String, String>) ois.readObject();
			if (accounts == null) {
				accounts = new HashMap<>();
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

}
