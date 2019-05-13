package person.dss_signature_person;

import bignumber_generator.BigNumberGenerator;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithm;
import fast_exponentiation.FastExponentiation;
import sha_hash.SHA3Hash;

import java.math.BigInteger;

public abstract class PersonElGamal {
    private final int LARGEPRIME_P_LENGHT = 10;
    private BigNumberGenerator bigNumberGenerator;
    private FastExponentiation fastExponentiation;
    private ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm;

    private BigInteger largePrime_p;
    private BigInteger number_a;
    private BigInteger[] publicKeyComponents;
    private BigInteger privateKey;
    private BigInteger[] signatureParameters;


    public PersonElGamal(BigNumberGenerator bigNumberGenerator, FastExponentiation fastExponentiation, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm){
        this.bigNumberGenerator = bigNumberGenerator;
        this.fastExponentiation = fastExponentiation;
        this.extendedEuclideanAlgorithm = extendedEuclideanAlgorithm;
        signatureParameters = new BigInteger[2];
    }


    public BigInteger[] getPublicKeyComponents() {
        return publicKeyComponents;
    }

    public BigInteger[] getSignatureParameters() {
        return signatureParameters;
    }

    public void elGamalKeyGeneration(){
        largePrime_p = computeLargePrimeP();
        number_a = computePrimitiveNumber_a();
        privateKey = computePrivateKey();
        publicKeyComponents = computePublicKeyComponents();

    }

    public void signTheMessage(String message){
        byte[] hashedMessageByte = SHA3Hash.sha3_256_hash_with(message);

        BigInteger hashedBigInteger = new BigInteger(hashedMessageByte);
        BigInteger largePrime_p_minus_1 = largePrime_p.subtract(BigInteger.ONE);
        hashedBigInteger = hashedBigInteger.mod(largePrime_p_minus_1);

        BigInteger number_k = computeNumber_k();

        BigInteger s1 = fastExponentiation.calculateModularWithFastExponentiation(number_a, number_k, largePrime_p);

        BigInteger inverseOf_k = number_k.modInverse(largePrime_p_minus_1);

        BigInteger calculate_m_xs1 = hashedBigInteger.subtract(privateKey.multiply(s1));

        BigInteger s2 = inverseOf_k.multiply(calculate_m_xs1).mod(largePrime_p_minus_1);

        signatureParameters[0] = s1;
        signatureParameters[1] = s2;
    }

    public void verifyTheMessage(BigInteger[] publicKeyComponents, BigInteger[] signatureParameters, String message){
        byte[] hashedMessageByte = SHA3Hash.sha3_256_hash_with(message);
        BigInteger hashedBigInteger = new BigInteger(hashedMessageByte);

        BigInteger number_p = publicKeyComponents[0];
        BigInteger number_a = publicKeyComponents[1];
        BigInteger publicKey = publicKeyComponents[2];

        BigInteger number_s1 = signatureParameters[0];
        BigInteger number_s2 = signatureParameters[1];

        BigInteger largePrime_p_minus_1 = number_p.subtract(BigInteger.ONE);
        hashedBigInteger = hashedBigInteger.mod(largePrime_p_minus_1);


        BigInteger v1 = fastExponentiation.calculateModularWithFastExponentiation(number_a, hashedBigInteger, number_p);

        BigInteger powerOfPublicKey_To_S1 = bigNumberGenerator.powBigInteger(publicKey, number_s1);
        BigInteger powerOfS1_To_S2 = bigNumberGenerator.powBigInteger(number_s1, number_s2);

        BigInteger v2 = powerOfPublicKey_To_S1.multiply(powerOfS1_To_S2).mod(number_p);

        if (v1.compareTo(v2) == 0){
            System.out.println("Valid");
        }else{
            System.out.println("invalid");
        }

    }

    private BigInteger computeNumber_k() {
        BigInteger min = BigInteger.ONE;
        BigInteger max = largePrime_p.subtract(BigInteger.ONE);

        BigInteger number_k = BigInteger.valueOf(-1);
        boolean isNumberFound = false;
        while (!isNumberFound){
            BigInteger candidate = bigNumberGenerator.bigIntInRange(min, max);
            extendedEuclideanAlgorithm.calculateExtendedEuclideanAlgorithm(candidate, max);
            if (extendedEuclideanAlgorithm.getExtendedEuclideanFormulaResult().compareTo(BigInteger.ONE) == 0){
                System.out.println(extendedEuclideanAlgorithm.getEuclideanAlgorithmInString());
                number_k = candidate;
                isNumberFound = true;
            }
        }
        return number_k;
    }

    private BigInteger[] computePublicKeyComponents() {
        BigInteger publicKey = fastExponentiation.calculateModularWithFastExponentiation(number_a, privateKey, largePrime_p);
        BigInteger[] arr = {largePrime_p, number_a, publicKey};
        return arr;
    }

    private BigInteger computePrivateKey() {
        BigInteger min = BigInteger.ONE;
        BigInteger max = largePrime_p.subtract(BigInteger.ONE);

        return bigNumberGenerator.bigIntInRange(min, max);
    }




    private BigInteger computePrimitiveNumber_a() {
        BigInteger randomInteger = bigNumberGenerator.generateBigIntegersWithNumberOfBits(LARGEPRIME_P_LENGHT);
        return randomInteger.mod(largePrime_p);
    }

    private BigInteger computeLargePrimeP() {
        return bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(LARGEPRIME_P_LENGHT);
    }



}
