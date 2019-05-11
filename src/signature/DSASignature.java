package signature;

import fast_exponentiation.FastExponentiation;
import sha_hash.SHA3Hash;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class DSASignature {

    private BigInteger number_r;
    private BigInteger number_s;
    private Map<Integer, BigInteger> signaturePair;
    private FastExponentiation fastExponentiation;


    public DSASignature(FastExponentiation fastExponentiation){
        this.fastExponentiation = fastExponentiation;
    }

    public void computeSignaturePair(Map<Integer, BigInteger> publicKeyComponents, BigInteger randomNumber_k, String message){
        signaturePair = new HashMap<>();

        BigInteger number_p = publicKeyComponents.get(0);
        BigInteger number_q = publicKeyComponents.get(1);
        BigInteger number_g = publicKeyComponents.get(2);
        BigInteger publicKey = publicKeyComponents.get(3);

        computeNumber_r(number_g, randomNumber_k, number_p, number_q);

        String hashedMessage = SHA3Hash.sha3_256_hash_with(message);
        BigInteger hashBigInteger = new BigInteger(hashedMessage);

        computeNumber_s(randomNumber_k, publicKey, number_q, hashBigInteger);

        signaturePair.put(0, number_r);
        signaturePair.put(1, number_s);
    }

    public Map getSignaturePair(){
        return signaturePair;
    }

    public BigInteger verificationTheMessage(Map<Integer, BigInteger> publicKeyComponents, Map<Integer, BigInteger> signaturePair, String message){
        BigInteger number_p = publicKeyComponents.get(0);
        BigInteger number_q = publicKeyComponents.get(1);
        BigInteger number_g = publicKeyComponents.get(2);
        BigInteger publicKey = publicKeyComponents.get(3);

        BigInteger number_r = signaturePair.get(0);
        BigInteger number_s = signaturePair.get(1);

        BigInteger number_w = computeNumber_w(number_s, number_q);

        String hashedMessage = SHA3Hash.sha3_256_hash_with(message);
        BigInteger hashBigInteger = new BigInteger(hashedMessage);

        BigInteger number_u1 = computeNumber_u1(number_w, number_q, hashBigInteger);
        BigInteger number_u2 = computeNumber_u2(number_r, number_w, number_q);

        BigInteger number_v = computeNumber_v(number_g, number_u1, publicKey, number_u2, number_p, number_q);

        return number_v;
    }


    private void computeNumber_r(BigInteger number_g, BigInteger number_k, BigInteger number_p, BigInteger number_q){
        BigInteger firstModOperation = fastExponentiation.calculateModularWithFastExponentiation(number_g, number_k, number_p);
        number_r = firstModOperation.mod(number_q);
    }

    private void computeNumber_s (BigInteger number_k, BigInteger number_x, BigInteger number_q, BigInteger hashedMessage){
        BigInteger number_k_pow_minus_1 = number_k.pow(-1);
        BigInteger multiply_x_and_r = number_x.multiply(number_k);
        BigInteger multiply_hash_x_and_r = hashedMessage.multiply(multiply_x_and_r);
        BigInteger multiply_k_pow_minus_1_and_hash_x_and_r = number_k_pow_minus_1.multiply(multiply_hash_x_and_r);

        number_s = multiply_k_pow_minus_1_and_hash_x_and_r.mod(number_q);
    }

    private BigInteger computeNumber_w(BigInteger number_s, BigInteger number_q){
        BigInteger number_s_pow_minus_1 = number_s.pow(-1);
        return number_s_pow_minus_1.mod(number_q);
    }

    private BigInteger computeNumber_u1(BigInteger number_w, BigInteger number_q, BigInteger hashedMessage){
        BigInteger multiply = hashedMessage.multiply(number_w);
        return multiply.mod(number_q);
    }

    private BigInteger computeNumber_u2(BigInteger number_r, BigInteger number_w, BigInteger number_q){
        BigInteger multiply = number_r.multiply(number_w);
        return multiply.mod(number_q);
    }

    private BigInteger computeNumber_v(BigInteger number_g, BigInteger number_u1, BigInteger number_y, BigInteger number_u2, BigInteger number_p, BigInteger number_q){
        BigInteger number_g_exponent_u1 = number_g.pow(number_u1.intValue());
        BigInteger number_y_exponent_u2 = number_y.pow(number_u2.intValue());
        BigInteger multiply_exponents = number_g_exponent_u1.multiply(number_y_exponent_u2);
        BigInteger firstModOperation = multiply_exponents.mod(number_p);

        return firstModOperation.mod(number_q);
    }
}
