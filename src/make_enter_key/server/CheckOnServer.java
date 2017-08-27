package make_enter_key.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by Slavik on 08.12.2016.
 */
public class CheckOnServer implements Serializable{
    Socket socket;
    public synchronized boolean letsChek (String srting){
        try {
            socket = new Socket("127.0.0.1", 4444);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no connnect");
            System.exit(1);
        }

        MessageForNameMake message = new MessageForNameMake();
        message.setName(srting);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        MessageForNameMake messageSer = new MessageForNameMake();

        boolean a = true;
        while (a == true)
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            messageSer = (MessageForNameMake) ois.readObject();
            a=false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(messageSer.isAnsver());
        return messageSer.isAnsver();
    }
}
