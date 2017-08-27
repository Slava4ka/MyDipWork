package make_enter_key.for_create_password;

import messanger.ParamForEnter;
import make_enter_key.server.CheckOnServer;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class a extends JFrame {

    boolean nameFlag = false;
    boolean passwordFlag = false;

    /* Для того, чтобы впоследствии обращаться к содержимому текстовых полей, рекомендуется сделать их членами класса окна */
    JTextField loginField;
    JTextField passwordField;
    JFrame frame;

    public void newName() {
        frame = new JFrame("Чат");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        // Настраиваем первую горизонтальную панель (для ввода логина)
        Box box1 = Box.createHorizontalBox();
        JLabel loginLabel = new JLabel("Имя:");
        loginField = new JTextField(15);
        box1.add(loginLabel);
        box1.add(Box.createHorizontalStrut(6));
        box1.add(loginField);
// Настраиваем вторую горизонтальную панель (для ввода пароля)
        Box box2 = Box.createHorizontalBox();
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordField = new JPasswordField(15);
        box2.add(passwordLabel);
        box2.add(Box.createHorizontalStrut(6));
        box2.add(passwordField);
// Настраиваем третью горизонтальную панель (с кнопками)
        Box box3 = Box.createHorizontalBox();

        JButton ok = new JButton("OK");
        ok.addActionListener(new okey());

        JButton cancel = new JButton("Отмена");
        cancel.addActionListener(new cancel());

        box3.add(Box.createHorizontalGlue());
        box3.add(ok);
        box3.add(Box.createHorizontalStrut(12));
        box3.add(cancel);
// Уточняем размеры компонентов
        loginLabel.setPreferredSize(passwordLabel.getPreferredSize());
// Размещаем три горизонтальные панели на одной вертикальной
        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new EmptyBorder(12, 12, 12, 12));
        mainBox.add(box1);
        mainBox.add(Box.createVerticalStrut(12));
        mainBox.add(box2);
        mainBox.add(Box.createVerticalStrut(17));
        mainBox.add(box3);

        mainPanel.add(mainBox);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(550, 200);
        frame.setVisible(true);

    }

    private class okey implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name1 = loginField.getText().trim();
            String password = passwordField.getText().trim();
            name1 = name1.replaceAll(" ", "");
            password = password.replaceAll(" ", "");

            System.out.println(name1);
            System.out.println(password);

            chek c = new chek();
            if (c.checkRegEx(password)) {
                String error_message = "";
                boolean w = false;
                if (name1.equals("") || name1.equals(null) || name1.equals("null") || name1.equals(" ")) {
                    w = true;
                    nameFlag = true;
                    error_message = "Пустое поле имени не допустимо!";
                }
//             chekName();
            /*тут будет класс, который будет отправлять на сервак имя и проверять его*/

                if (w == false) {
                    CheckOnServer checkOnServer = new CheckOnServer();
                    nameFlag = checkOnServer.letsChek(name1);
                    error_message = "Такое имя уже есть!";
                    System.out.println("nameFlag " + nameFlag);
                }

                if (nameFlag == true) {
                    System.out.println("косяк в имени");
                    nameFlag = false;
                    JOptionPane.showMessageDialog(null,
                            error_message);

                    a a = new a();
                    a.newName();
                    frame.dispose();
                } else {
                    /*тут будем создавать файл, код у меня уже есть. Только с начала надо его зашифровать (имя и подпись)*/
                    String POD = "97ytb257yv250y36489uNOy-yu54y364P{P}:yu3406y31we2621354UIO{_^*)cv6[v50964y64bn84*^NY6u8nb6-7b6v^U&BJHFasf><>" +
                            "D-070nn496n2537v66134650v54bv-64tv1";
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
                    String nameToFile = aes128.encrypt(name1, secretKeySpec);
                    String podpisToFile = aes128.encrypt(POD, secretKeySpec);

                    System.out.println("nameToFile "+ nameToFile);
                    System.out.println("podpisToFile "+podpisToFile);

                    ParamForEnter paramForEnter = new ParamForEnter();
                    paramForEnter.setName(nameToFile);
                    paramForEnter.setP(podpisToFile);

                    try{
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Client"+name1+".diplom"));
                        objectOutputStream.writeObject(paramForEnter);
                        objectOutputStream.close();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                    // эта просто читает
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("Client"+name1+".diplom"));
                        ParamForEnter a = (ParamForEnter) objectInputStream.readObject();
                        System.out.println("Имя юзера "+a.getName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(null,
                            "Входной файл успешно создан!");

                    /*тут будем создавать файл, код у меня уже есть. Только с начала надо его зашифровать (имя и подпись)*/
                    System.exit(1);
                    frame.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Пароль  не соответствует предъявленным требованиям");
                a a = new a();
                a.newName();
                frame.dispose();
            }
        }
    }

    private class cancel implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(1);
        }
    }

    public static String GenerateHash(String input) {
        MessageDigest objSHA = null;
        try {
            objSHA = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytSHA = objSHA.digest(input.getBytes());
        BigInteger intNumber = new BigInteger(1, bytSHA);
        String strHashCode = intNumber.toString(16);

        // pad with 0 if the hexa digits are less then 64.
        while (strHashCode.length() < 64) {
            strHashCode = "0" + strHashCode;
        }
        return strHashCode;
    }
}