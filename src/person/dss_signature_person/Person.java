package person.dss_signature_person;

import bignumber_generator.BigNumberGenerator;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithm;
import fast_exponentiation.FastExponentiation;
import sha_hash.SHA3Hash;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;

public abstract class Person {

    private BigNumberGenerator bigNumberGenerator;
    private FastExponentiation fastExponentiation;
    private ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm;
    private BigInteger number_l;
    private BigInteger primeNumber_q;
    private BigInteger primeNumber_p;
    private BigInteger number_h;
    private BigInteger generator_g;
    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger[] publicKeyComponent;

    private BigInteger number_k;
    private BigInteger number_r;
    private BigInteger number_s;
    private BigInteger[] signatureComponent;

    public Person(BigNumberGenerator bigNumberGenerator, FastExponentiation fastExponentiation, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm) {
        this.bigNumberGenerator = bigNumberGenerator;
        this.fastExponentiation = fastExponentiation;
        this.extendedEuclideanAlgorithm = extendedEuclideanAlgorithm;
    }

    public BigInteger[] getPublicKeyComponent() {
        return publicKeyComponent;
    }

    public BigInteger[] getSignatureComponent() {
        return signatureComponent;
    }

    public void DSAKeyGeneration(){

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();

        DSAParams dsaParams = privateKey.getParams();
        primeNumber_p = dsaParams.getP();
        primeNumber_q = dsaParams.getQ();
        generator_g = dsaParams.getG();

//        computeNumber_l();
//        computePrimeNumber_q();
//        computeLargePrimeNumber_p();
//        computeNumber_h();
//        computeNumber_g();
        computePrivateKey();
        computePublicKey();

        createPublicKeyComponents();
    }

    private void computeNumber_l() {
        System.out.println("Compute number l");
        boolean isNumber_l_Generated = false;
        int startBitNumsOfNumber_l = 2;
        BigInteger candidateNumber_l;
        while (!isNumber_l_Generated) {
            candidateNumber_l = bigNumberGenerator.generateBigIntegersWithNumberOfBits(startBitNumsOfNumber_l);

            if (startBitNumsOfNumber_l > 512) {
                System.out.println("Can not find L between 512 to 1024 bits");
                break;
            } else if (isCandidateNumber_l_multiple_of_64(candidateNumber_l)) {
                System.out.println("Candidate number l: " + candidateNumber_l);
                number_l = candidateNumber_l;
                isNumber_l_Generated = true;
            } else {
                startBitNumsOfNumber_l++;
            }
        }
    }

    private boolean isCandidateNumber_l_multiple_of_64(BigInteger candidateNumber_l) {
        BigInteger multiple_64 = BigInteger.valueOf(64);
        if (candidateNumber_l.mod(multiple_64).compareTo(BigInteger.ZERO) == 0)
            return true;
        else
            return false;
    }

    private void computePrimeNumber_q() {
        System.out.println("Compute Prime number q");
        primeNumber_q = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(160);
    }

    private void computeLargePrimeNumber_p() {
        System.out.println("Compute large prime number q");
        BigInteger number_2 = BigInteger.valueOf(2);
        BigInteger number_l_minus_1 = number_l.subtract(BigInteger.ONE);
        System.out.println("number_l_minus_1: " + number_l_minus_1);
        // TODO: Try to do with Fast exponentiation
//        int num_l_minus_1 = number_l_minus_1.intValue();
//        int num_l = number_l.intValue();
//
//        System.out.println("number_l_minus_1: " + number_l_minus_1);
//        System.out.println("number_l: " + num_l);

//        BigInteger two_to_number_l_minus_1 = bigNumberGenerator.powBigInteger(number_2, number_l_minus_1);
//        BigInteger two_to_number_l = bigNumberGenerator.powBigInteger(number_2, number_l);

        BigInteger two_to_number_l_minus_1 = bigNumberGenerator.powBigIntegerInMod(number_2, number_l_minus_1, primeNumber_q);
        BigInteger two_to_number_l = bigNumberGenerator.powBigIntegerInMod(number_2, number_l, primeNumber_q);


        primeNumber_p = bigNumberGenerator.bigIntInRange(two_to_number_l_minus_1, two_to_number_l);
//        boolean isCandidateLargePrimeFound = false;
//        while (!isCandidateLargePrimeFound) {
//            if (isCandidateLargePrimeNumber_q_between_interval(candidateLargePrime_p)) {
//                isCandidateLargePrimeFound = true;
//                primeNumber_p = candidateLargePrime_p;
//            } else {
//                candidateLargePrime_p = bigNumberGenerator.generateBigIntegersWithUpperLimit(two_to_number_l).add(two_to_number_l_minus_1);
//            }
//        }
    }

    private boolean isCandidateLargePrimeNumber_q_between_interval(BigInteger candidateLargePrime_p) {
        BigInteger number_2 = BigInteger.valueOf(2);
        BigInteger number_l_minus_1 = number_l.subtract(BigInteger.ONE);
        // TODO: Try to do with Fast exponentiation
        int num_l_minus_1 = number_l_minus_1.intValue();
        int num_l = number_l.intValue();
        BigInteger two_to_number_l_minus_1 = bigNumberGenerator.powBigInteger(number_2, number_l_minus_1);
        BigInteger two_to_number_l = bigNumberGenerator.powBigInteger(number_2, number_l);

        if (candidateLargePrime_p.compareTo(two_to_number_l_minus_1) == 1 && candidateLargePrime_p.compareTo(two_to_number_l) == -1)
            return true;
        else
            return false;
    }

    private void computeNumber_h(){
        BigInteger number_p_minus_one = primeNumber_p.subtract(BigInteger.ONE);

        boolean isNumberHFound = false;

        while (!isNumberHFound){
            BigInteger candidateNumber_h = bigNumberGenerator.bigIntInRange(BigInteger.ONE, number_p_minus_one);
            BigInteger exponent = number_p_minus_one.divide(primeNumber_q);
            if (fastExponentiation.calculateModularWithFastExponentiation(candidateNumber_h, exponent, primeNumber_p).compareTo(BigInteger.ONE) == 1){
                number_h = candidateNumber_h;
                isNumberHFound = true;
            }
        }
    }

    private void computeNumber_g() {
        BigInteger number_p_minus_one = primeNumber_p.subtract(BigInteger.ONE);
        BigInteger exponent = number_p_minus_one.divide(primeNumber_q);
        generator_g = bigNumberGenerator.powBigInteger(number_h, exponent);
    }

    private void computePrivateKey(){
        privateKey = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(primeNumber_q.bitLength());
    }

    private void computePublicKey(){
//        publicKey = fastExponentiation.calculateModularWithFastExponentiation(generator_g, privateKey, primeNumber_p);
        publicKey = generator_g.modPow(privateKey, primeNumber_p);
    }

    private void createPublicKeyComponents(){
        publicKeyComponent = new BigInteger[4];
        publicKeyComponent[0] = primeNumber_p;
        publicKeyComponent[1] = primeNumber_q;
        publicKeyComponent[2] = generator_g;
        publicKeyComponent[3] = publicKey;
    }

    public void signTheMessage(String message){
        computeNumber_k();
        computeNumber_r();
        computeNumber_s(message);
        createSignatureComponent();
    }

    private void computeNumber_k() {
        number_k = bigNumberGenerator.generateBigIntegersWithNumberOfBits(primeNumber_q.bitLength());
    }

    private void computeNumber_r(){
        number_r = fastExponentiation.calculateModularWithFastExponentiation(generator_g, number_k, primeNumber_q);
    }

    private void computeNumber_s(String message) {
        BigInteger inverseOfK = number_k.modInverse(primeNumber_q);

        byte[] hashedBytes = SHA3Hash.sha3_256_hash_with(message);
        BigInteger byteToBigInt = new BigInteger(1, hashedBytes);

        BigInteger innerOperation = byteToBigInt.add(privateKey.multiply(number_r));
        BigInteger outerOperation = inverseOfK.multiply(innerOperation);

        number_s = outerOperation.mod(primeNumber_q);
    }

    private void createSignatureComponent(){
        signatureComponent = new BigInteger[2];
        signatureComponent[0] = number_r;
        signatureComponent[1] = number_s;
    }

    public void verifyTheMessage(String message, BigInteger[] publicKeyComponent, BigInteger[] signatureComponent){
        BigInteger number_p = publicKeyComponent[0];
        BigInteger number_q = publicKeyComponent[1];
        BigInteger number_g = publicKeyComponent[2];
        BigInteger public_key = publicKeyComponent[3];

        BigInteger number_r = signatureComponent[0];
        BigInteger number_s = signatureComponent[1];

        byte[] hashedBytes = SHA3Hash.sha3_256_hash_with(message);
        BigInteger byteToBigInt = new BigInteger(1, hashedBytes);



        BigInteger number_w = computeNumber_w(number_s, number_q);
        BigInteger number_u1 = computeNumber_u1(byteToBigInt, number_w, number_q);
        BigInteger number_u2 = computeNumber_u2(number_r, number_w, number_q);
        BigInteger number_v = computeNumber_v(number_g, public_key, number_u1, number_u2, number_p, number_q);

        if (number_v.compareTo(number_r) == 0){
            System.out.println("Verified");
        }else{
            System.out.println("Not verified");
        }
    }

    private BigInteger computeNumber_w(BigInteger number_s, BigInteger number_q) {
        return number_s.modInverse(number_q);
    }

    private BigInteger computeNumber_u1(BigInteger byteToBigInt, BigInteger number_w, BigInteger number_q) {
        return byteToBigInt.multiply(number_w).mod(number_q);
    }

    private BigInteger computeNumber_u2(BigInteger number_r, BigInteger number_w, BigInteger number_q) {
        return number_r.multiply(number_w).mod(number_q);
    }


    private BigInteger computeNumber_v(BigInteger number_g, BigInteger public_key, BigInteger number_u1, BigInteger number_u2, BigInteger number_p, BigInteger number_q) {
        BigInteger exponent_g = bigNumberGenerator.powBigIntegerInMod(number_g, number_u1, number_p);
        BigInteger exponent_y = bigNumberGenerator.powBigIntegerInMod(public_key, number_u2, number_p);
        BigInteger multiplyExponents_mod = exponent_g.multiply(exponent_y).mod(number_p);

        return multiplyExponents_mod.mod(number_q);
    }


}
