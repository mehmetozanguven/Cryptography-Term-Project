package diffie_helman;

import java.math.BigInteger;

public interface DiffieHellman {

    BigInteger publishPrimeNumberToUnsecureChannel();

    BigInteger publishAnyNumberAlphaToUnsecureChannel();
}
