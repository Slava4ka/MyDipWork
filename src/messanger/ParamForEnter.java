package messanger;

import java.io.Serializable;
import java.util.ArrayList;
//Объект в котором хранится список друзей и имя юзера

/**
 * Created by Slavik on 01.12.2016.
 */
public class ParamForEnter implements Serializable {
    String p;

    String name;

    ArrayList<String> friends = new ArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
