package messanger.Objects;

import messanger.MessageType;

import java.io.Serializable;
import java.util.ArrayList;

//Это само соообщение, ктоторое передается
public class Message implements Serializable {
    private String bKeyAsHexStringBase64; // Это для диффи хеллмана
    private String pAsValueAsHexBase64;
    private String gAsValueAsHexBase64;
    private String yAsValueAsHexBase64;

    public void setyAsValueAsHexBase64(String yAsValueAsHexBase64) {
        this.yAsValueAsHexBase64 = yAsValueAsHexBase64;
    }

    public String getbKeyAsHexStringBase64() {
        return bKeyAsHexStringBase64;
    }

    public void setbKeyAsHexStringBase64(String bKeyAsHexStringBase64) {
        this.bKeyAsHexStringBase64 = bKeyAsHexStringBase64;
    }

    public String getpAsValueAsHexBase64() {
        return pAsValueAsHexBase64;
    }

    public void setpAsValueAsHexBase64(String pAsValueAsHexBase64) {
        this.pAsValueAsHexBase64 = pAsValueAsHexBase64;
    }

    public String getgAsValueAsHexBase64() {
        return gAsValueAsHexBase64;
    }

    public void setgAsValueAsHexBase64(String gAsValueAsHexBase64) {
        this.gAsValueAsHexBase64 = gAsValueAsHexBase64;
    }

    public String getyAsValueAsHexBase64() {
        return yAsValueAsHexBase64;
    }

    private String name;
    private String data;
    private String adress;
    private String[] list; // нужно для отправки сервером имен клиентов онлайн, для добавления в список
    // типы содержащихся данных в пересылаемом объекте содержатся в коллекции Instructions
    private ArrayList<MessageType> Insructions = new ArrayList();
    // Список друзей клиента
    private ArrayList<Friend> FriendsList = new ArrayList();

    //private String key;

    public ArrayList<Friend> getFriendsList() {
        return FriendsList;
    }

    public void setFriendsList(ArrayList<Friend> friendsList) {
        FriendsList = friendsList;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public Message() {
    }

    public ArrayList getInsructions() {
        return Insructions;
    }

    public void setInsructions(MessageType type1) {
        Insructions.add(type1);
    }

    public void setInsructions(MessageType type1, MessageType type2) {
        Insructions.add(type1);
        Insructions.add(type2);
    }

    public void setInsructions(MessageType type1, MessageType type2, MessageType type3) {
        Insructions.add(type1);
        Insructions.add(type2);
        Insructions.add(type3);
    }

    public void setInsructions(MessageType type1, MessageType type2, MessageType type3, MessageType type4) {
        Insructions.add(type1);
        Insructions.add(type2);
        Insructions.add(type3);
        Insructions.add(type4);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String sms) {
        this.data = sms;
    }
}
