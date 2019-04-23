package greatest_common_divisor;

import extended_euclidean_algorithm.ExtendedEuclideanAlgorithm;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithmImpl;

import java.math.BigInteger;

public class GreatestCommonDivisorCalculator {
    private static ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm = new ExtendedEuclideanAlgorithmImpl();

    /**
     * Calculate the gcd for 2 numbers
     * I am using extended euclidean algorithm for that method
     * eea formula is also the gcd for 2 numbers
     * @param num1
     * @param num2
     * @return
     */
    public static BigInteger calculateGreatestCommonDivisor(BigInteger num1, BigInteger num2){
        extendedEuclideanAlgorithm.calculateExtendedEuclideanAlgorithm(num1, num2);
        return extendedEuclideanAlgorithm.getExtendedEuclideanFormulaResult();
    }
}
