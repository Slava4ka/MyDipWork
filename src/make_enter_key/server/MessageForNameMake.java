package make_enter_key.server;

import java.io.Serializable;

/**
 * Created by Slavik on 07.12.2016.
 */
public class MessageForNameMake implements Serializable {
    private String name;

    public MessageForNameMake() {
    }

    private boolean ansver;

    public boolean isAnsver() {
        return ansver;
    }

    public void setAnsver(boolean ansver) {
        this.ansver = ansver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
