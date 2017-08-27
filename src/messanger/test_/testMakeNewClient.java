package messanger.test_;

import messanger.ParamForEnter;

import java.io.*;

/**
 * Created by Slavik on 01.12.2016.
 */
public class testMakeNewClient implements Serializable {
    public static void main(String[] args) {
        ParamForEnter paramForEnter = new ParamForEnter();
      /*  paramForEnter.setName("Петя");
        paramForEnter.getFriends().add("Маша");
        paramForEnter.getFriends().add("Дима");
        paramForEnter.getFriends().add("Дана");
        paramForEnter.getFriends().add("Паша");
        paramForEnter.getFriends().add("Ваша");*/

     /*   paramForEnter.setName("Маша");
        paramForEnter.getFriends().add("Петя");
        paramForEnter.getFriends().add("Оля");
        paramForEnter.getFriends().add("Таня");
        paramForEnter.getFriends().add("Дима");
        paramForEnter.getFriends().add("Света");*/

        paramForEnter.setName("Рита");
        paramForEnter.getFriends().add("Петя");
        paramForEnter.getFriends().add("Маша");
        paramForEnter.getFriends().add("Слава");
        paramForEnter.getFriends().add("Света");

        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Client"+paramForEnter.getName()+".diplom"));
            objectOutputStream.writeObject(paramForEnter);
            objectOutputStream.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("Client"+paramForEnter.getName()+".diplom"));
            ParamForEnter a = (ParamForEnter) objectInputStream.readObject();
            System.out.println("Имя юзера "+a.getName());
            a.getFriends().forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
