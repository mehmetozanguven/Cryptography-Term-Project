package unsecure_channel;

import java.math.BigInteger;
import java.util.Map;

public interface DSSUnsecureChannel {

    Map<Integer, BigInteger> getSenderPublicKeyComponents();

    void setSenderPublicKeyComponents(Map<Integer, BigInteger> senderPublicKeyComponents);

    Map<Integer, BigInteger> getSenderSignaturePair();

    void setSenderSignaturePair(Map<Integer, BigInteger> senderSignaturePair);

    Map<Integer, BigInteger> getReceiverPublicKeyComponents();

    void setReceiverPublicKeyComponents(Map<Integer, BigInteger> receiverPublicKeyComponents);

    Map<Integer, BigInteger> getReceiverSignaturePair();

    void setReceiverSignaturePair(Map<Integer, BigInteger> receiverSignaturePair);


}
