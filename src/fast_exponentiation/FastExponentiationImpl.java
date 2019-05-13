package fast_exponentiation;


import java.math.BigInteger;

public class FastExponentiationImpl implements FastExponentiation{

    /**
     * Fast exponentiation algorithm implementation
     * @param base
     * @param exponent
     * @param modulus
     * @return
     */
    @Override
    public BigInteger calculateModularWithFastExponentiation(BigInteger base, BigInteger exponent, BigInteger modulus){
        BigInteger y = new BigInteger("1");
        for(int i = 0; i < exponent.bitLength(); i++){
            if (exponent.testBit(i)) {
                y = y.multiply(base);
                y = y.mod(modulus);
            }
            base = base.multiply(base);
            base = base.mod(modulus);
        }
        return y;
    }

}
