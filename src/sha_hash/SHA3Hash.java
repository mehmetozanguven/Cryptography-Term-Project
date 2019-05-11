package sha_hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA3Hash {

    private SHA3Hash(){}

    public static String sha3_256_hash_with(String message){
        MessageDigest digest = null;
        String hashedString = null;
        try {
            digest = MessageDigest.getInstance("SHA3-256");
            byte[] encodedhash = digest.digest(
                    message.getBytes(StandardCharsets.UTF_8));
            hashedString = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedString;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
