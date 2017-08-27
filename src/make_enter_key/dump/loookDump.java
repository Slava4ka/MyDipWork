package make_enter_key.dump;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by Slavik on 08.12.2016.
 */
public class loookDump {
    public static void main(String[] args) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ClientsDump.diplom"));
            Dump dump = (Dump) objectInputStream.readObject();
            System.out.println("Ok");
            dump.getNames().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
