package messanger;

import messanger.AES.AES128;
import messanger.DiffieHellman.SideA;
import messanger.DiffieHellman.SideB;
import messanger.Objects.*;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Client {

    JTextArea incoming;
    JTextField outgoing;
    JFrame frame;
    Socket socket;

    String name = null; // имя клиента
    String adress_name = null;
    String adress_name_temp = null;
    boolean flagToFirstGenerateKey = false; //чтоб генерация ключа со стороны A сработала только одигн раз при запуске
    boolean notClone = false;

    ArrayList<Friend> myFriendsList; // список друзей клиента
    ArrayList<Keys> friendsListForKeys = new ArrayList();
    ArrayList<Dialogs> myDialogsList = new ArrayList(); // список диалогов

    String[] data; // тут хранятся имена друзей с Online пометкой
    DefaultListModel model;
    Enter enter; // сюда помещается имя клиента и список друзей

    JLabel label;

    JLabel label2;
    String listOfNewM;

    JList list;

    public static void main(String[] args) {
        Client client = new Client();
        client.go();
    }

    public void go() {
        enter = new Enter();
        try {
            enter.Start();
            name = enter.getName();
            myFriendsList = enter.getFriendsClassFriends();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Чат " + getNameChatClient());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {

            }

            public void windowClosed(WindowEvent event) {

            }

            public void windowClosing(WindowEvent event) {
                Object[] options = { "Да", "Нет!" };
                int n = JOptionPane
                        .showOptionDialog(event.getWindow(), "Закрыть окно?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    event.getWindow().setVisible(false);
                    System.exit(0);
                }
            }

            public void windowDeactivated(WindowEvent event) {

            }

            public void windowDeiconified(WindowEvent event) {

            }

            public void windowIconified(WindowEvent event) {

            }

            public void windowOpened(WindowEvent event) {

            }

        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        System.out.println("клиент" + name);
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setLineWrap(true);
        incoming.setEditable(false);
        JScrollPane qScroll = new JScrollPane(incoming);
        qScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);

        JButton ShowKeys = new JButton("Показать ключи");
        ShowKeys.addActionListener(new showKeys());

        JButton sendButton = new JButton("Отправить сообщение");
        sendButton.addActionListener(new SendButtonListener());

        JButton addFriendButton = new JButton("Добавить друга");
        addFriendButton.addActionListener(new addFriendButton());

        JButton deleteFriendButton = new JButton("Удалить друга");
        deleteFriendButton.addActionListener(new deleteFriendButton());

        mainPanel.add(qScroll);
        mainPanel.add(outgoing);


        JPanel secondMainPanel = new JPanel();
        secondMainPanel.setLayout(new BoxLayout(secondMainPanel, BoxLayout.X_AXIS));
        mainPanel.add(secondMainPanel);

        Box box = Box.createHorizontalBox();
        mainPanel.add(box);

        box.add(Box.createVerticalStrut(1));
        box.add(sendButton);
        box.add(Box.createVerticalStrut(1));
        box.add(ShowKeys);
        box.add(Box.createVerticalStrut(1));
        box.add(addFriendButton);
        box.add(Box.createVerticalStrut(1));
        box.add(deleteFriendButton);
        box.add(Box.createVerticalStrut(1));


        list = new JList(data);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(0);

        JScrollPane listScroll = new JScrollPane(list);
        listScroll.setPreferredSize(new Dimension(100, 100));

        mainPanel.add(listScroll);

        model = new DefaultListModel();
        list.setModel(model);

        label = new JLabel();
        label.setText("");

        label2 = new JLabel();
        listOfNewM = "New Message: ";
        label2.setText(listOfNewM);


        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setIncoming();
                //  System.out.println("In valueChanged");
                String string = String.valueOf(list.getSelectedValue());
                adress_name_temp = string;
                if (string.contains(" Online")) {
                    // System.out.println("Where is Online");
                    String asd = string.replaceAll(" Online", "").trim();
                    label.setText(asd);
                    adress_name = asd;
                    //  System.out.println(adress_name + " = " + asd);
                } else {
                    // System.out.println("Where is NO Online");
                    label.setText(string);
                    adress_name = string;
                    // System.out.println("!!!!! " + adress_name);
                }
            }
        });

        mainPanel.add(label, BorderLayout.WEST);
        mainPanel.add(label2, BorderLayout.WEST);

        setFriendsList();

        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(700, 400);
        frame.setVisible(true);
    }

    public class showKeys implements ActionListener { //показывает имеющиеся ключи в данный момент
        @Override
        public void actionPerformed(ActionEvent e) {
            //временно, потом удалить выводить будет все текущие ключики

            String res = "";
            for (Keys keys : friendsListForKeys) {
                String s1;
                String s2 = "";
                String s3;
                byte[] result = keys.getSecretKeySpec().getEncoded();
                String name = keys.getFriedsName();
                System.out.print("Ключ для пользователя " + name + ": ");
                s1 = "Ключ для пользователя " + name + ": ";
                for (byte b : result) {
                    System.out.print(b + " ");
                    s2 = s2 + b + " ";
                }
                s3 = s1 + s2;
                System.out.println();
                res = res + s3+"\n";
            }
            JOptionPane.showMessageDialog(null,
                    res);
            //
        }
    } // clouse class

    public class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(adress_name_temp);
//добавить проверку на пустоту и null
            System.out.println("SendButtonListener " + adress_name);
            if ((adress_name != null) && !(adress_name.equals("null"))) {
                if (adress_name_temp.indexOf(" Online") != -1) {
                    System.out.println("adress_name " + adress_name);
                    Message message = new Message();
                    message.setName(getNameChatClient());

                    //вот тут встраивается шифрование текста
                    AES128 aes128 = new AES128();
                    SecretKeySpec temp_key = null;
                    for (Keys keys : friendsListForKeys) {
                        if (keys.getFriedsName().equals(adress_name)) {
                            temp_key = keys.getSecretKeySpec();
                        }
                    }

                    if (temp_key != null) {

                        System.out.println(outgoing.getText());
                        byte[] result = temp_key.getEncoded();
                        for (byte b : result) {
                            System.out.print(b + " ");
                        }
                        System.out.println();

                        String temppop = aes128.encrypt(outgoing.getText(), temp_key);
                        System.out.println(temppop);
                        message.setData(temppop);
                        //встраивание закончено
                    } else {
                        message.setData("Для обмена сообщениями с этим пользователем довьте его в друзья и перезапустите чат");
                    }


                    message.setAdress(adress_name);
                    message.setInsructions(MessageType.USER_NAME, MessageType.MESSAGE_TEXT, MessageType.ADRESS); // имя отправителя, сообщени, адресс
                    //System.out.println(message.getName()+" "+message.getData());
                    putMessegeToTheDialogsListAfterButtonSend(message, outgoing.getText());
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(message);
                        oos.flush();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    outgoing.setText("");
                    //outgoing.requestFocus();
                    //incoming.append(message.getName() + ": " + message.getAdress() + ", " + message.getData() + "\n");

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Получатель в оффлайне",
                            "Inane warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Не выбран получатель письма",
                        "Inane error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    } // clouse class

    public class addFriendButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("addFriendButton click");
            String test1 = null;
            Boolean b = false;
            test1 = JOptionPane.showInputDialog(null, "Введите имя: ").trim();
            test1 = test1.trim();
            test1 = test1.replaceAll(" ","");

            System.out.println("Введенное имя " + test1);
            for (Friend friend : myFriendsList) {
                if (friend.getFriendName().equals(test1)) {
                    b = true;
                }
            }
            if (!(test1.equals(name))) {
                System.out.println("флаг значения переменной типа булеан, отвечающей за совпадение введенного имени с имеющимся \n" +
                        "если тру, то не зайдет дальше, а выкинет ошибку, если фолс, то все норм и этот друг добавится= " + b);
                if (test1 != null && !(test1.equals(""))) {
                    if (b == false) {
                        enter.addFriend(test1);
                        enter.refresh();
                        myFriendsList = enter.getFriendsClassFriends();
                        setFriendsList();
                        Message message = new Message();
                        message.setInsructions(MessageType.USER_NAME, MessageType.ADD_FRIEND, MessageType.FRIENDS_LIST); // отправляет на сервер сообщение с инсрукциями  USER_NAME ENTER
                        //FRIENDS_LIST - вкладывая при этом в сообщение коллекцию с друзьями
                        message.setName(getNameChatClient());
                        message.setFriendsList(myFriendsList);
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject(message);
                            oos.flush();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Этот пользователь уже в у вас в списке друзей",
                                "Inane warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Вы ничего не ввели",
                            "Inane warning",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Вы не можете добавить себя в друзья",
                        "Inane warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public class deleteFriendButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("deleteFriendButton");
            String[] templ = new String[data.length];

            for (int i = 0; i < templ.length; i++) {
                templ[i] = data[i].replaceAll(" Online", "").trim();
            }

            Icon icon = new ImageIcon();
            Object[] possibilities = templ;
            String user = (String) JOptionPane.showInputDialog(
                    null,
                    "Выберите из списка друга, которого хотите удалить",
                    "Удаление из списка друзей",
                    JOptionPane.INFORMATION_MESSAGE,
                    icon,
                    possibilities,
                    templ[0]);

//If a string was returned, say so.
            if ((user != null) && (user.length() > 0)) {
                System.out.println("был выбран " + user);
                enter.deleteFriend(user);
                enter.refresh();
                myFriendsList = enter.getFriendsClassFriends();
                setFriendsList();
                Message message = new Message();
                message.setInsructions(MessageType.USER_NAME, MessageType.ADD_FRIEND, MessageType.FRIENDS_LIST); // отправляет на сервер сообщение с инсрукциями  USER_NAME ENTER
                //FRIENDS_LIST - вкладывая при этом в сообщение коллекцию с друзьями
                message.setName(getNameChatClient());
                message.setFriendsList(myFriendsList);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    oos.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "пользователь " + user + " был удален!");
            }


        }
    }

    // ВСЕ СЧИТЫВАЕТСЯ ТУТА 1!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            Message messageSer;
            try {
                while (true) {
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    messageSer = (Message) ois.readObject(); // получил объект

                    updateOnlineClientList(messageSer); // обновил тех кто онлайн

                    clonControll(messageSer); //проверяет есть ли такой пользователь

                    //вход по флагу
                    if ((flagToFirstGenerateKey == false) && (notClone == true)) {
                        System.out.println("flagToFirstGenerateKey == " + flagToFirstGenerateKey + " зашел");
                        if (data.length > 0) {
                            System.out.println("if (data.length > 0) - зашел; data.length = " + data.length);
                            for (String s : data) {
                                System.out.println("for(String s : data); s= " + s);
                                if (s.contains(" Online")) {
                                    System.out.println("if(s.contains( Online)); " + s.contains(" Online"));
                                    String nameTemp = s.replaceAll(" Online", "").trim();
                                    System.out.println(nameTemp);
                                    SideA sideA = new SideA();
                                    sideA.generateSecretKey(socket, nameTemp, name);
                                    System.out.println("c клиентом " + nameTemp + " был сгенерирован ключ:");
                                    sideA.showHashSecretKey(); // вот тут происходит обмен ключами ЖИ ЕСТЬ
                                    //тут надо добавлять ключ в коллекцию
                                    SecretKeySpec secretKeySpec = new SecretKeySpec(sideA.getHashSecretKey(), "AES");
                                    Keys key = new Keys();
                                    key.setFriedsName(nameTemp);
                                    key.setSecretKeySpec(secretKeySpec);
                                    friendsListForKeys.add(key);
                                }
                            }
                        } else {
                            System.out.println("в онлайне нету друзей");
                        }
                        // тра та та та
                        flagToFirstGenerateKey = true;
                        System.out.println("теперь flagToFirstGenerateKey = " + flagToFirstGenerateKey + " и больше сюда никто не зайдет");
                    }

                    if (isWhereKey(messageSer)) {
                        System.out.println("тут естьь ключ");
                        SideB sideB = new SideB();
                        sideB.generateSecretKey(socket, messageSer);
                        System.out.println("Ключ c клиентом " + messageSer.getName() + " : ");
                        sideB.showHashSecretKey();

                        SecretKeySpec secretKeySpec = new SecretKeySpec(sideB.getHashSecretKey(), "AES");
                        //тут надо добавлять ключ в коллекцию
                        Keys key = new Keys();
                        key.setFriedsName(messageSer.getName());
                        key.setSecretKeySpec(secretKeySpec);
                        friendsListForKeys.add(key);
                    }

                    if (isWhereData(messageSer)) {
                        System.out.println(messageSer.getName() + " " + messageSer.getData());
//дешифрование
                        AES128 aes128 = new AES128();
                        SecretKeySpec temp_key = null;

                        for (Keys keys : friendsListForKeys) {
                            if (keys.getFriedsName().equals(messageSer.getName())) {
                                temp_key = keys.getSecretKeySpec();
                            }
                        }

                        if (temp_key != null) {
                            byte[] result = temp_key.getEncoded();
                            for (byte b : result) {
                                System.out.print(b + " ");
                            }
                            System.out.println();
                            String tempop = messageSer.getData();
                            messageSer.setData(aes128.decrypt(tempop, temp_key));

                            //конец
                            //  incoming.append(messageSer.getName() + ": " + messageSer.getAdress() + ", " + messageSer.getData() + "\n");
                            putMessegeToTheDialogsList(messageSer);
                        } else {
                            putMessegeToTheDialogsList(messageSer);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setUpNetworking() {
        try {
            socket = new Socket("localhost", 5000);

            Message message = new Message();
            message.setInsructions(MessageType.USER_NAME, MessageType.ENTER, MessageType.FRIENDS_LIST); // отправляет на сервер сообщение с инсрукциями  USER_NAME ENTER FRIENDS_LIST
            //FRIENDS_LIST - вкладывая при этом в сообщение коллекцию с друзьями
            message.setName(getNameChatClient());
            message.setFriendsList(myFriendsList);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(message);
                oos.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Networking established");
        } catch (IOException e) {
            Object[] options = {"Да", "Нет"};
            Icon icon = new ImageIcon();

            int n = JOptionPane.showOptionDialog(null,
                    "В данный момент сервер не доступен!\n" +
                            "Хотите ли вы повторить попытку соединения?",
                    "Ошибка соединения с сервером",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]); //default button title

            System.out.println("n = " + n);
            if (n == 1 || n == -1) {
                System.exit(1);
                frame.dispose();
            } else {
                setUpNetworking();
            }
            e.printStackTrace();

        }
    }

    private String getNameChatClient() {
        return name;
    }

    private boolean isWhereData(Message message) { // проверяет естьли в сообщении тектовое письмо
        boolean data = false;
        ArrayList temp = message.getInsructions();

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i) == MessageType.MESSAGE_TEXT) {
                data = true;
                System.out.println("isWhereData = " + data + " содержание: " + message.getData());
            }
        }
        return data;
    } // clouse isWhereData

    private void setFriendsList() {
        int r = myFriendsList.size();
        data = new String[r];
        for (int i = 0; i < r; i++) {
            data[i] = myFriendsList.get(i).getFriendName();
        }
        model.clear();
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
            model.addElement(data[i]);
        }
    }

    private void updateOnlineClientList(Message message) {
        // обновляет список юзеров, которые онлайн ГАВНОКОДДДДДДД

        // На вхожд идет коллекци со списком друзей и значениями тру или фалс в графе статуса
        // список будет полностьюб обновляться при каждом вызове функции updateOnlineClientList
        //она должна срабатывать каждый раз, когда приходит message с инструкцией STATUS;

        ArrayList<MessageType> temp = message.getInsructions();
        String[] tempDataOnline;
        String[] tempDataOffline;
        int tOnline = 0;
        int tOffline = 0;
        for (MessageType messageType : temp) { // это сработает только один раз, так как инструкция STATUS только одна
            if (messageType == MessageType.STATUS) {
                ArrayList<Friend> tempFriend = message.getFriendsList();
                for (Friend friend : tempFriend) {
                    if (friend.isFriendStatus()) { //true
                        tOnline = tOnline + 1;
                    } else {
                        tOffline = tOffline + 1;
                    }
                }
                tempDataOnline = new String[tOnline];
                tempDataOffline = new String[tOffline];
                int ttOline = 0;
                int ttOffline = 0;
                for (Friend friend : tempFriend) {
                    if (friend.isFriendStatus()) {  //if true
                        tempDataOnline[ttOline] = friend.getFriendName();
                        ttOline = ttOline + 1;
                    } else {
                        tempDataOffline[ttOffline] = friend.getFriendName();
                        ttOffline = ttOffline + 1;
                    }
                }

                System.out.println("Вот те друзья, которые онлайн: ");
                for (int i = 0; i < tOnline; i++) {
                    System.out.println(tempDataOnline[i]);
                }
                System.out.println("Вот те друзья, которые оффлайн: ");
                for (int i = 0; i < tOffline; i++) {
                    System.out.println(tempDataOffline[i]);
                }

                //Вот тут будут удаляться ключи:
                if (!friendsListForKeys.isEmpty()) {
                    System.out.println("friendsListForKeys - там что то есть");
                    for (int i = 0; i < tOffline; i++) {
                        System.out.println("удаляю ключ " + tempDataOffline[i]);
                        Iterator<Keys> itr = friendsListForKeys.iterator(); // remove all even numbers
                        while (itr.hasNext()) {
                            Keys k = itr.next();
                            if (k.getFriedsName().equals(tempDataOffline[i])) {
                                itr.remove();
                            }
                        }
                    }
                } else {
                    System.out.println("friendsListForKeys - ничего тут нету");
                }
                //

                String[] backUpData = data;

                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < tempDataOnline.length; j++) {
                        if (data[i].equals(tempDataOnline[j])) {
                            data[i] = data[i] + " Online";
                        } else {
                            data[i] = backUpData[i];
                        }
                    }
                }

                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j < tempDataOffline.length; j++) {
                        String tempo = data[i];
                        String tempa = tempDataOffline[j];
                        tempa = tempa + " Online";
                        if (tempo.equals(tempa)) {
                            data[i] = tempDataOffline[j];
                        }
                    }
                }

                model.clear();
                for (int i = 0; i < data.length; i++) {
                    model.addElement(data[i]);
                }
            }
        }
    }// clouse updateOnlineClientList()

    public synchronized void putMessegeToTheDialogsList(Message message) {
        System.out.println("зашел в putMessegeToTheDialogsList");
        if (myDialogsList.isEmpty()) {
            System.out.println("зашел в myDialogsList.isEmpty(");
            // сюда зайдет в случае, если диаговового окна еще нету. Должен создать новый диалог с тем, кто пррислал входное сообщение
            // СРАБОТАЕТ ТОЛЬКО ЕСЛИ НЕТУ АКТИВНЫХ ДИАЛОГОВ!!!!
            Dialogs dialogs1 = new Dialogs();
            dialogs1.setName(message.getName());
            dialogs1.setDialogListl(new ArrayList<>());

            messageNew(dialogs1);
            setLabelNewMessage(dialogs1);

            myDialogsList.add(dialogs1);
            dialogs1.getDialogListl().add(message.getName() + ": " + message.getAdress() + ", " + message.getData() + "\n");
        } else {
            boolean b = false;
            for (Dialogs dialogs : myDialogsList) {
                if (dialogs.getName().equals(message.getName())) {
                    System.out.println("зашел в if - нашел такой диалог");
                    dialogs.getDialogListl().add(message.getName() + ": " + message.getAdress() + ", " + message.getData() + "\n");

                    messageNew(dialogs);
                    setLabelNewMessage(dialogs);

                    System.out.println("size= " + dialogs.getDialogListl().size());
                    b = true;
                    System.out.println("b=true");
                }
            }
            if (b == false) {
                System.out.println("зашел в b == false");
                // сюда зайдет в случае, если диаговового окна еще нету. Должен создать
                Dialogs dialogs1 = new Dialogs();
                dialogs1.setName(message.getName());
                dialogs1.setDialogListl(new ArrayList<>());
                dialogs1.getDialogListl().add(message.getName() + ": " + message.getAdress() + ", " + message.getData() + "\n");

                messageNew(dialogs1);
                setLabelNewMessage(dialogs1);

                myDialogsList.add(dialogs1);
            }
        }
        System.out.println("в myDialogsList сейчас находятся: ");
        for (Dialogs dialogs : myDialogsList) {
            System.out.println(dialogs.getName());
        }
        System.out.println("----------------------------");

        //сделаем так чтоб приходили уведомления от недобаленных плльзователей

        boolean si = false;
        for (Friend friend : myFriendsList) {
            if (message.getName().equals(friend.getFriendName())) {
                si = true;
            }
        }
        if (si == false) {
            JOptionPane.showMessageDialog(null,
                    "Вам сообщение от " + message.getName() + "\n" + "***" + message.getData() + "***" + "\n" + "добавьте этого клиента в друзья, чтоб получать от него сообщения!",
                    "Inane error",
                    JOptionPane.INFORMATION_MESSAGE);
        }


    }

    public void putMessegeToTheDialogsListAfterButtonSend(Message message, String data) {
        System.out.println("зашел в putMessegeToTheDialogsListAfterButtonSend");
        if (myDialogsList.isEmpty()) {
            System.out.println("зашел в myDialogsList.isEmpty(");
            // сюда зайдет в случае, если диаговового окна еще нету. Должен создать новый диалог с тем, кто пррислал входное сообщение
            // СРАБОТАЕТ ТОЛЬКО ЕСЛИ НЕТУ АКТИВНЫХ ДИАЛОГОВ!!!!
            Dialogs dialogs1 = new Dialogs();
            dialogs1.setName(message.getAdress());
            dialogs1.setDialogListl(new ArrayList<>());
            myDialogsList.add(dialogs1);
            dialogs1.getDialogListl().add("Я: " + message.getAdress() + ", " + data + "\n");
        } else {
            boolean b = false;
            for (Dialogs dialogs : myDialogsList) {
                if (dialogs.getName().equals(message.getAdress())) {
                    System.out.println("зашел в if - нашел такой диалог");
                    dialogs.getDialogListl().add("Я: " + message.getAdress() + ", " + data + "\n");
                    System.out.println("size= " + dialogs.getDialogListl().size());
                    b = true;
                    System.out.println("b=true");
                }
            }
            if (b == false) {
                System.out.println("зашел в b == false");

                // сюда зайдет в случае, если диаговового окна еще нету. Должен создать
                Dialogs dialogs1 = new Dialogs();
                dialogs1.setName(message.getAdress());
                dialogs1.setDialogListl(new ArrayList<>());
                dialogs1.getDialogListl().add("Я: " + message.getAdress() + ", " + data + "\n");
                myDialogsList.add(dialogs1);
            }
        }
        System.out.println("в myDialogsList сейчас находятся: ");
        for (Dialogs dialogs : myDialogsList) {
            System.out.println(dialogs.getName());
        }
        System.out.println("----------------------------");
    }

    public void setIncoming() {
        boolean b = false;

        for (Dialogs dialogs : myDialogsList) {
            System.out.println("setIncoming" + "\n" + dialogs.getName() + " " + adress_name);
            if (dialogs.getName().equals(adress_name)) {
                b = true;
                incoming.setText(null);

                messageOld(dialogs);
                setLabelNewMessage(dialogs);

                for (String string : dialogs.getDialogListl()) {
                    incoming.append(string);
                }
            }
        }
        if (b == false) {
            incoming.setText("С этим пользователем вы еще не начали беседу");
        }
    }

    public synchronized boolean isWhereKey(Message message) {
        boolean flag = false;
        ArrayList<MessageType> a = message.getInsructions();
        for (MessageType messageType : a) {
            if (messageType == MessageType.GET_KEY_SIDE_A) {
                flag = true;
            }
        }
        return flag;
    }

    public synchronized void clonControll(Message message)  {
        System.out.println("Зашел в клонконтроль");
        boolean fatall_erorr = false;

        ArrayList temp = message.getInsructions();

        for (int i = 0; i < temp.size(); i++) {
            System.out.println("проверяю инструкции");
            if (temp.get(i) == MessageType.FATALL_ERROR_THIS_USER_ONLINE) {
                System.out.println("Фатал ЭРОР");
                fatall_erorr = true;
                System.out.println("ERORR " + fatall_erorr);
            } else {
                System.out.println("Нет фатал эрор");
                notClone = true;
            }
        }
        System.out.println("fatall_erorr " + fatall_erorr);
        if (fatall_erorr) {
            System.out.println("fatall_erorr " + fatall_erorr);
            JOptionPane.showMessageDialog(null,
                    "Этот пользователь уже Online",
                    "Ошибка при входе",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            frame.dispose();
        }


    }

    public void messageNew(Dialogs dialogs) {
        dialogs.setStatus(false);
    }

    public void messageOld(Dialogs dialogs) {
        dialogs.setStatus(true);
    }

    public void setLabelNewMessage(Dialogs dialogs) {
        if (dialogs.isStatus() == true) {
            String temp1 = label2.getText();
            String temp2 = temp1.replaceAll(" " + dialogs.getName(), "");
            label2.setText(temp2);
        }

        if (dialogs.isStatus() == false) {
            String temp1 = label2.getText();
            String temp2 = temp1 + " " + dialogs.getName();
            label2.setText(temp2);
        }
    }

}






