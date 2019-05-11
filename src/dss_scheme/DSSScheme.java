package dss_scheme;

import bignumber_generator.BigNumberGenerator;
import fast_exponentiation.FastExponentiation;
import fast_exponentiation.FastExponentiationImpl;
import person.dss_signature_person.Person;
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
        DSASignature dsaSignature = new DSASignature(fastExponentiation);

        // Create unsecureChannel(represents the Internet),
        DSSUnsecureChannel dssUnsecureChannel = new UnsecureChannel();

        // Create a sender person
        Person sender = new Sender(bigNumberGenerator, fastExponentiation);
        // DSS key generation
        sender.DSAKeyGeneration();
        // sender will send his/her public key components to unsecure channel
        dssUnsecureChannel.setSenderPublicKeyComponents(sender.getPublicKeyComponent());
        // sender will sign message
        sender.signMessage(simpleMessage, dsaSignature);
        // sender will send (r,s) pair to unsecure channel
        dssUnsecureChannel.setSenderSignaturePair(sender.getSignaturePair());

        // Create a receiver person
        Person receiver = new Receiver(bigNumberGenerator, fastExponentiation);
        // DSS key generation for receiver
        receiver.DSAKeyGeneration();
        // receiver will send his/her public key components to unsecure channel
        dssUnsecureChannel.setReceiverPublicKeyComponents(receiver.getPublicKeyComponent());
        // now receiver will take the message comes from sender and verify the message
        receiver.verificationTheMessage(dssUnsecureChannel.getSenderPublicKeyComponents(), dssUnsecureChannel.getSenderSignaturePair(), simpleMessage, dsaSignature);
    }
}
