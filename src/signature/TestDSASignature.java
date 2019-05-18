package signature;
import bignumber_generator.BigNumberGenerator;
import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;
import sha_hash.SHA3Hash;

import  java.math.BigInteger;
import  java.security.KeyPair;
import  java.security.KeyPairGenerator;
import  java.security.NoSuchAlgorithmException;
import  java.security.interfaces.DSAParams;
import  java.security.interfaces.DSAPrivateKey;
import  java.security.interfaces.DSAPublicKey;
import  java.util.Random;
import java.security.MessageDigest;

public class TestDSASignature {
    private BigNumberGenerator bigNumberGenerator;
    private FastExponentiation fastExponentiation;

    private BigInteger number_p;
    private BigInteger number_q;
    private BigInteger number_g;
    private BigInteger number_x;
    private BigInteger number_y;

    private BigInteger[] signParameter;
    private BigInteger[] publicParameter;

    public TestDSASignature(){
        bigNumberGenerator = new BigNumberGenerator();
        fastExponentiation = new FastExponentiationImpl();
    }

    public BigInteger[] getSignParameter(){
        return signParameter;
    }

    public BigInteger[] getPublicParameter(){
        return publicParameter;
    }

    public void keyGeneration() {
        KeyPairGenerator keyGen = null;

        try {
            keyGen = KeyPairGenerator.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.genKeyPair();
        DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();
        DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();

        DSAParams dsaParams = privateKey.getParams();
        number_p = dsaParams.getP();
        number_q = dsaParams.getQ();
        number_g = dsaParams.getG();


        number_x = computePrivateKey();
        number_y = computePublicKey();
        createPublicParameters();
    }

    private void createPublicParameters() {
        publicParameter = new BigInteger[4];
        publicParameter[0] = number_p;
        publicParameter[1] = number_q;
        publicParameter[2] = number_g;
        publicParameter[3] = number_y;
    }

    private BigInteger computePrivateKey() {
        return bigNumberGenerator.generateBigIntegersWithNumberOfBits(number_p.bitLength());
    }

    private BigInteger computePublicKey(){
        return fastExponentiation.calculateModularWithFastExponentiation(number_g, number_x, number_p);
    }

    public void signMessage(String message) {
        byte[] hashMessageWithByte = SHA3Hash.sha3_256_hash_with(message);
        BigInteger hashedBigInteger = new BigInteger(hashMessageWithByte);
        BigInteger number_k = generateRandomNumber_k();
        BigInteger number_r = computeNumber_r(number_k);
        BigInteger number_s = computeNumber_s(number_k, number_r, hashedBigInteger);
        createSignParameter(number_r, number_s);

    }

    private BigInteger generateRandomNumber_k(){
        return bigNumberGenerator.generateBigIntegersWithNumberOfBits(number_p.bitLength());
    }

    private BigInteger computeNumber_r(BigInteger number_k){
        // g.modPow(k, p)
        BigInteger innerOperation = fastExponentiation.calculateModularWithFastExponentiation(number_g, number_k, number_p);
        // g.modPow(k, p).mod(q);
        BigInteger result = innerOperation.mod(number_q);
        return result;
    }

    private BigInteger computeNumber_s(BigInteger number_k, BigInteger number_r, BigInteger hash){
        BigInteger inverse_k = number_k.modInverse(number_q);
        // x.r
        BigInteger x_r = number_x.multiply(number_r);
        // hash + x.r
        BigInteger hash_x_r = hash.add(x_r);
        //k^{-1}*(H(M)+ xr)
        BigInteger k_hash_x_r = inverse_k.multiply(hash_x_r);
        return k_hash_x_r.mod(number_q);

    }

    private void createSignParameter(BigInteger number_r, BigInteger number_s){
        signParameter = new BigInteger[2];
        signParameter[0] = number_r;
        signParameter[1] = number_s;
    }

    public void verifyMessage(String message, BigInteger[] signParameter, BigInteger[] publicParameter){
        BigInteger number_r = signParameter[0];
        BigInteger number_s = signParameter[1];

        BigInteger number_p = publicParameter[0];
        BigInteger number_q = publicParameter[1];
        BigInteger number_g = publicParameter[2];
        BigInteger number_y = publicParameter[3];

        byte[] hashMessageWithByte = SHA3Hash.sha3_256_hash_with(message);
        BigInteger hashedBigInteger = new BigInteger(hashMessageWithByte);

        BigInteger number_w = computeNumber_w(number_s, number_q);
        BigInteger number_u1 = computeNumber_u1(number_w, number_q, hashedBigInteger);
        BigInteger number_u2 = computeNumber_u2(number_r, number_w, number_q);
        BigInteger number_v = computeNumber_v(number_g, number_u1, number_u2, number_p, number_q, number_y);

        if (number_v.compareTo(number_r) == 0){
            System.out.println("Verified");
        }else{
            System.out.println("Not verified");
        }
    }

    private BigInteger computeNumber_w(BigInteger number_s, BigInteger number_q){
        return number_s.modInverse(number_q);
    }

    private BigInteger computeNumber_u1(BigInteger number_w, BigInteger number_q, BigInteger hash){
        // hash(msg).multiply(w)
        BigInteger hash_multiply_w = hash.multiply(number_w);
        // hash(msg).multiply(w).mod(q);
        BigInteger result = hash_multiply_w.mod(number_q);

        return result;
    }

    private BigInteger computeNumber_u2(BigInteger number_r, BigInteger number_w, BigInteger number_q){
        //  r.multiply(w)
        BigInteger r_multiply_w = number_r.multiply(number_w);
        // r.multiply(w).mod(q);
        BigInteger result = r_multiply_w.mod(number_q);
        return result;
    }

    private BigInteger computeNumber_v(BigInteger number_g, BigInteger number_u1, BigInteger number_u2, BigInteger number_p, BigInteger number_q, BigInteger number_y){
        // g^{u1}
        BigInteger g_to_u1 = number_g.modPow(number_u1, number_p);
        // y^{u2}
        BigInteger y_to_u2 = number_y.modPow(number_u2, number_p);
        // g^{u1} * y^{u2}
        BigInteger multiply = g_to_u1.multiply(y_to_u2);
        // (g^{u1} * y^{u2}).mod(p)
        BigInteger multiply_mod = multiply.mod(number_p);
        // [(g^{u1} * y^{u2}).mod(p)].mod q
        BigInteger result = multiply_mod.mod(number_q);
        return result;
    }
}
