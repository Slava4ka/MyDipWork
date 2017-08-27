package messanger.Objects;

import make_enter_key.for_create_password.AES128;
import messanger.ParamForEnter;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
//Это входная часть, в которой загружается идентификационный файл

/**
 * Created by Slavik on 02.12.2016.
 */
// этот класс создает объект, который используется для загрухки клиента
public class Enter extends JFrame implements Serializable {

    private String name;

    private String adress = null;

    private ArrayList<String> friends;

    private ArrayList<Friend> friendsClassFriends;

    public ArrayList<Friend> getFriendsClassFriends() {
        return friendsClassFriends;
    }

    public void setFriendsClassFriends(ArrayList<Friend> friendsClassFriends) {
        this.friendsClassFriends = friendsClassFriends;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public void Start() throws ClassNotFoundException {
        JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("ClientPart", "diplom");
            fileopen.setFileFilter(filter);
            fileopen.setCurrentDirectory(new File("C:\\Users\\Slavik\\IdeaProjects\\MyDipWork"));
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                adress = file.getPath();
                System.out.println(adress);
            System.out.println(file.getName());
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(adress));
            ParamForEnter paramForEnter = (ParamForEnter) objectInputStream.readObject();

            String adress_Crypto = adress;
            name = read(adress);
            System.out.println("Прочел имя " + name);

            friends = paramForEnter.getFriends();
            System.out.println("Имя пользователя " + name);
            friends.forEach(System.out::println);
            friendsClassFriends = new ArrayList();

            for (String s : friends) {
                Friend friend = new Friend();
                friend.setFriendName(s);
                friendsClassFriends.add(friend);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Нажат calen");
            System.exit(1);
        }
    }

    public void addFriend(String newFriendName){
        try {
            System.out.println("добавляю друга \nоткрываю адрес "+adress);
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(adress));
            ParamForEnter paramForEnter = (ParamForEnter) objectInputStream.readObject();
            System.out.println("открыл");

            paramForEnter.getFriends().add(newFriendName);

            System.out.println("добавил "+newFriendName+" теперь записываю в "+"Client"+name+".diplom");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Client"+name+".diplom"));
            System.out.println("создал файл Client"+name+".diplom");
            objectOutputStream.writeObject(paramForEnter);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(String deleteFriendName){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(adress));
            ParamForEnter paramForEnter = (ParamForEnter) objectInputStream.readObject();
            System.out.println("открыл");

            paramForEnter.getFriends().remove(deleteFriendName);

            System.out.println("удалил "+deleteFriendName+" теперь записываю в "+"Client"+name+".diplom");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Client"+name+".diplom"));
            System.out.println("создал файл Client"+name+".diplom");
            objectOutputStream.writeObject(paramForEnter);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh(){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(adress));
            ParamForEnter paramForEnter = (ParamForEnter) objectInputStream.readObject();
            //name = paramForEnter.getName();
            friends = paramForEnter.getFriends();
            System.out.println("Имя пользователя " + name);
            friends.forEach(System.out::println);
            friendsClassFriends = new ArrayList();

            for (String s : friends) {
                Friend friend = new Friend();
                friend.setFriendName(s);
                friendsClassFriends.add(friend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String read(String adress){

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

    public  String GenerateHash(String input) {
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

    public  String readPassword() {
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
