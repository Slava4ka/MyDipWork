package messanger.test_;

public class test {
    public String getYt() {
        return yt;
    }

    public void setYt(String yt) {
        this.yt = yt;
    }

    String yt;

    public static void main(String[] args) {

/*
        int n = JOptionPane.showConfirmDialog(
                null,
                "В данный момент сервер не доступен!\n" +
                        "Хотите ли вы повторить попытку соединения?",
                "Ошибка соединения с сервером",
                JOptionPane.YES_NO_OPTION);
        System.out.println("n = " + n);
        if (n == 1 || n == -1) {
            System.exit(1);
            frame.despose();
        } else {
            setUpNetworking();
        }
*/

   /*     Icon icon = new ImageIcon();
        String [] e = {"ham", "spam", "yam", "asd", "tor Online"};

        String [] g = new String[e.length];

        for(int i=0; i<g.length; i++){
            g[i] = e[i].replaceAll(" Online", "").trim();
        }

        Object[] possibilities = g;
        String s = (String)JOptionPane.showInputDialog(
                null,
                "Complete the sentence:\n"
                        + "\"Green eggs and...\"",
                "Customized Dialog",
                JOptionPane.INFORMATION_MESSAGE,
                icon,
                possibilities,
                "ham");

//If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            JOptionPane.showMessageDialog(null, "Green eggs and... " + s + "!");
        }*/
       /* String test1 = JOptionPane.showInputDialog("Please input mark for test 1: ", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(test1);*/
    }
}
