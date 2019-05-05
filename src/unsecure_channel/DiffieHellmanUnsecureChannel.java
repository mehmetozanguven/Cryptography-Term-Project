package unsecure_channel;

import java.math.BigInteger;

public interface DiffieHellmanUnsecureChannel {

    void diffieHellmanSetup();

    BigInteger getPrimeNum_p();

    BigInteger getAnyNum_alpha();

    BigInteger getDiffieHellmanSenderPublicKey();

    void setDiffieHellmanSenderPublicKey(BigInteger diffieHellmanSenderPublicKey);

    BigInteger getDiffieHellmanReceiverPublicKey();

    void setDiffieHellmanReceiverPublicKey(BigInteger diffieHellmanReceiverPublicKey);
}
