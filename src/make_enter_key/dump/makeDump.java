package make_enter_key.dump;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Slavik on 08.12.2016.
 */
public class makeDump implements Serializable {
    public static void main(String[] args) {
        Dump dump = new Dump();

        try {
            ObjectOutputStream objectOutputStream = null;
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("ClientsDump.diplom"));
            objectOutputStream.writeObject(dump);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
