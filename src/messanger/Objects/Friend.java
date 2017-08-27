package messanger.Objects;

import java.io.Serializable;

// Это объект класса фрэнд. Он представляет собой друга, хранящегося в коллекции друзей в входном файле
// friend status - если онлайн, то тру фал - оффлайн

public class Friend implements Serializable {
    private String friendName;
    private boolean friendStatus = false;

    public Friend() {
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public boolean isFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(boolean friendStatus) {
        this.friendStatus = friendStatus;
    }
}
