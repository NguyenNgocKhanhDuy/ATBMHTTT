package lab6;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class DS {
    KeyPair keyPair;
    SecureRandom secureRandom;
    Signature signature;
    PublicKey publicKey;
    PrivateKey privateKey;

    public DS(String alg, String algRandom, String prov) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(alg,prov);
        secureRandom = SecureRandom.getInstance(algRandom, prov);
        generator.initialize(1024, secureRandom);
        keyPair = generator.genKeyPair();
        signature = Signature.getInstance(alg, prov);
    }

    public boolean genKey() {
        if (keyPair == null) return  false;
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        return true;
    }

    public String sign(String mes) throws InvalidKeyException, SignatureException {
        byte[] data = mes.getBytes();
        signature.initSign(privateKey);
        signature.update(data);
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public String signFile(String src) throws InvalidKeyException, SignatureException, IOException {
        byte[] data = src.getBytes();
        signature.initSign(privateKey);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        byte[] buff = new byte[1024];
        int read;
        while ((read = bis.read(buff)) != -1) {
            signature.update(buff, 0, read);
        }
        bis.close();
        byte[] sign = signature.sign();
        return Base64.getEncoder().encodeToString(sign);
    }

    public boolean verify(String mes, String sign) throws InvalidKeyException, SignatureException {
        signature.initVerify(publicKey);
        byte[] data = mes.getBytes();
        byte[] signValue = Base64.getDecoder().decode(sign);
        signature.update(data);
        return signature.verify(signValue);
    }

    public boolean verifyFile(String src, String sign) throws InvalidKeyException, SignatureException, IOException {
        signature.initVerify(publicKey);
        byte[] data = src.getBytes();
        byte[] signValue = Base64.getDecoder().decode(sign);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        byte[] buff = new byte[1024];
        int read;
        while ((read = bis.read(buff)) != -1) {
            signature.update(buff, 0, read);
        }
        bis.close();
        return signature.verify(signValue);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException, IOException {
        DS ds = new DS("DSA", "SHA1PRNG", "SUN");
        ds.genKey();
        String sign = ds.signFile("C:\\CentOS7\\CentOS7\\CentOS 7 64-bit-4d7fe2c2.vmem");
        System.out.println(sign);
        System.out.println("Kiem tra chu ky "+ (ds.verifyFile("C:\\CentOS7\\CentOS7\\CentOS 7 64-bit-4d7fe2c2-1.vmem", sign)?"":"khong ")+"hop le");
    }
}
