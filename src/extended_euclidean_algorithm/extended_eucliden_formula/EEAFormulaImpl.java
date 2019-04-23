package extended_euclidean_algorithm.extended_eucliden_formula;

import java.math.BigInteger;

public class EEAFormulaImpl implements EEAFormula {

    private BigInteger coefficient_s;
    private BigInteger r0;

    private BigInteger coefficient_t;
    private BigInteger r1;


    public EEAFormulaImpl(BigInteger coefficient_s, BigInteger r0, BigInteger coefficient_t, BigInteger r1) {
        this.coefficient_s = coefficient_s;
        this.r0 = r0;
        this.coefficient_t = coefficient_t;
        this.r1 = r1;
    }


    public BigInteger getCoefficient_s() {
        return coefficient_s;
    }

    public BigInteger getExtendedEuclideanFormulaResult(){

        return (coefficient_s.multiply(r0).add(coefficient_t.multiply(r1)));
    }

    public void setCoefficient_s(BigInteger coefficient_s) {
        this.coefficient_s = coefficient_s;
    }

    public BigInteger getR0() {
        return r0;
    }

    public void setR0(BigInteger r0) {
        this.r0 = r0;
    }

    public BigInteger getCoefficient_t() {
        return coefficient_t;
    }

    public void setCoefficient_t(BigInteger coefficient_t) {
        this.coefficient_t = coefficient_t;
    }

    public BigInteger getR1() {
        return r1;
    }

    public void setR1(BigInteger r1) {
        this.r1 = r1;
    }

    public String toString() {
        return "Extended euclidean algorithm formula: (" + coefficient_s + ") * " + r0
        + " + (" + coefficient_t + ") * " + r1 + " = " + getExtendedEuclideanFormulaResult();
    }
}
