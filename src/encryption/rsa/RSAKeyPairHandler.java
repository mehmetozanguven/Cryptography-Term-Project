package encryption.rsa;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;

/**
 * This util class is for RSA encryption
 * This util class is to get keypair element from the hash map
 * instead of doing in the concrete class directly.
 */
public class RSAKeyPairHandler {
    private Map<BigInteger, BigInteger> keyPair;
    private BigInteger key;
    private BigInteger value;

    /**
     * Generates instance of this class with any keyPair
     * then find its number N and number d/e
     * @param keyPair
     */
    public RSAKeyPairHandler(Map keyPair){
        this.keyPair = keyPair;
        findKeyAndValue();
    }

    private void findKeyAndValue() {

        Iterator iterator = keyPair.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<BigInteger, BigInteger> pair = (Map.Entry) iterator.next();
            key = pair.getKey();
            value = pair.getValue();
        }
    }

    public BigInteger getKey() {
        return key;
    }

    public BigInteger getValue() {
        return value;
    }
}
