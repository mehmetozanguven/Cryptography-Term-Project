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
        if (exponent.compareTo(BigInteger.ZERO) == 0)
            return BigInteger.ONE;
        else if ((exponent.remainder(BigInteger.valueOf(2))).compareTo(BigInteger.ZERO) == 0){
            BigInteger nResult = calculateModularWithFastExponentiation(base, exponent.divide(BigInteger.valueOf(2)), modulus);
            return (nResult.multiply(nResult)).remainder(modulus);
        }else{
            return ((base.remainder(modulus)).multiply(calculateModularWithFastExponentiation(base, exponent.subtract(BigInteger.ONE),modulus))).remainder(modulus);
        }
    }

}
