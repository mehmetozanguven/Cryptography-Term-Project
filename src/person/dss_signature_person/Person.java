package person.dss_signature_person;

import bignumber_generator.BigNumberGenerator;
import fast_exponentiation.FastExponentiation;
import sha_hash.SHA3Hash;
import signature.DSASignature;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public abstract class Person {
    private final int bitNumsOfPrimeNumber_q = 160;
    private final int bitNumsOfLargePrimeNumber_p = 512;

    private BigNumberGenerator bigNumberGenerator;
    private FastExponentiation fastExponentiation;

    private BigInteger primeNumber_q;
    private BigInteger largePrimeNumber_p;
    private BigInteger number_l;
    private BigInteger number_g;
    private BigInteger randomPrivateKey_x;
    private BigInteger publicKey;
    private Map<Integer, BigInteger> publicKeyComponents;
    private Map<Integer, BigInteger> signaturePair;

    public Person(BigNumberGenerator bigNumberGenerator, FastExponentiation fastExponentiation){
        this.bigNumberGenerator = bigNumberGenerator;
        this.fastExponentiation = fastExponentiation;
        publicKeyComponents = new HashMap<>();
        computeNumber_l();
    }

    public void DSAKeyGeneration(){
        computePrimeNumber_q();
        computeLargePrimeNumber_p();
        computeNumber_g();
        computeRandomPrivateKey();
        computePublicKey();
    }

    public void signMessage(String message, DSASignature dsaSignature){
        BigInteger randomNumberK = computeRandomNumberK();
        dsaSignature.computeSignaturePair(publicKeyComponents, randomNumberK, message);
        signaturePair = dsaSignature.getSignaturePair();
    }

    public Map<Integer, BigInteger> getSignaturePair(){
        if (signaturePair == null){
            throw new NullPointerException("Signature pair is null");
        }else {
            return signaturePair;
        }
    }

    public void verificationTheMessage(Map<Integer, BigInteger> publicKeyComponents, Map<Integer, BigInteger> signaturePair, String message, DSASignature dsaSignature){
        BigInteger verificationNumber = dsaSignature.verificationTheMessage(publicKeyComponents, signaturePair, message);
        BigInteger number_r = signaturePair.get(0);
        if (verificationNumber.compareTo(number_r) == 0){
            System.out.println("Verified");
        }else{
            System.out.println("Not verified");
        }
    }

    public Map<Integer, BigInteger> getPublicKeyComponent(){
        publicKeyComponents.put(1, largePrimeNumber_p);
        publicKeyComponents.put(2, primeNumber_q);
        publicKeyComponents.put(3, number_g);
        publicKeyComponents.put(4, publicKey);

        return publicKeyComponents;
    }

    private BigInteger computeRandomNumberK() {
        boolean isRandomNumberKGenerated = false;
        BigInteger candidateNumberK = BigInteger.ONE;
        while (!isRandomNumberKGenerated){
            candidateNumberK = bigNumberGenerator.generateBigIntegersWithNumberOfBits(bitNumsOfPrimeNumber_q);
            if (candidateNumberK.compareTo(primeNumber_q) == -1){
                isRandomNumberKGenerated = true;
            }
        }
        return candidateNumberK;
    }

    private void computePrimeNumber_q(){
        primeNumber_q = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(bitNumsOfPrimeNumber_q);
    }

    private void computeLargePrimeNumber_p(){
        BigInteger number_2 = BigInteger.valueOf(2);
        BigInteger number_l_minus_1 = number_l.subtract(BigInteger.ONE);
        // TODO: Try to do with Fast exponentiation
        int num_l_minus_1 = number_l_minus_1.intValue();
        int num_l = number_l.intValue();
        BigInteger two_to_number_l_minus_1 = number_2.pow(num_l_minus_1);
        BigInteger two_to_number_l = number_2.pow(num_l);

        BigInteger candidateLargePrime_p = bigNumberGenerator.generateBigIntegersWithUpperLimit(two_to_number_l).add(two_to_number_l_minus_1);
        boolean isCandidateLargePrimeFount = false;
        while (!isCandidateLargePrimeFount) {
            if (isCandidateLargePrimeNumber_q_between_interval(candidateLargePrime_p)) {
                isCandidateLargePrimeFount = true;
                largePrimeNumber_p = candidateLargePrime_p;
            } else {
                candidateLargePrime_p = bigNumberGenerator.generateBigIntegersWithUpperLimit(two_to_number_l).add(two_to_number_l_minus_1);
            }
        }
    }

    private void computeNumber_g(){
        System.out.println("Compute number g");
        BigInteger p_minus_1 = largePrimeNumber_p.subtract(BigInteger.ONE);
        BigInteger number_h = computeNumber_h(p_minus_1);
        BigInteger exponent = p_minus_1.divide(primeNumber_q);

        number_g = number_h.modPow(exponent, number_h);
    }

    private void computeRandomPrivateKey(){
        System.out.println("Random private Key");
        boolean isRandomPrivateKey_Generated = false;
        BigInteger candidatePrivateKey;
        while (!isRandomPrivateKey_Generated){
            candidatePrivateKey = bigNumberGenerator.generateBigIntegersWithNumberOfBits(bitNumsOfPrimeNumber_q);
            if (candidatePrivateKey.compareTo(primeNumber_q) == -1){
                randomPrivateKey_x = candidatePrivateKey;
                isRandomPrivateKey_Generated = true;
            }
        }
    }

    private void computePublicKey(){
        System.out.println("Compute public key");
        publicKey = fastExponentiation.calculateModularWithFastExponentiation(number_g, randomPrivateKey_x, largePrimeNumber_p);
    }

    private BigInteger computeNumber_h(BigInteger p_minus_1) {
        BigInteger candidateNumber_h;

        candidateNumber_h = bigNumberGenerator.generateBigIntegersWithNumberOfBits(p_minus_1.bitLength());
        if (candidateNumber_h.compareTo(BigInteger.ONE) == 1 && candidateNumber_h.compareTo(p_minus_1) == -1) {
            BigInteger divide = p_minus_1.divide(primeNumber_q);
            if (fastExponentiation.calculateModularWithFastExponentiation(candidateNumber_h, divide, largePrimeNumber_p).compareTo(BigInteger.ONE) == 1) {
                return candidateNumber_h;
            }
        }

        return BigInteger.ONE;
    }

    private boolean isCandidateLargePrimeNumber_q_between_interval(BigInteger candidateLargePrime_p){
        BigInteger number_2 = BigInteger.valueOf(2);
        BigInteger number_l_minus_1 = number_l.subtract(BigInteger.ONE);
        // TODO: Try to do with Fast exponentiation
        int num_l_minus_1 = number_l_minus_1.intValue();
        int num_l = number_l.intValue();
        BigInteger two_to_number_l_minus_1 = number_2.pow(num_l_minus_1);
        BigInteger two_to_number_l = number_2.pow(num_l);

        if (candidateLargePrime_p.compareTo(two_to_number_l_minus_1) == 1 && candidateLargePrime_p.compareTo(two_to_number_l) == -1)
            return true;
        else
            return false;
    }

    private boolean isCandidateLargeNumber_q_primeDivisor_of_p_1(BigInteger candidateLargePrime_p){
        BigInteger p_1 = largePrimeNumber_p.subtract(BigInteger.ONE);
        if (p_1.mod(primeNumber_q).compareTo(BigInteger.ZERO) == 0)
            return true;
        else
            return false;
    }

    private void computeNumber_l(){
        boolean isNumber_l_Generated = false;
        int startBitNumsOfNumber_l = 5;
        BigInteger candidateNumber_l;
        while (!isNumber_l_Generated){
            candidateNumber_l = bigNumberGenerator.generateBigIntegersWithNumberOfBits(startBitNumsOfNumber_l);
            if (startBitNumsOfNumber_l > 1024) {
                System.out.println("Can not find L between 512 to 1024 bits");
                break;
            }
            else if (isCandidateNumber_l_multiple_of_64(candidateNumber_l)){
                number_l = candidateNumber_l;
                isNumber_l_Generated = true;
            }else{
                startBitNumsOfNumber_l ++;
            }
        }
    }


    private boolean isCandidateNumber_l_multiple_of_64(BigInteger candidateNumber_l){
        BigInteger multiple_64 = BigInteger.valueOf(64);
        if (candidateNumber_l.mod(multiple_64).compareTo(BigInteger.ZERO) == 0)
            return true;
        else
            return false;
    }
}
