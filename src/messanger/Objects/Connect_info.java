package messanger.Objects;

import java.net.Socket;
import java.util.ArrayList;
// Это хранится у сервера

/**
 * Created by Slavik on 24.11.2016.
 */
public class Connect_info {
    private String name;
    private String adress;
    private Socket clientSocket;
    private ArrayList<Friend> clientsFriendsList;

    public Connect_info() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Socket getclientSocket() {
        return clientSocket;
    }

    public void setclientSocket(Socket socket) {
        this.clientSocket = socket;
    }

    public ArrayList<Friend> getClientsFriendsList() {
        return clientsFriendsList;
    }

    public void setClientsFriendsList(ArrayList<Friend> clientsFriendsList) {
        this.clientsFriendsList = clientsFriendsList;
    }
}
