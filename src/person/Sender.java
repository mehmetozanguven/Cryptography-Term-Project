package person;

import encryption.des.DESEncryption;
import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;

import java.math.BigInteger;

public class Sender extends Person {


    public Sender(BigInteger primeNumber_p_from_unsecureChannel, BigInteger anyNumber_alpha_from_unsecureChannel) {
        super(primeNumber_p_from_unsecureChannel, anyNumber_alpha_from_unsecureChannel);
    }



}
