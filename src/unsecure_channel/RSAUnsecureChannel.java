package unsecure_channel;

import java.math.BigInteger;
import java.util.Map;

public interface RSAUnsecureChannel {

    Map<BigInteger, BigInteger> getSenderPublicKey();

    void setSenderPublicKeyPair(Map<BigInteger, BigInteger> senderPublicKey);

    Map<BigInteger, BigInteger> getReceiverPublicKey();

    void setReceiverPublicKeyPair(Map<BigInteger, BigInteger> receiverPublicKey);

    BigInteger getSenderRandomDESWithRSAEncryption();

    void setSenderRandomDESWithRSAEncryption(BigInteger senderRandomDESWithRSAEncryption);

    BigInteger getReceiverRandomDESWithRSAEncryption();

    void setReceiverRandomDESWithRSAEncryption(BigInteger receiverRandomDESWithRSAEncryption);

}
