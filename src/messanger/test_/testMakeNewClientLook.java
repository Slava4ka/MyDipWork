package messanger.test_;

import messanger.ParamForEnter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by Slavik on 01.12.2016.
 */
public class testMakeNewClientLook extends JFrame implements Serializable {

    public static void main(String[] args) throws ClassNotFoundException {

        String adress = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ClientPart", "diplom");
        fileopen.setFileFilter(filter);
        fileopen.setCurrentDirectory(new File("C:\\Users\\Slavik\\IdeaProjects\\TTTT"));
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            adress = file.getPath();
            System.out.println(adress);
            System.out.println(file.getName());
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(adress));
            ParamForEnter paramForEnter = (ParamForEnter) objectInputStream.readObject();
            System.out.println("Имя пользователя " + paramForEnter.getName());
            paramForEnter.getFriends().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
