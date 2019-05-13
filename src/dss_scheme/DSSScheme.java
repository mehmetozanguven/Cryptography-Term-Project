package dss_scheme;

import bignumber_generator.BigNumberGenerator;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithm;
import extended_euclidean_algorithm.ExtendedEuclideanAlgorithmImpl;
import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;
import person.dss_signature_person.PersonElGamal;
import person.dss_signature_person.Receiver;
import person.dss_signature_person.Sender;
import signature.DSASignature;
import unsecure_channel.DSSUnsecureChannel;
import unsecure_channel.UnsecureChannel;



public class DSSScheme {

    public static void startDSSScheme(){

        String simpleMessage = "ozan";


        FastExponentiation fastExponentiation = new FastExponentiationImpl();
        BigNumberGenerator bigNumberGenerator = new BigNumberGenerator();
        ExtendedEuclideanAlgorithm extendedEuclideanAlgorithm = new ExtendedEuclideanAlgorithmImpl();

        DSASignature dsaSignature = new DSASignature(fastExponentiation);

        // Create unsecureChannel(represents the Internet),
        DSSUnsecureChannel dssUnsecureChannel = new UnsecureChannel();

        PersonElGamal sender = new Sender(bigNumberGenerator, fastExponentiation ,extendedEuclideanAlgorithm);
        sender.elGamalKeyGeneration();
        sender.signTheMessage(simpleMessage);

        PersonElGamal receiver = new Receiver(bigNumberGenerator, fastExponentiation, extendedEuclideanAlgorithm);
        receiver.verifyTheMessage(sender.getPublicKeyComponents(), sender.getSignatureParameters(), "ozan");

    }
}
