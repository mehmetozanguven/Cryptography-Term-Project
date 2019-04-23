package fermat_little_theorem;

import java.math.BigInteger;

public interface FermatLittleTheorem {

    void calculateFermatLittleTheorem(BigInteger base, BigInteger exponent, BigInteger modulesPrimeNum);

    BigInteger getFermatLittleTheoremResult();
}
