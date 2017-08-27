package messanger.DiffieHellman;

import messanger.MessageType;
import messanger.Objects.Message;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.util.Arrays;

public class SideB {
    private byte[] hashSecretKey = null;

    public byte[] getHashSecretKey() {
        return hashSecretKey;
    }

    public void showHashSecretKey() {
        for (int i = 0; i < hashSecretKey.length; i++) {
            System.out.print(hashSecretKey[i] + " ");
        }
        System.out.println();
    }

    public synchronized void generateSecretKey(Socket socket, Message message) {
        CipherMessage cipherMessage = new CipherMessage();

        BigInteger p = cipherMessage.convertBase64HexStringToBigInteger(message.getpAsValueAsHexBase64());
        BigInteger g = cipherMessage.convertBase64HexStringToBigInteger(message.getgAsValueAsHexBase64());
        BigInteger y = cipherMessage.convertBase64HexStringToBigInteger(message.getyAsValueAsHexBase64());
        PublicKey publicKey = TransportPublicKey.getKey(null, y, p, g, 0);
        PublicKey key = null;
        try {
            DHParameterSpec dhParams = new DHParameterSpec(p, g);
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DiffieHellman");

            keyGen.initialize(dhParams, new SecureRandom());

            KeyAgreement keyAgree = KeyAgreement.getInstance("DiffieHellman");
            KeyPair pair = keyGen.generateKeyPair();

            keyAgree.init(pair.getPrivate());
            key = pair.getPublic();

            keyAgree.doPhase(publicKey, true);

            byte[] secretKey = keyAgree.generateSecret();

            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            hashSecretKey = hash.digest(secretKey);
            hashSecretKey = Arrays.copyOf(hashSecretKey, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String text = cipherMessage.convertBigIntegerToBase64HexString(((DHPublicKey) key).getY());

        Message messageToA = new Message();
        messageToA.setInsructions(MessageType.GET_KEY_SIDE_B); // тут ихменится
        messageToA.setbKeyAsHexStringBase64(text);
        messageToA.setAdress(message.getName());
        messageToA.setName(message.getAdress());

        try {
            System.out.println("Отправил сообщение с ключом в ответ");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(messageToA);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
