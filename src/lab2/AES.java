package lab2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AES {
    SecretKey key;

    public SecretKey genKey() throws NoSuchAlgorithmException {
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        keygenerator.init(128);
        key = keygenerator.generateKey();
        return key;
    }

    public void loadKey(SecretKey key) {
        this.key = key;
    }

    public byte[] encrypt(String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Sử dụng padding
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        return cipher.doFinal(data);
    }

    public String decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        byte[] bytes = cipher.doFinal(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public boolean encryptFile(String src, String des) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
        cipher.init(Cipher.ENCRYPT_MODE, this.key);

        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(des));
             CipherOutputStream cipherOut = new CipherOutputStream(out, cipher)) {

            byte[] read = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(read)) != -1) {
                cipherOut.write(read, 0, bytesRead); // Ghi đúng số byte đã đọc
            }
        }
        return true;
    }

    public boolean decryptFile(String src, String des) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Sử dụng padding
        cipher.init(Cipher.DECRYPT_MODE, this.key);

        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(des));
             CipherInputStream cipherIn = new CipherInputStream(input, cipher)) {

            byte[] read = new byte[1024];
            int bytesRead;
            while ((bytesRead = cipherIn.read(read)) != -1) {
                out.write(read, 0, bytesRead); // Ghi đúng số byte đã đọc
            }
        }
        return true;
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
        String src = "./src/lab2/image.jpg";
        String desf = "./src/lab2/b.jpg";
        String f = "./src/lab2/f.jpg";
        AES des = new AES();
        des.genKey(); // Tạo khóa
        des.encryptFile(src, desf); // Mã hóa tệp
        des.decryptFile(desf, f); // Giải mã tệp
    }
}
