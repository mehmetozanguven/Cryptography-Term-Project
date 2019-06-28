package sha_hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAHash {

    private SHAHash(){}

    /**
     * Hash the message and return as byte array
     * @param message that will be hashed
     * @return
     */
    public static byte[] sha_256_hash_with(String message){
        MessageDigest digest = null;
        byte[] messageToByte = message.getBytes();

        byte[] encodedhash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(message.getBytes(),0,message.length());
            encodedhash = digest.digest(
                    messageToByte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodedhash;
    }

}
