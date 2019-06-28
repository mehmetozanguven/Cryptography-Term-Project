package extended_euclidean_algorithm;

import java.math.BigInteger;

public interface ExtendedEuclideanAlgorithm {

    void calculateExtendedEuclideanAlgorithm(BigInteger r0, BigInteger r1);

    String getEuclideanAlgorithmInString();

    BigInteger getExtendedEuclideanFormulaResult();

    BigInteger getCoefficientOfR0();

    BigInteger getCoefficientOfR1();
}
