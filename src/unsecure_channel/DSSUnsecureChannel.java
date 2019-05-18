package unsecure_channel;

import java.math.BigInteger;
import java.util.Map;

public interface DSSUnsecureChannel {

    void setSenderDSAPublicKeyComponent(BigInteger[] senderDSAPublicKeyComponent);

    void setSenderDSASignParameter(BigInteger[] senderDSASignParameter);

    void setSenderMessage(String senderMessage);

    BigInteger[] getSenderDSAPublicKeyComponent();

    BigInteger[] getSenderDSASignParameter();

    String getSenderMessage();

    void setReceiverDSAPublicKeyComponent(BigInteger[] receiverDSAPublicKeyComponent);

    void setReceiverDSASignParameter(BigInteger[] receiverDSASignParameter);

    void setReceiverMessage(String receiverMessage);

    BigInteger[] getReceiverDSAPublicKeyComponent();

    BigInteger[] getReceiverDSASignParameter();

    String getReceiverMessage();
}
