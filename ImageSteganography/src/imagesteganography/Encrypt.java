/*

 */
package imagesteganography;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Brenden Cho
 */
public class Encrypt {

    SecretKeySpec sKey;

    public Encrypt() {
    }

    public void generateKey(String key) {
        MessageDigest md;
        byte[] temp;

        try {
            temp = key.getBytes("UTF-8");
            md = MessageDigest.getInstance("SHA-1");
            temp = md.digest(temp);
            temp = Arrays.copyOf(temp, 16);
            sKey = new SecretKeySpec(temp, "AES");

        } catch (UnsupportedEncodingException ex) {
        } catch (NoSuchAlgorithmException e) {
        }

    }

    public String encrypt(String password, String input) {
        String returnString = "";
        try {
            generateKey(password);
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, sKey);
            return Base64.getEncoder().encodeToString(c.doFinal(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
        } catch (NoSuchPaddingException ex) {
        } catch (UnsupportedEncodingException ex) {
        } catch (InvalidKeyException ex) {
        } catch (IllegalBlockSizeException ex) {
        } catch (BadPaddingException ex) {
        }

        return returnString;
    }

    public String decrypt(String password, String input) {
        String returnString = "";
        try {
            generateKey(password);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(input)));
        } catch (NoSuchAlgorithmException ex) {
        } catch (NoSuchPaddingException ex) {
        } catch (InvalidKeyException ex) {
        } catch (IllegalBlockSizeException ex) {
        } catch (BadPaddingException ex) {
        }

        return returnString;
    }

}
