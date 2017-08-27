package messanger.DiffieHellman;

import java.math.BigInteger;

public class CipherMessage {

    private  String convertBigIntegerToString(BigInteger value) {
        return value.toString();
    }
     
    private  byte[] convertStringToBytes(String value) {
        return value.getBytes();
    }
     
    private  String convertStringToHexString(String value) {
        return new BigInteger(value).toString(16);
    }
     
    public  String convertBigIntegerToBase64HexString(BigInteger value) {
        String valueAsString = convertBigIntegerToString(value);
        String valueAsHexString = convertStringToHexString(valueAsString);
        Base64 b = new Base64();
        return b.encode(convertStringToBytes(valueAsHexString));
    }
     
    private  String convertByteArrayToString(byte[] byteArray) {
        return new String(byteArray);
    }
     
    private  BigInteger convertStringToBigInteger(String value, int base) {
        return new BigInteger(value, base);
    }
     
    public  BigInteger convertBase64HexStringToBigInteger(String base64HexString) {
        Base64 b = new Base64();
        byte[] decodedStringAsByteArrayOfHex = b.decode(base64HexString);
        String valueAsHex = convertByteArrayToString(decodedStringAsByteArrayOfHex);
        return convertStringToBigInteger(valueAsHex, 16);
    }
}
