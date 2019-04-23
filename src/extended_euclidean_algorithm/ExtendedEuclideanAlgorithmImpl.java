package extended_euclidean_algorithm;

import extended_euclidean_algorithm.extended_eucliden_formula.EEAFormula;
import extended_euclidean_algorithm.extended_eucliden_formula.EEAFormulaImpl;

import java.math.BigInteger;

public class ExtendedEuclideanAlgorithmImpl implements ExtendedEuclideanAlgorithm{
    private EEAFormula prevFormula;
    private EEAFormula prevPrevFormula;

    public void calculateExtendedEuclideanAlgorithm(BigInteger r0, BigInteger r1){
        BigInteger s0 = BigInteger.valueOf(1);
        BigInteger s1 = BigInteger.valueOf(0);
        BigInteger t0 = BigInteger.valueOf(0);
        BigInteger t1 = BigInteger.valueOf(1);
        BigInteger original_r0 = r0;
        BigInteger original_r1 = r1;

        prevFormula = new EEAFormulaImpl(s1, r0, t1, r1);
        prevPrevFormula = new EEAFormulaImpl(s0, r0, t0, r1);

        BigInteger remainder = r0.remainder(r1);
        BigInteger quotient = r0.divide(r1);

        while (remainder.compareTo(BigInteger.ZERO) != 0){
            BigInteger newCoeffcient_s = prevPrevFormula.getCoefficient_s().subtract(quotient.multiply(prevFormula.getCoefficient_s()));
            BigInteger newCoeffcient_t = prevPrevFormula.getCoefficient_t().subtract(quotient.multiply(prevFormula.getCoefficient_t()));
            prevPrevFormula = prevFormula;
            prevFormula = new EEAFormulaImpl(newCoeffcient_s, original_r0, newCoeffcient_t, original_r1);

            BigInteger newRemainder = r1.remainder(remainder);
            quotient = r1.divide(remainder);

            r1 = remainder;
            remainder = newRemainder;

        }
    }

    /**
     * After running method calculateExtendedEuclideanAlgorithm(BigInteger r0, BigInteger r1)
     * call this method to see extended euclidean formula in string format.
     * @return
     */
    public String getEuclideanAlgorithmInString(){
        return this.prevFormula.toString();
    }

    /**
     * After running method calculateExtendedEuclideanAlgorithm(BigInteger r0, BigInteger r1)
     * call this method to see formula result which is gcd
     * @return
     */
    public BigInteger getExtendedEuclideanFormulaResult(){
        return this.prevFormula.getExtendedEuclideanFormulaResult();
    }


    public static void main(String[] args) {
        ExtendedEuclideanAlgorithmImpl a = new ExtendedEuclideanAlgorithmImpl();
        a.calculateExtendedEuclideanAlgorithm(BigInteger.valueOf(973), BigInteger.valueOf(301));
        System.out.println(a.getEuclideanAlgorithmInString());
        a.calculateExtendedEuclideanAlgorithm(BigInteger.valueOf(3), BigInteger.valueOf(5));
        System.out.println(a.getEuclideanAlgorithmInString());
    }
}
