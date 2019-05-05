package encryption.rsa;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;

public class RSAKeyPairHandler {
    private Map<BigInteger, BigInteger> keyPair;
    private BigInteger key;
    private BigInteger value;

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
