package person.dss_signature_person;

import bignumber_generator.BigNumberGenerator;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithm;
import fast_exponentiation.FastExponentiation;

public class Receiver extends PersonElGamal {
    public Receiver(BigNumberGenerator bigNumberGenerator, FastExponentiation fastExponentiation, ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm) {
        super(bigNumberGenerator, fastExponentiation, extendedEuclideanAlgorithm);
    }
}
