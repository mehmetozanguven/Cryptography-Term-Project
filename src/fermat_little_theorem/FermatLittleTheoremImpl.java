package fermat_little_theorem;

import greatest_common_divisor.GreatestCommonDivisorCalculator;

import java.math.BigInteger;

/**
 * Fermat's Little Theorem implementation
 */
public class FermatLittleTheoremImpl implements FermatLittleTheorem{

    // Result will be hold in this variable
    private BigInteger fltResult;

    /**
     * Fermat's Little theorem calculation
     * If the module number is prime and also satisfies the GreatestCommonDivisor calculation
     *  then I can calculate the fermat's little theorem invoking getFermatLittleTheoremResult(...) method
     * else, I just assign the result to zero
     * @param base
     * @param exponent
     * @param modulesPrimeNum
     */
    @Override
    public void calculateFermatLittleTheorem(BigInteger base, BigInteger exponent, BigInteger modulesPrimeNum){

        if (modulesPrimeNum.isProbablePrime(1) && GreatestCommonDivisorCalculator.calculateGreatestCommonDivisor(base, modulesPrimeNum).compareTo(BigInteger.ONE) != 1){
            this.fltResult = getFermatLittleTheoremResult(base, exponent, modulesPrimeNum);
        }else{
            this.fltResult = BigInteger.ZERO;
        }
    }

    /**
     * After running method calculateFermatLittleTheorem(BigInteger base, BigInteger exponent, BigInteger modulesPrimeNum)
     * method, result can be invoked by this method
     * @return
     */
    @Override
    public BigInteger getFermatLittleTheoremResult() {
        return fltResult;
    }


    /**
     * Fermat Little theorem implementation
     * Explanation with an example: calculate 2^35 = ? (mod 7) where p = 7, a = 2, exponent = 35
     *      1. Find the p-1 = 7-1 = 6
     *      2. Find the remainder of (35 % 6) which is 5
     *      3. Then according to the Fermat Little Theorem:
     *          2^35(mod7) = 2^5(mod7) - calculate this value and return it -
     *
     * Detail explanation for above example
     *      p-1 = 6,
     *      remainder (35 % 6) = 5
     *      quotient (35 / 6) = 5
     *      now I can write 35 = 6*5+5, then question becomes:
     *      = 2^(6*5+5)mod 7
     *      = (2^(6*5) * 2^5)mod 7
     *      = (32^6 * 2^5)mod 7
     *      according to FLT 32^6 mod 7 = 1, then
     *      = (1^6 * 2^5) mod 7
     *      = 32 mod 7
     *      = 4
     * @param base
     * @param exponent
     * @param modulusPrimeNum
     * @return
     */
    private BigInteger getFermatLittleTheoremResult(BigInteger base, BigInteger exponent, BigInteger modulusPrimeNum){
        BigInteger p_1 = modulusPrimeNum.subtract(BigInteger.ONE);

        BigInteger remainder = exponent.remainder(p_1);

        BigInteger newPowerOfa = base.pow(remainder.intValue());
        return newPowerOfa.mod(modulusPrimeNum);
    }

}
