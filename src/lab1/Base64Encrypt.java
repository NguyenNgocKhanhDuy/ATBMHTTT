package lab1;

import java.io.*;
import java.util.Base64;

public class Base64Encrypt {
    private static final String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    private static final String KEY_PATH = "./src/th01/key.txt";
    private static final String ENCRYPT_PATH = "./src/th01/encrypt.txt";
    private static final String DECRYPT_PATH = "./src/th01/decrypt.txt";

    public static String fileToBase64(String path) {
        try {
            File file = new File(path);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] data = new byte[(int) file.length()];
            bis.read(data);
            bis.close();
            return Base64.getEncoder().encodeToString(data);
        } catch (FileNotFoundException e) {
            return "File not Found";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void genKey() {
        char[] base64Char = BASE64_CHARS.toCharArray();
        String result = "";
        boolean[] hasChar = new boolean[base64Char.length];
        for (int i = 0; i < base64Char.length; i++) {
            int index = (int) (Math.random() * base64Char.length);
            while (hasChar[index]) {
                index = (int) (Math.random() * base64Char.length);
            }
            hasChar[index] = true;
            result += base64Char[index];
        }
        saveFile(result, KEY_PATH);
    }

    public static void saveFile(String content, String path) {
        try {
            File file = new File(path);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadKey() {
        try {
            File file = new File(KEY_PATH);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] data = new byte[(int) file.length()];
            bis.read(data);
            bis.close();
            return new String(data);
        } catch (FileNotFoundException e) {
            genKey();
            return loadKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void encrypt(String path) {
        String base64 = fileToBase64(path);
        String key = loadKey();

        String result = "";
        if (key.equals("File not found")){
            System.out.println("File not found");
        }else {
            for (int i = 0; i < base64.length(); i++) {
                int index = BASE64_CHARS.indexOf(base64.charAt(i));
                char characterEncrypt = key.charAt(index);
                result += characterEncrypt;
            }
            System.out.println("Success encrypt");
            saveFile(result, ENCRYPT_PATH);
        }
    }
    public static void decrypt() {
        try {
            File file = new File(ENCRYPT_PATH);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] data = new byte[(int) file.length()];
            bis.read(data);
            bis.close();

            String encrypt = new String(data);
            String key = loadKey();
            String result = "";

            for (int i = 0; i < encrypt.length(); i++) {
                int index = key.indexOf(encrypt.charAt(i));
                char characterDecrypt = BASE64_CHARS.charAt(index);
                result += characterDecrypt;
            }
            System.out.println("Success decrypt");
            saveFile(result, DECRYPT_PATH);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void base64ToFile(String fileBase64, String path) {
        try {
            File file = new File(fileBase64);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] data = new byte[(int) file.length()];
            bis.read(data);
            bis.close();

            byte[] decodedData = Base64.getDecoder().decode(data);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
            bos.write(decodedData);
            bos.close();
            System.out.println("Complete");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String path = "./src/lab1/image.jpg";
        File file = new File(path);
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        encrypt(path);
        decrypt();
        base64ToFile(DECRYPT_PATH, "./src/lab1/after."+ext);
    }
}
