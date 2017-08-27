package messanger.test_.for_take_password;

import make_enter_key.for_create_password.AES128;
import messanger.ParamForEnter;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Slavik on 08.12.2016.
 */
public class openEnterFile {
    public static void main(String[] args) {
        String adress = "ClientВано.diplom";
        String name = read(adress);
        System.out.println("Прочел имя " + name);
    }
    public static String read(String adress){

        String name;
        String password = "-1";

        while (password.equals("-1")) {
            password = readPassword();
        }
        System.out.println("password" +password);
        ParamForEnter paramForEnter = new ParamForEnter();


        String nameTemp = null;
        String podTemp = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(adress));
            ParamForEnter readParam = (ParamForEnter) objectInputStream.readObject();
            System.out.println("Имя юзера "+readParam.getName());
            nameTemp = readParam.getName();
            podTemp = readParam.getP();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "что то пошло не так");
            System.exit(1);
            ex.printStackTrace();
        }

        System.out.println(nameTemp);
        System.out.println(podTemp);

        String s = GenerateHash(password);
        System.out.println(s + "\n" + s.length());
        byte[] keyB = s.getBytes();
        for (byte b : keyB) {
            System.out.print(b + " ");
        }

        System.out.println(keyB.length);

        byte[] keyA = new byte[16];
        for (int i = 0; i < 16; i++) {
            keyA[i] = keyB[i];
        }

        System.out.println(keyA.length);

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyA, "AES");

        AES128 aes128 = new AES128();
        String PODforCheck = aes128.decrypt(podTemp, secretKeySpec);
        System.out.println("PODforCheck " + PODforCheck);


        if(PODforCheck!=null){
            name = aes128.decrypt(nameTemp, secretKeySpec);
            JOptionPane.showMessageDialog(null,
                    "вы ввели верный пароль");
            return name;
        }else{
            String dr = read(adress);
            return dr;
        }
    }

    public  static String GenerateHash(String input) {
        MessageDigest objSHA = null;
        try {
            objSHA = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytSHA  = objSHA.digest(input.getBytes());
        BigInteger intNumber = new BigInteger(1, bytSHA);
        String strHashCode = intNumber.toString(16);

        // pad with 0 if the hexa digits are less then 64.
        while (strHashCode.length() < 64) {
            strHashCode = "0" + strHashCode;
        }
        return strHashCode;
    }

    public static String readPassword() {
        Icon icon = new ImageIcon();
        String s = (String)JOptionPane.showInputDialog(
                null,
                "Введите пароль",
                "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE,
                icon, null,
                null);

//If for_create_password.a string was returned, say so.
        if ((s != null) && (s.length() > 3)) {
            s = s.trim();
            return s;
        }
        JOptionPane.showMessageDialog(null,
                "вы не ввели пароль");
        readPassword();
        return "-1";
    }
}
