package encryption.rsa;

import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;


import java.math.BigInteger;
import java.util.Map;

public class RSAEncryption {
    private FastExponentiation fastExponentiation;

    public RSAEncryption(){
        fastExponentiation = new FastExponentiationImpl();
    }

    /**
     * RSA encryption process, this method encrypts the key with public key pair
     * @param publicKeyPairs is the pair (N, e)
     * @param key is the number that will be encrypted
     * @return encrypted number as a resul
     */
    public BigInteger encryptTheKey(Map publicKeyPairs, BigInteger key){
        RSAKeyPairHandler rsaKeyPairHandler = new RSAKeyPairHandler(publicKeyPairs);
        BigInteger largeNumber_n = rsaKeyPairHandler.getKey();
        BigInteger publicKey_e = rsaKeyPairHandler.getValue();

        BigInteger encryptionResult = fastExponentiation.calculateModularWithFastExponentiation(key, publicKey_e, largeNumber_n);

        return encryptionResult;
    }

    /**
     * RSA decryption process, this methods decrypts the key with private key pair
     * @param privateKeyPairs is the pair (N, d)
     * @param key is the number that will be decrypted
     * @return decrypted number as a result
     */
    public BigInteger decryptTheKey(Map privateKeyPairs, BigInteger key){
        RSAKeyPairHandler rsaKeyPairHandler = new RSAKeyPairHandler(privateKeyPairs);
        BigInteger largeNumber_n = rsaKeyPairHandler.getKey();
        BigInteger publicKey_d = rsaKeyPairHandler.getValue();

        BigInteger decryptionResult = fastExponentiation.calculateModularWithFastExponentiation(key, publicKey_d, largeNumber_n);
        return decryptionResult;
    }
}
