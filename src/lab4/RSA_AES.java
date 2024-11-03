package lab4;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;

public class RSA_AES {
    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private SecretKey secretKey;
    private IvParameterSpec iv;
    private String encryptKey;
    private String encryptIv;
    private static final String PUBLIC_PRIVATE_KEY_PATH = "./src/lab4/public_private_key.dat";


    public String encryptBase64(byte[] dataInByte) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(dataInByte));
    }

    public byte[] encrypt(byte[] dataInByte) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] out = cipher.doFinal(dataInByte);
        return out;
    }

    public byte[] decrypt(String base64) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] in = Base64.getDecoder().decode(base64);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] out = cipher.doFinal(in);
        return out;
    }


    public void genKey() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void savePublicPrivateKey() throws Exception {
        try (FileOutputStream fos = new FileOutputStream(PUBLIC_PRIVATE_KEY_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(publicKey);
            oos.writeObject(privateKey);

        }
    }

    public void loadPublicPrivateKey() throws Exception {
        try (FileInputStream fis = new FileInputStream(PUBLIC_PRIVATE_KEY_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            publicKey = (PublicKey) ois.readObject();
            privateKey = (PrivateKey) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
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
    }

    public String encryptKeyAES(SecretKey key) throws Exception {
        return encryptBase64(key.getEncoded());
    }

    public String encryptIvAES(IvParameterSpec iv) throws Exception {
        return encryptBase64(iv.getIV());
    }


    public void decryptKeyIv(String keyBase64, String ivBase64) throws Exception {
        secretKey = new SecretKeySpec(decrypt(keyBase64), "AES");
        iv = new IvParameterSpec(decrypt(ivBase64));
    }


    public void saveKeyAndIv(String path) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(path);
             DataOutputStream dos = new DataOutputStream(fos)) {

            dos.writeUTF(encryptKey);
            dos.writeUTF(encryptIv);

        }
    }

    public void loadKeyAndIv(String path) throws Exception {
        try (FileInputStream fis = new FileInputStream(path);
             DataInputStream dis = new DataInputStream(fis)) {
            encryptKey = dis.readUTF();
            encryptIv = dis.readUTF();
            decryptKeyIv(encryptKey, encryptIv);
        }
    }


    public boolean encryptFileWithAESAndRSA(String src, String des) throws Exception {
        genKey();
        savePublicPrivateKey();
        generateKeyAndIv();
        encryptKey = encryptKeyAES(secretKey);
        encryptIv = encryptIvAES(iv);


        try (FileOutputStream fos = new FileOutputStream(des);
             DataOutputStream dos = new DataOutputStream(fos)) {

            dos.writeUTF(encryptKey);
            dos.writeUTF(encryptIv);

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
                 CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                }
            }
        }

        publicKey = null;
        privateKey = null;
        secretKey = null;
        iv = null;
        encryptKey = null;
        encryptIv = null;

        return true;
    }

    public boolean decryptFileWithAESAndRSA(String src, String des) throws Exception {
        loadPublicPrivateKey();
        loadKeyAndIv(src);
        decryptKeyIv(encryptKey, encryptIv);

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
             CipherInputStream cis = new CipherInputStream(bis, cipher)) {

            bis.skip(encryptKey.length() + encryptIv.length() + 4);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
        return true;
    }


    public static void main(String[] args) throws Exception {
        RSA_AES rsa = new RSA_AES();
        String file = "./src/lab4/123.txt";
        String encryptFile = "./src/lab4/encryptFile.txt";
        String decryptFile = "./src/lab4/decryptFile.txt";
        rsa.encryptFileWithAESAndRSA(file, encryptFile);
        rsa.decryptFileWithAESAndRSA(encryptFile, decryptFile);
    }
}
