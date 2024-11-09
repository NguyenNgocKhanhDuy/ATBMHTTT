package lab5;

import java.io.*;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = data.getBytes();
        byte[] digest = md.digest(bytes);
        BigInteger re = new BigInteger(1, digest);
        return re.toString(16);
    }

    public String hashFile(String src) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        File f = new File(src);
        if (!f.exists()) return null;

        InputStream in = new BufferedInputStream(new FileInputStream(f));
        DigestInputStream di = new DigestInputStream(in, md);

        byte[] buff = new byte[1024];
        int read;
        do {
            read = di.read(buff);
        }while (read != -1);

        BigInteger re = new BigInteger(1, di.getMessageDigest().digest());
        return re.toString(16);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        MD5 md5 = new MD5();
        System.out.println(md5.hash("hello world"));
        System.out.println(md5.hashFile("./src/lab5/image.jpg"));
    }
}
