package person.dss_signature_person;

import bignumber_generator.BigNumberGenerator;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithm;
import fast_exponentiation.FastExponentiation;
import signature.TestDSASignature;

import java.math.BigInteger;

public abstract class TestPerson {

    private BigNumberGenerator bigNumberGenerator;
    private FastExponentiation fastExponentiation;
    private ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm;
    private TestDSASignature testDSASignature;
    private BigInteger[] signParameter;
    private BigInteger[] publicParameter;

    public TestPerson(BigNumberGenerator bigNumberGenerator, FastExponentiation fastExponentiation, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm) {
        this.bigNumberGenerator = bigNumberGenerator;
        this.fastExponentiation = fastExponentiation;
        this.extendedEuclideanAlgorithm = extendedEuclideanAlgorithm;
        this.testDSASignature = new TestDSASignature();
    }

    public void DSAKeyGeneration(){
        testDSASignature.keyGeneration();
        publicParameter = testDSASignature.getPublicParameter();
    }

    public void signMessage(String message){
        testDSASignature.signMessage(message);
        signParameter =  testDSASignature.getSignParameter();
    }

    public BigInteger[] getSignParameter(){
       return signParameter;
    }

    public BigInteger[] getPublicParameter(){
        return publicParameter;
    }

    public void verifyMessage(String message, BigInteger[] signParameter, BigInteger[] publicParameter){
        testDSASignature.verifyMessage(message, signParameter, publicParameter);
    }


}
