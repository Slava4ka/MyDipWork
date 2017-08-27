package make_enter_key.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Slavik on 08.12.2016.
 */
public class Client_test {
    static Socket socket;
    public static void main(String[] args) {
        try {
            socket = new Socket("127.0.0.1", 4444);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no connnect");
            System.exit(1);
        }

        MessageForNameMake message = new MessageForNameMake();
        message.setName("Ваня");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        MessageForNameMake messageSer = new MessageForNameMake();



        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            messageSer = (MessageForNameMake) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(messageSer.isAnsver());
    }
}
