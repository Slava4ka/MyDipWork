package make_enter_key.dump;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Slavik on 08.12.2016.
 */
public class Dump implements Serializable{
    private ArrayList<String> names = new ArrayList();

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
}
