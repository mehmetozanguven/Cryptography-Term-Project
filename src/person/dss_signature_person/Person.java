package person.dss_signature_person;

import bignumber_generator.BigNumberGenerator;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithm;
import fast_exponentiation.FastExponentiation;
import sha_hash.SHA3Hash;
import signature.DSASignature;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public abstract class Person {
    private final int bitNumsOfPrimeNumber_q = 160;
    private final int bitNumsOfLargePrimeNumber_p = 512;

    private BigNumberGenerator bigNumberGenerator;
    private FastExponentiation fastExponentiation;
    private ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm;

//    private BigInteger primeNumber_q;
//    private BigInteger largePrimeNumber_p;
//    private BigInteger number_l;
//    private BigInteger number_g;
//    private BigInteger randomPrivateKey_x;
//    private BigInteger publicKey;
//    private Map<Integer, BigInteger> publicKeyComponents;
//    private Map<Integer, BigInteger> signaturePair;


    private BigInteger secretKey;
    private BigInteger publicKey;
    private BigInteger randomBigIntegerK;
    private BigInteger[] signParameter;

    private BigInteger PRIME_MODULUS_P = new BigInteger(
            "b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6eddef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323");
    private BigInteger GENERATOR_G = new BigInteger(
            "44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a0267c9defe4119f2e373388cfa350a4e66e432d638ccdc58eb703e31d4c84e50398f9f91677e88641a2d2f6157e2f4ec538088dcf5940b053c622e53bab0b4e84b1465f5738f549664bd7430961d3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68");

    public Person(BigNumberGenerator bigNumberGenerator, FastExponentiation fastExponentiation, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm){
        this.bigNumberGenerator = bigNumberGenerator;
        this.fastExponentiation = fastExponentiation;
        this.extendedEuclideanAlgorithm = extendedEuclideanAlgorithm;
//        publicKeyComponents = new HashMap<>();

    }
    //Used to calculate secret key x
    //learned about compare to and the random constructor from https://stackoverflow.com/questions/3735664/randomizing-a-biginteger
    public void computeTheSecretKey(){
        Random rnd = new Random();

        BigInteger x = new BigInteger(PRIME_MODULUS_P.bitLength(), rnd);
        int count = 0; //Count for debugging purposes

        //while x <= min || x >= max
        //So far while testing I've only seen a max of 4 iterations of the loop.
        //Generally it's either 0 or 1 iterations
        while (x.compareTo(BigInteger.ONE) <= 0 || x.compareTo(PRIME_MODULUS_P) >= 0){
            x = new BigInteger(PRIME_MODULUS_P.bitLength(), rnd);
            count++;
        }
        System.out.println("Count for big int in range: " + count);
        secretKey = x;
    }

    //Right to left variant of calculating modular exponentiation.
    //Taken from my submission in assignment 1. Renamed method from performRSA to modularExponentiation
    public void computePublicKey(){
        publicKey = fastExponentiation.calculateModularWithFastExponentiation(GENERATOR_G, secretKey, PRIME_MODULUS_P);
    }

    public String getPublicKey() {
        return publicKey.toString(16);
    }

    public String[] getSignParameter() {
        String[] arr = {signParameter[0].toString(16), signParameter[0].toString(16)};
        return arr;
    }

    public BigInteger gcd(BigInteger a, BigInteger b){
        // Use Euclidean Algorithm of:
        //      gcd(a,0) = a
        //      gcd(a,b) = gcd(b, a mod b)

        // Base case gcd(a,0) = a
        if(b.compareTo(BigInteger.ZERO) == 0){
            return a;
        }

        //Recursive case gcd(a,b) = gcd(b, a mod b)
        else{
            return gcd(b, a.mod(b));
        }
    }

    //From spec: random value k with 0 < k < p-1 and gcd(k,p-1) = 1
    private  BigInteger generateK(BigInteger pMinusOne){
        BigInteger k = bigNumberGenerator.bigIntInRange(BigInteger.ZERO, pMinusOne);

        int count = 0;
        //So far while testing I've only seen a max of 6 iterations of the loop.
        while(gcd(k,pMinusOne).compareTo(BigInteger.ONE) != 0)
        {
            k = bigNumberGenerator.bigIntInRange(BigInteger.ZERO, pMinusOne);
            count++;
        }

        System.out.println("Count for generate k: " + count);
        return k;
    }


    //Explanation of pseudo-code: https://www.csee.umbc.edu/~chang/cs203.s09/exteuclid.shtml
    private  BigInteger[] xgcd(BigInteger a, BigInteger b){
        if(b.compareTo(BigInteger.ZERO) == 0){
            return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO}; // base case if b == 0 return (a,1,0)
        }

        //where recursiveCall = {d1, s1, t1}
        BigInteger[] recursiveCall = xgcd(b, a.mod(b));
        BigInteger d = recursiveCall[0]; //d = d1
        BigInteger s = recursiveCall[2]; //s = t1
        BigInteger t = recursiveCall[1].subtract(a.divide(b).multiply(recursiveCall[2])); //t = s1 - (a div b) * t1
        return new BigInteger[] {d,s,t};
    }


    //From your number theory notes http://www.computing.dcu.ie/~hamilton/teaching/CA4005/notes/Number1.pdf
    private  BigInteger multiplicativeInverse(BigInteger a, BigInteger N) throws ArithmeticException {
        //ensure gcd is one, i.e ensure d == 1
        BigInteger[] xgcdResult = xgcd(a,N);

        if(xgcdResult[0].compareTo(BigInteger.ONE) == 0){ //if d == 1
            return xgcdResult[1].mod(N); //return s mod N where s == x from the notes (page 8/16) http://www.computing.dcu.ie/~hamilton/teaching/CA4005/notes/Number1.pdf
        }
        else{
            throw new ArithmeticException("No modular inverse as d != 1. d == " + xgcdResult[0].toString());
        }
    }


    private BigInteger[] generateRAndS(BigInteger secretKey, byte[] hashedMessageBytes){
        //Generate k
        BigInteger randomK = generateK(PRIME_MODULUS_P.subtract(BigInteger.ONE));

        //Generate r
        BigInteger r = modularExponentiation(GENERATOR_G,randomK, PRIME_MODULUS_P);

        //calculate H(m)-xr
        BigInteger hashedMessageBigInt = new BigInteger(hashedMessageBytes); //convert to bigInt
        BigInteger hmMinusXR = hashedMessageBigInt.subtract(secretKey.multiply(r));

        //calculate k^-1 mod(p-1)
        BigInteger kMultiplicativeInverse = multiplicativeInverse(randomK, PRIME_MODULUS_P.subtract(BigInteger.ONE));

        //compute s as H(m)-xr * k^-1 mod(p-1)
        BigInteger s = hmMinusXR.multiply(kMultiplicativeInverse).mod(PRIME_MODULUS_P.subtract(BigInteger.ONE));

        //if s == 0 start over again
        if(s.compareTo(BigInteger.ZERO) == 0)
        {
            BigInteger[] recursiveCall = generateRAndS(secretKey, hashedMessageBytes);
            r = recursiveCall[0];
            s = recursiveCall[1];
        }

        //return r and s
        return new BigInteger[] {r, s};
    }

    private  void writeAssignmentHandInToFile(String outputPath,
                                                    BigInteger publicKeyY,
                                                    BigInteger[] digitalSignature,
                                                    BigInteger secretKeyX,
                                                    byte[] unhashedFile,
                                                    byte[] hashedFile) throws IOException {
        PrintWriter printwriter = new PrintWriter(outputPath, "UTF-8");
        System.out.println("Writing assignment hand-in details to: " + outputPath);
        printwriter.println("Public Key Y: " + publicKeyY.toString(16));
        printwriter.println("\nSecret Key X: " + secretKeyX.toString(16)); //Include X just in case
        printwriter.println("\nDigital Signature Value R: " + digitalSignature[0].toString(16));
        printwriter.println("\nDigital Signature Value S: " +  digitalSignature[1].toString(16));

        //Including the unhashed and hashed files just in case.
        printwriter.println("\nUnhashed File: " + DatatypeConverter.printHexBinary(unhashedFile));
        printwriter.println("\nHashed File: " + DatatypeConverter.printHexBinary(hashedFile));

        printwriter.close();
    }


    public boolean verify(String hexPublicKeyY,
                                  String hexR,
                                  String hexS,
                                  byte[] unhashedFile, String message) {
        BigInteger y = hexStringToBigInt(hexPublicKeyY);
        BigInteger r = hexStringToBigInt(hexR);
        BigInteger s = hexStringToBigInt(hexS);
        System.out.println(y + " " + r + " " + s);

        byte[] hashedMessage = SHA3Hash.sha3_256_hash_with(message);
        BigInteger hm = new BigInteger(hashedMessage);

        boolean rCheck = (r.compareTo(BigInteger.ZERO) > 0) && (r.compareTo(PRIME_MODULUS_P) < 0);
        boolean sCheck = (s.compareTo(BigInteger.ZERO) > 0) && (s.compareTo(PRIME_MODULUS_P.subtract(BigInteger.ONE)) < 0);


        //g^H(m) (mod p)
        BigInteger gHmModP = modularExponentiation(GENERATOR_G, hm,PRIME_MODULUS_P);
        //(y^r)(r^s) (mod p)
        BigInteger yRrSModP = modularExponentiation(y,r,PRIME_MODULUS_P).multiply(modularExponentiation(r,s,PRIME_MODULUS_P)).mod(PRIME_MODULUS_P);

        boolean signatureCheck = gHmModP.compareTo(yRrSModP) == 0;

        System.out.println("rCheck: " + rCheck + "\nsCheck " + sCheck + "\nSignature Check: " + signatureCheck);
        return rCheck && sCheck && signatureCheck;



//        MessageDigest digest = null;
//        try {
//            digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashedFile = digest.digest(unhashedFile); //the hashed file
//            BigInteger hm = new BigInteger(hashedFile);
//
//            boolean rCheck = (r.compareTo(BigInteger.ZERO) > 0) && (r.compareTo(PRIME_MODULUS_P) < 0);
//            boolean sCheck = (s.compareTo(BigInteger.ZERO) > 0) && (s.compareTo(PRIME_MODULUS_P.subtract(BigInteger.ONE)) < 0);
//
//
//            //g^H(m) (mod p)
//            BigInteger gHmModP = modularExponentiation(GENERATOR_G, hm,PRIME_MODULUS_P);
//            //(y^r)(r^s) (mod p)
//            BigInteger yRrSModP = modularExponentiation(y,r,PRIME_MODULUS_P).multiply(modularExponentiation(r,s,PRIME_MODULUS_P)).mod(PRIME_MODULUS_P);
//
//            boolean signatureCheck = gHmModP.compareTo(yRrSModP) == 0;
//
//            System.out.println("rCheck: " + rCheck + "\nsCheck " + sCheck + "\nSignature Check: " + signatureCheck);
//            return rCheck && sCheck && signatureCheck;
//
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            throw new NoSuchAlgorithmException(e);
//        }
    }

    private BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger mod){
        BigInteger y = new BigInteger("1");
        for(int i = 0; i < exponent.bitLength(); i++){
            if (exponent.testBit(i)) {
                y = y.multiply(base);
                y = y.mod(mod);
            }
            base = base.multiply(base);
            base = base.mod(mod);
        }
        return y;
    }

    private  BigInteger hexStringToBigInt(String hexString){
        return new BigInteger(hexString.replaceAll("\\s+",""), 16);
    }

    public void signTheMessage(String simpleMessage){
        byte[] hashedMessage = SHA3Hash.sha3_256_hash_with(simpleMessage);
        signParameter = generateRAndS(secretKey, hashedMessage);
    }


//    public void DSAKeyGeneration(){
//        computeNumber_l();
//        computePrimeNumber_q();
//        computeLargePrimeNumber_p();
//        computeNumber_g();
//        computeRandomPrivateKey();
//        computePublicKey();
//    }
//
//    public void signMessage(String message, DSASignature dsaSignature){
//        BigInteger randomNumberK = computeRandomNumberK();
//        dsaSignature.computeSignaturePair(publicKeyComponents, randomNumberK, message);
//        signaturePair = dsaSignature.getSignaturePair();
//    }
//
//    public Map<Integer, BigInteger> getSignaturePair(){
//        if (signaturePair == null){
//            throw new NullPointerException("Signature pair is null");
//        }else {
//            return signaturePair;
//        }
//    }
//
//    public void verificationTheMessage(Map<Integer, BigInteger> publicKeyComponents, Map<Integer, BigInteger> signaturePair, String message, DSASignature dsaSignature){
//        BigInteger verificationNumber = dsaSignature.verificationTheMessage(publicKeyComponents, signaturePair, message);
//        BigInteger number_r = signaturePair.get(0);
//        if (verificationNumber.compareTo(number_r) == 0){
//            System.out.println("Verified");
//        }else{
//            System.out.println("Not verified");
//        }
//    }
//
//    public Map<Integer, BigInteger> getPublicKeyComponent(){
//        publicKeyComponents.put(0, largePrimeNumber_p);
//        publicKeyComponents.put(1, primeNumber_q);
//        publicKeyComponents.put(2, number_g);
//        publicKeyComponents.put(3, publicKey);
//
//        return publicKeyComponents;
//    }
//
//    private void computeNumber_l(){
//        System.out.println("Compute number l");
//        boolean isNumber_l_Generated = false;
//        int startBitNumsOfNumber_l = 2;
//        BigInteger candidateNumber_l;
//        while (!isNumber_l_Generated){
//            candidateNumber_l = bigNumberGenerator.generateBigIntegersWithNumberOfBits(startBitNumsOfNumber_l);
//
//            if (startBitNumsOfNumber_l > 512) {
//                System.out.println("Can not find L between 512 to 1024 bits");
//                break;
//            }
//            else if (isCandidateNumber_l_multiple_of_64(candidateNumber_l)){
//                System.out.println("Candidate number l: " + candidateNumber_l);
//                number_l = candidateNumber_l;
//                isNumber_l_Generated = true;
//            }else{
//                startBitNumsOfNumber_l ++;
//            }
//        }
//    }
//
//
//    private BigInteger computeRandomNumberK() {
//        boolean isRandomNumberKGenerated = false;
//        BigInteger candidateNumberK = BigInteger.ONE;
//        while (!isRandomNumberKGenerated){
//            candidateNumberK = bigNumberGenerator.generateBigIntegersWithNumberOfBits(bitNumsOfPrimeNumber_q);
//            if (candidateNumberK.compareTo(primeNumber_q) == -1){
//                isRandomNumberKGenerated = true;
//            }
//        }
//        return candidateNumberK;
//    }
//
//    private void computePrimeNumber_q(){
//        System.out.println("Compute Prime number q");
//        primeNumber_q = bigNumberGenerator.generateBigPrimeIntegersWithNumberOfBits(bitNumsOfPrimeNumber_q);
//    }
//
//    private void computeLargePrimeNumber_p(){
//        System.out.println("Compute large prime number q");
//        BigInteger number_2 = BigInteger.valueOf(2);
//        BigInteger number_l_minus_1 = number_l.subtract(BigInteger.ONE);
//        System.out.println("number_l_minus_1: " + number_l_minus_1);
//        // TODO: Try to do with Fast exponentiation
////        int num_l_minus_1 = number_l_minus_1.intValue();
////        int num_l = number_l.intValue();
////
////        System.out.println("number_l_minus_1: " + number_l_minus_1);
////        System.out.println("number_l: " + num_l);
//
//        BigInteger two_to_number_l_minus_1 = bigNumberGenerator.powBigInteger(number_2, number_l_minus_1);
//        BigInteger two_to_number_l = bigNumberGenerator.powBigInteger(number_2, number_l);
//
//        BigInteger candidateLargePrime_p = bigNumberGenerator.generateBigIntegersWithUpperLimit(two_to_number_l).add(two_to_number_l_minus_1);
//        boolean isCandidateLargePrimeFount = false;
//        while (!isCandidateLargePrimeFount) {
//            if (isCandidateLargePrimeNumber_q_between_interval(candidateLargePrime_p)) {
//                isCandidateLargePrimeFount = true;
//                largePrimeNumber_p = candidateLargePrime_p;
//            } else {
//                candidateLargePrime_p = bigNumberGenerator.generateBigIntegersWithUpperLimit(two_to_number_l).add(two_to_number_l_minus_1);
//            }
//        }
//    }
//
//    private void computeNumber_g(){
//        System.out.println("Compute number g");
//        BigInteger p_minus_1 = largePrimeNumber_p.subtract(BigInteger.ONE);
//        BigInteger number_h = computeNumber_h(p_minus_1);
//        BigInteger exponent = p_minus_1.divide(primeNumber_q);
//
//        number_g = number_h.modPow(exponent, number_h);
//    }
//
//    private void computeRandomPrivateKey(){
//        System.out.println("Random private Key");
//        boolean isRandomPrivateKey_Generated = false;
//        BigInteger candidatePrivateKey;
//        while (!isRandomPrivateKey_Generated){
//            candidatePrivateKey = bigNumberGenerator.generateBigIntegersWithNumberOfBits(bitNumsOfPrimeNumber_q);
//            if (candidatePrivateKey.compareTo(primeNumber_q) == -1){
//                randomPrivateKey_x = candidatePrivateKey;
//                isRandomPrivateKey_Generated = true;
//            }
//        }
//    }
//
//    private void computePublicKey(){
//        System.out.println("Compute public key");
//        publicKey = fastExponentiation.calculateModularWithFastExponentiation(number_g, randomPrivateKey_x, largePrimeNumber_p);
//    }
//
//    private BigInteger computeNumber_h(BigInteger p_minus_1) {
//        BigInteger candidateNumber_h;
//
//        candidateNumber_h = bigNumberGenerator.generateBigIntegersWithNumberOfBits(p_minus_1.bitLength());
//        if (candidateNumber_h.compareTo(BigInteger.ONE) == 1 && candidateNumber_h.compareTo(p_minus_1) == -1) {
//            BigInteger divide = p_minus_1.divide(primeNumber_q);
//            if (candidateNumber_h.modPow(divide, largePrimeNumber_p).compareTo(BigInteger.ONE) == 1){
//                return candidateNumber_h;
//            }
////            if (fastExponentiation.calculateModularWithFastExponentiation(candidateNumber_h, divide, largePrimeNumber_p).compareTo(BigInteger.ONE) == 1) {
////                return candidateNumber_h;
////            }
//        }
//
//        return BigInteger.ONE;
//    }
//
//    private boolean isCandidateLargePrimeNumber_q_between_interval(BigInteger candidateLargePrime_p){
//        BigInteger number_2 = BigInteger.valueOf(2);
//        BigInteger number_l_minus_1 = number_l.subtract(BigInteger.ONE);
//        // TODO: Try to do with Fast exponentiation
//        int num_l_minus_1 = number_l_minus_1.intValue();
//        int num_l = number_l.intValue();
//        BigInteger two_to_number_l_minus_1 = bigNumberGenerator.powBigInteger(number_2, number_l_minus_1);
//        BigInteger two_to_number_l = bigNumberGenerator.powBigInteger(number_2, number_l);
//
//        if (candidateLargePrime_p.compareTo(two_to_number_l_minus_1) == 1 && candidateLargePrime_p.compareTo(two_to_number_l) == -1)
//            return true;
//        else
//            return false;
//    }
//
//    private boolean isCandidateLargeNumber_q_primeDivisor_of_p_1(BigInteger candidateLargePrime_p){
//        BigInteger p_1 = largePrimeNumber_p.subtract(BigInteger.ONE);
//        if (p_1.mod(primeNumber_q).compareTo(BigInteger.ZERO) == 0)
//            return true;
//        else
//            return false;
//    }
//
//
//
//    private boolean isCandidateNumber_l_multiple_of_64(BigInteger candidateNumber_l){
//        BigInteger multiple_64 = BigInteger.valueOf(64);
//        if (candidateNumber_l.mod(multiple_64).compareTo(BigInteger.ZERO) == 0)
//            return true;
//        else
//            return false;
//    }
}
