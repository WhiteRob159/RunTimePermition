package com.runtimepermition.aes;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static java.sql.DriverManager.println;

public class JavaAES1 {

    private static final String TAG = JavaAES1.class.getSimpleName();

    public static void main(String[] args) throws IOException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ClassNotFoundException {

        String key = "e8ffc7e56311679f12b6fc91aa77a5eb";

        byte[] ivBytes = new byte[16];
//        byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        byte[] keyBytes = key.getBytes("UTF-8");

        String text = "Hello";


        final byte[] encrypt = encrypt(ivBytes, keyBytes, text.getBytes("UTF-8"));
        final byte[] decrypt = decrypt(ivBytes, keyBytes, encrypt);

        System.out.println(" " + encrypt);
        System.out.println(" " + decrypt);

        String encoded = Base64.encodeToString(encrypt, Base64.NO_WRAP);
        System.out.println(" " + encoded);
        String decoded = new String(Base64.decode(decrypt, Base64.NO_WRAP));
        System.out.println(" " + decoded);


    }

    public static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] mes)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException, IOException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(mes);

    }

    public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException, IOException, ClassNotFoundException {

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);

    }


}
