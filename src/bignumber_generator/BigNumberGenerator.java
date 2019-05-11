package bignumber_generator;


import java.math.BigInteger;
import java.util.Random;

/**
 * This class is responsible for generating random numbers
 */
public class BigNumberGenerator {

    public BigNumberGenerator(){
    }

    /**
     * Generates a probably big prime integer with specific bit numbers
     * @param bitsNumber
     * @return the big prime integers
     */
    public BigInteger generateBigPrimeIntegersWithNumberOfBits(int bitsNumber){
        boolean isPrimeNumberGenerated = false;
        BigInteger primeNum_p = BigInteger.ZERO;
        while (!isPrimeNumberGenerated){
            Random rnd = new Random();
            BigInteger primeNumCandidate = new BigInteger(bitsNumber, rnd);
            if (primeNumCandidate.isProbablePrime(1)){
                primeNum_p = primeNumCandidate;
                isPrimeNumberGenerated = true;
            }
        }
        return primeNum_p;
    }

    /**
     * Generates a big integer with specific bit numbers
     * @param bitsNumber
     * @return the big integer
     */
    public BigInteger generateBigIntegersWithNumberOfBits(int bitsNumber){
        Random rnd = new Random();
        BigInteger bigInteger = new BigInteger(bitsNumber, rnd);
        return bigInteger;
    }

    public BigInteger generateBigIntegersWithUpperLimit(BigInteger upperLimit){
        Random random = new Random();
        BigInteger bigInteger = new BigInteger(upperLimit.bitLength(), random);
        return bigInteger;
    }
}
