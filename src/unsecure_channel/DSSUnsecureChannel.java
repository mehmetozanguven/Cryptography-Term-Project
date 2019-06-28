package unsecure_channel;

import java.math.BigInteger;
import java.util.Map;

public interface DSSUnsecureChannel {

    void setSenderDSAPublicKeyComponent(BigInteger[] senderDSAPublicKeyComponent);

    void setSenderDSASignParameter(BigInteger[] senderDSASignParameter);


    BigInteger[] getSenderDSAPublicKeyComponent();

    BigInteger[] getSenderDSASignParameter();


    void setReceiverDSAPublicKeyComponent(BigInteger[] receiverDSAPublicKeyComponent);

    void setReceiverDSASignParameter(BigInteger[] receiverDSASignParameter);


    BigInteger[] getReceiverDSAPublicKeyComponent();

    BigInteger[] getReceiverDSASignParameter();

}
