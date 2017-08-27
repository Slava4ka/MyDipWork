package make_enter_key.dump;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Slavik on 08.12.2016.
 */
public class deleteAll {
    public static void main(String[] args) {
        Dump dump = new Dump();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ClientsDump.diplom"));
            dump = (Dump) objectInputStream.readObject();
            System.out.println("dump is read");
            //dump.getNames().add(s);

            dump.getNames().clear();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("ClientsDump.diplom"));
            objectOutputStream.writeObject(dump);
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
