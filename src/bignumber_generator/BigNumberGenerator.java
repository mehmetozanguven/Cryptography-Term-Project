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

    public BigInteger bigIntInRange(BigInteger min, BigInteger max){
        Random rnd = new Random();

        BigInteger x = new BigInteger(max.bitLength(), rnd);

        //while x <= min || x >= max
        //So far while testing I've only seen a max of 4 iterations of the loop.
        //Generally it's either 0 or 1 iterations
        while (x.compareTo(min) <= 0 || x.compareTo(max) >= 0){
            x = new BigInteger(max.bitLength(), rnd);
        }
        return x;
    }
}
