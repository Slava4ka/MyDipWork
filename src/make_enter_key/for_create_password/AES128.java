package make_enter_key.for_create_password;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by Slavik on 03.12.2016.
 */
public class AES128 {

    public String encrypt(String strToEncrypt, SecretKeySpec secretKey) {
        try {
            System.out.println(secretKey.getFormat());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            showInfo(cipher);  // аоказывает информацию
            String s = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            System.out.println(s);
            return s;
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decrypt(String strToDecrypt, SecretKeySpec secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            showInfo(cipher);
            String s = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            System.out.println(s);
            return s;
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


    public void showInfo(Cipher cipher) {
        System.out.println(cipher.getAlgorithm() + " " + cipher.getBlockSize());
    }
}
