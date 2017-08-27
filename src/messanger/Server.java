package messanger;

import messanger.Objects.Connect_info;
import messanger.Objects.Friend;
import messanger.Objects.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    ArrayList<Connect_info> clientInfo = new ArrayList(); // в объектах содержится инфа о адрессе и имени клиетов онлайн
    boolean tellAboutClone = false;

    public class ClientHandler implements Runnable {
       // BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSocket) {
            try {
                socket = clientSocket;
                ///InputStreamReader isReadr = new InputStreamReader(socket.getInputStream());
               // reader = new BufferedReader(isReadr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            Message messageSer;
            System.out.println(("Установленно соединение с адресом " + socket.getRemoteSocketAddress()));
            System.out.println(socket.getRemoteSocketAddress());

            try {
                while (true) {
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    messageSer = (Message) ois.readObject();

                    antiClon(messageSer, socket); //проверяет уникальность

                    getAbonentNameAtFirstTime(messageSer, socket); // вот тут надо получать список друзьяшек и помещать в объкты Connect_info
                    addAbonentFriend(messageSer, socket);

                    checkOnlineFriendsAndSendToCleient(socket); // тут отвечает ему о тех кто онлайн
                    delivery(); // тут оповещает всех "друзей" о выходе в онлайн клиента (толькл тех, у кого он в списке друзьяшек)

                    // System.out.println("Принял сообщение от: " + messageSer.getName() + " Содержание: " + messageSer.getData());


                    if (isWhereKey(messageSer)) {
                        tellToAdress(messageSer);
                    }
                    if (isWhereData(messageSer)) {
                        if (isWhereAdress(messageSer)) {
                            tellToAdress(messageSer);
                        } else {
                            /*tellEveryone(messageSer);*/
                        }
                    } else {
                        System.out.println("нету текстового содержимого");
                    }
                }
                //  }
            } catch (IOException e) {
                System.out.println("Ошибка при обмене данными с удаленным адресом " + getNameFromSocket(socket));
                if (tellAboutClone == false) {
                    aboutOfflineFriend(socket);
                    deleteFromeList(getNameFromSocket(socket), socket);
                    soutOlineUsersList();
                } else {
                    System.out.println("выкинули клона нахрен");
                    tellAboutClone = false;
                }

                // sendOnlineListForClientsMessage();
            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка при обмене данными с удаленным адресом " + getNameFromSocket(socket));
                if (tellAboutClone == false) {
                    aboutOfflineFriend(socket);
                    deleteFromeList(getNameFromSocket(socket), socket);
                    soutOlineUsersList();
                }else {
                    System.out.println("выкинули клона нахрен");
                    tellAboutClone = false;
                }
                //  sendOnlineListForClientsMessage();
            }
        } // run
    } // вложенный клас

    public static void main(String[] args) {
        new Server().go();
    }

    public void go() {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                firsConnect(clientSocket);
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // clouse go();

    /*public void tellEveryone(Message message) {
        System.out.println("зашел в tellEveryOone");
        for (Connect_info c_i : clientInfo) {
            String name = c_i.getName();
            Socket socket = c_i.getclientSocket();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Сервер отправил " + name + " по адресу " + socket.getRemoteSocketAddress() + " следующее собщение: " + message.getData());
                oos.writeObject(message);
                oos.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }  // clouse for
    } // clouse tellEveryone*/

    public void tellToAdress(Message message) {
        System.out.println("зашел в tellToAdress");
        Socket socket;
        String name;
        for (Connect_info c_i : clientInfo) {
            if (c_i.getName().equals(message.getAdress())) {
                socket = c_i.getclientSocket();
                name = message.getAdress();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    System.out.println("Сервер отправил  " + name + " по адресу " + socket.getRemoteSocketAddress() + " от " + message.getName() + " следующее собщение: " + message.getData());
                    oos.writeObject(message);
                    oos.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isWhereAdress(Message message) {// проверяет наличие адресата сообщения
        boolean adress = false;
        ArrayList temp = message.getInsructions();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i) == MessageType.ADRESS) {
                adress = true;
                System.out.println("isWhereAdress = " + adress + " Адресату: " + message.getAdress());
            }
        }
        return adress;
    } // clouse isWhereAdress

    public boolean isWhereData(Message message) { // проверяет естьли в сообщении тектовое письмо
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

    public void firsConnect(Socket socket) { // записывает в коллекцию clientInfo адрес нового клиента в виде стринга и сокета
        Connect_info info = new Connect_info();
        info.setAdress(String.valueOf(socket.getRemoteSocketAddress()));
        info.setclientSocket(socket);
        clientInfo.add(info);
        System.out.println("На сервер подключичился абонент " + info.getAdress());
    }// clouse firstConnect

    public String getNameFromSocket(Socket socket) { // ищет по сокету имя объекта в коллекции clientInfo
        String name = "";
        for (Connect_info c_i : clientInfo) {
            if (c_i.getclientSocket().equals(socket)) {
                name = c_i.getName();
            }
        }
        return name;
    } // clouse getNameFromSocket

    public void getAbonentNameAtFirstTime(Message message, Socket socket) { // после выделения нового потока должен присвоить к адрессу имя абонента и установить список друзей

        ArrayList temp = message.getInsructions();

        boolean enter = false;
        boolean user_name = false;
        boolean friends_list = false;

        for (int i = 0; i < temp.size(); i++) {

            if (temp.get(i) == MessageType.ENTER) {
                enter = true;
                System.out.println("Enter " + enter);
            }
            if (temp.get(i) == MessageType.USER_NAME) {
                user_name = true;
                System.out.println("User name " + user_name);
            }
            if (temp.get(i) == MessageType.FRIENDS_LIST) {
                friends_list = true;
                System.out.println("Friends list " + friends_list);
            }
        }

        if (enter && user_name && friends_list) {
            for (Connect_info c_i : clientInfo) {
                if (c_i.getAdress().equals(String.valueOf(socket.getRemoteSocketAddress()))) {
                    System.out.println(c_i.getAdress() + " " + String.valueOf(socket.getRemoteSocketAddress()));
                    c_i.setName(message.getName());
                    c_i.setClientsFriendsList(message.getFriendsList());

                    /* Короче, кайф мне посмотреть фильмец и чет не думается))) Так что все надо доделать завтра
                     Эта функция уже добавляет в clientInfo в объект Connect_info входящую с сообщением коллекцию друзей клиета
                     теперь мне надо сделать так, чтоб сервер отправлял всем пользователям инфо о их друзьях
                      */
                }
            }
        }
    } // clouse getAbonentName

    public void addAbonentFriend(Message message, Socket socket) { // после выделения нового потока должен присвоить к адрессу имя абонента и установить список друзей

        ArrayList temp = message.getInsructions();

        boolean add_friend = false;
        boolean user_name = false;
        boolean friends_list = false;

        for (int i = 0; i < temp.size(); i++) {

            if (temp.get(i) == MessageType.ADD_FRIEND) {
                add_friend = true;
                System.out.println("add_friend " + add_friend);
            }
            if (temp.get(i) == MessageType.USER_NAME) {
                user_name = true;
                System.out.println("User name " + user_name);
            }
            if (temp.get(i) == MessageType.FRIENDS_LIST) {
                friends_list = true;
                System.out.println("Friends list " + friends_list);
            }
        }

        if (add_friend && user_name && friends_list) {
            for (Connect_info c_i : clientInfo) {
                if (c_i.getAdress().equals(String.valueOf(socket.getRemoteSocketAddress()))) {
                    System.out.println(c_i.getAdress() + " " + String.valueOf(socket.getRemoteSocketAddress()));
                    c_i.setClientsFriendsList(message.getFriendsList());

                    // Эта функция уже добавляет в clientInfo в объект Connect_info входящую с сообщением коллекцию друзей клиета
                }
            }
        }
    } // clouse getAbonentName

    public void checkOnlineFriendsAndSendToCleient(Socket socket) { // уведомить о пользователях онлайн
        for (Connect_info connect_info : clientInfo) {
            if (connect_info.getclientSocket().equals(socket)) {
                ArrayList<Friend> temp = connect_info.getClientsFriendsList();
                for (int i = 0; i < temp.size(); i++) {
                    for (int j = 0; j < clientInfo.size(); j++) {
                        if (temp.get(i).getFriendName().equals(clientInfo.get(j).getName())) {
                            temp.get(i).setFriendStatus(true);
                        }
                    }
                }

                Message message = new Message();
                message.setFriendsList(temp);
                message.setInsructions(MessageType.STATUS);

                try {
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    oos.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void aboutOfflineFriend(Socket socket) {
        String name = "";
        for (Connect_info connect_info : clientInfo) {
            if (connect_info.getclientSocket().equals(socket)) {
                name = connect_info.getName();
                ArrayList<Friend> temp = connect_info.getClientsFriendsList();
                System.out.println(temp.size());
                for (int i = 0; i < temp.size(); i++) {
                    for (int j = 0; j < clientInfo.size(); j++) {
                        if (temp.get(i).getFriendName().equals(clientInfo.get(j).getName())) {

                            Message message = new Message();
                            ArrayList<Friend> temp2 = new ArrayList();
                            Friend me = new Friend();
                            me.setFriendName(name);
                            me.setFriendStatus(false);
                            temp2.add(me);

                            message.setFriendsList(temp2);
                            message.setInsructions(MessageType.STATUS);

                            try {
                                ObjectOutputStream oos = new ObjectOutputStream(clientInfo.get(j).getclientSocket().getOutputStream());
                                oos.writeObject(message);
                                oos.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public void delivery() { // рассылка всем юзерам после добавления кого либо
        for (Connect_info connect_info : clientInfo) {

            Socket socket = connect_info.getclientSocket();

            ArrayList<Friend> temp = connect_info.getClientsFriendsList();

            for (int i = 0; i < temp.size(); i++) {
                for (int j = 0; j < clientInfo.size(); j++) {
                    if (temp.get(i).getFriendName().equals(clientInfo.get(j).getName())) {
                        temp.get(i).setFriendStatus(true);
                    }
                }
            }

            Message message = new Message();
            message.setFriendsList(temp);
            message.setInsructions(MessageType.STATUS);

            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(message);
                oos.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void deleteFromeList(String name, Socket socket) { // удаление объкта из коллекции - используется когда кто то отключается
        for (int i = 0; i < clientInfo.size(); i++) {
            Connect_info connect_info = clientInfo.get(i);
            if ((connect_info.getName().equals(name)) && (connect_info.getclientSocket().equals(socket))) {
                clientInfo.remove(i);
            }
        }
    }

    public void soutOlineUsersList() { // вывести список пользоваетелей, котовые сейчас онлайн
        System.out.println("Сейчас онлайн: ");
        for (Connect_info connect_info : clientInfo) {
            System.out.println(connect_info.getName() + " " + connect_info.getAdress());
        }
    }

    public boolean isWhereKey(Message message) {
        boolean flag = false;
        ArrayList<MessageType> a = message.getInsructions();
        for (MessageType messageType : a) {
            if (messageType == MessageType.GET_KEY_SIDE_A) {
                flag = true;
            }
            if (messageType == MessageType.GET_KEY_SIDE_B) {
                flag = true;
            }
        }
        return flag;
    }

    public void antiClon(Message message, Socket socket) { //проыеряет, пользователь уже онлайн или нет!
        //попробуем так, без ответа клиента, если не будет работать - то надо порешать. Пока так
        System.out.println("размер коллекции клиентинфо " + clientInfo.size());
        //System.out.println(clientInfo.get(1).getName());
        if (clientInfo.size() > 1) {
            System.out.println("зашев в клиентинфо");
            ArrayList temp = message.getInsructions();

            boolean enter = false;
            boolean user_name = false;

            for (int i = 0; i < temp.size(); i++) {

                if (temp.get(i) == MessageType.ENTER) {
                    enter = true;
                    System.out.println("Enter " + enter);
                }
                if (temp.get(i) == MessageType.USER_NAME) {
                    user_name = true;
                    System.out.println("User name " + user_name);
                }
            }

            if ((enter == true) && (user_name == true)) {
                String name = message.getName();
                System.out.println(name);
                for (Connect_info connect_info : clientInfo) {
                    if (connect_info.getName() != null) {
                        System.out.println("Поле имени объекта не пустое " + connect_info.getName());
                        if (connect_info.getName().equals(name)) {
                            System.out.println("ЗАФИКСИРОВАНА ПОПЫТКА ДОСТУПА ВТОРОГО КЛИЕНТА ПО ОДНОМУ ВХОДНОМУ КЛЮЧУ");
                            Message messageKill = new Message();
                            messageKill.setInsructions(MessageType.FATALL_ERROR_THIS_USER_ONLINE);
                            tellAboutClone = true;
                            System.out.println("активировал tellAboutClone " + tellAboutClone);
                            try {
                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                System.out.println("Сервер отправил  " + name + " по адресу " + socket.getRemoteSocketAddress() + " от " + message.getName() + " Уведомление о фатальной ошибке доступа");
                                oos.writeObject(messageKill);
                                oos.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
} // clouse class
