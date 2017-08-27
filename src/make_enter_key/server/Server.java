package make_enter_key.server;

import make_enter_key.dump.Dump;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    ArrayList<String> clientsDump;
    Dump dump;

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSocket) {
            try {
                socket = clientSocket;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            // true - имя использовать нельзя, оно есть. false - имени нету, можно юзать.
            MessageForNameMake messageSer;
            try {
                while (true) {
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    messageSer = (MessageForNameMake) ois.readObject();
                    getDump();
                    String name = messageSer.getName();

                    boolean flag = false; // если флаг фалс - то это имя добавится. В противном случае оно есть в дампе

                    for (String s : clientsDump) {
                        if (s.equals(name)) {
                            sendMessage(socket, true);
                            flag = true;
                        }
                    /*и вот тут проверяю и даю ответ, если все норм то записываю*/
                    }

                    if (flag == false) {
                        sendMessage(socket, false);
                        addToDump(name);
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка при обмене данными с удаленным адресом " + (socket));
            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка при обмене данными с удаленным адресом " + (socket));
            }
        } // run
    } // вложенный клас


    public static void main(String[] args) {
        new Server().go();
    }

    public void go() {
        try {
            ServerSocket serverSocket = new ServerSocket(4444);
            clientsDump = new ArrayList<>();
            dump = new Dump();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got for_create_password.a connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // clouse go();

    public synchronized void getDump() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ClientsDump.diplom"));
            dump = (Dump) objectInputStream.readObject();
            System.out.println("dump is read");
            clientsDump = dump.getNames();
            if (clientsDump.isEmpty()) {
                clientsDump.add("null");
                System.out.println("was empty");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void addToDump(String s) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ClientsDump.diplom"));
            dump = (Dump) objectInputStream.readObject();
            System.out.println("dump is read");
            dump.getNames().add(s);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("ClientsDump.diplom"));
            objectOutputStream.writeObject(dump);
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void sendMessage(Socket socket, boolean b) {
        MessageForNameMake message = new MessageForNameMake();
        message.setAnsver(b);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
} // clouse class