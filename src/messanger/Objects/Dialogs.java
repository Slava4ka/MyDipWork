package messanger.Objects;

import java.util.ArrayList;
//Это диалог, хранится у клиента в Коллекции

/**
 * Created by Slavik on 28.11.2016.
 */
public class Dialogs {

    String name;
    boolean status;
    ArrayList<String> dialogListl;

    public ArrayList<String> getDialogListl() {
        return dialogListl;
    }

    public void setDialogListl(ArrayList<String> dialogListl) {
        this.dialogListl = dialogListl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
