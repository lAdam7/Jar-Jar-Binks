package JarJarBinks;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Password {
    private final String Algorithm = "PBKDF2WithHmacSHA1";
    private final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final SecureRandom random;
    private final byte[] salt;
    private final int iterations;
    private final int byteSize;

    public Password() {
        salt = new byte[24];
        random = new SecureRandom();
        iterations = 2500;
        byteSize = 128;
    }

    public String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        random.nextBytes(salt);
        byte[] passwordHash = pbkdf2(password.toCharArray(), salt, iterations, byteSize);
        //format - salt:hash:iterations
        return toHexString(salt) + ':' + toHexString(passwordHash) + ':' + iterations;
    }

    public Boolean validatePassword(String password, String correctHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int offset = 0;
        String[] corHash = correctHash.split(":");
        byte[] pasSalt = toByteArray(corHash[0]);
        int iter = Integer.parseInt(corHash[2]);
        byte[] corHashByte = toByteArray(corHash[1]);
        byte[] pasHash = pbkdf2(password.toCharArray(), pasSalt, iter, corHash[1].length());
        for (int i = 0; i < pasHash.length && i < corHashByte.length; i++) {
            offset += pasHash[i] ^ corHashByte[i];
        }
        return offset == 0;
    }

    public String generatePassword(int length) {
        final String passCharacters = "0123456789abcdefghijklmnopqrstuvwxzyABCDEFGHIJKLMNOPQRSTUVWXYZ?!+-_&*";
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < length; i++)
            pass.append(passCharacters.charAt(random.nextInt(passCharacters.length())));
        return pass.toString();
    }

    private byte[] pbkdf2(char[] password, byte[] salt, int iterations, int byteSize) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, byteSize * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(Algorithm);
        return factory.generateSecret(spec).getEncoded();
    }

    private String toHexString(byte[] bytes) {
        int v;
        char[] hexSymbols = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            v = bytes[i] & 0xFF;
            hexSymbols[i*2] = hexChars[v/16];
            hexSymbols[i*2+1] = hexChars[v%16];
        }
        return new String(hexSymbols);
    }

    private byte[] toByteArray(String hexString) {
        char[] hexCharString = hexString.toCharArray();
        byte[] bytes = new byte[hexString.length()/2];
        int v;
        for(int i = 0; i < hexString.length(); i+=2) {
            v = 16 * java.util.Arrays.binarySearch(hexChars, hexCharString[i]);
            v += java.util.Arrays.binarySearch(hexChars, hexCharString[i+1]);
            bytes[i/2] = (byte)v;
        }
        return bytes;
    }
}
