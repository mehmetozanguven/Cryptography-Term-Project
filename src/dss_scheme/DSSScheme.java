package dss_scheme;

import person.dss_signature_person.*;
import unsecure_channel.DSSUnsecureChannel;
import unsecure_channel.UnsecureChannel;



public class DSSScheme {

    public static void startDSSScheme(){


        // Create unsecureChannel(represents the Internet),
        DSSUnsecureChannel dssUnsecureChannel = new UnsecureChannel();

        // Create a sender person
        Person sender = new Sender();


        // Create a receiver person
        Person receiver = new Receiver();


        int[] pageLength = new int[] {1,10,100,1000};

        for (int i = 0; i < pageLength.length; i++){
            // sender will generate keys for DSA includes:
            // public key components(p, q, g) and y (public key)
            // private key x
            sender.DSAKeyGeneration();

            // sender will sign the file
            sender.signFile(i);

            // then, sender will send signature parameter (for that file) to unsecure channel
            dssUnsecureChannel.setSenderDSASignParameter(sender.getSignParameter());
            // then it will send the message that he/she has signed to unsecure channel
            dssUnsecureChannel.setSenderMessage(sender.getFileAsString());
            // sender will send public key to unsecure channel
            dssUnsecureChannel.setSenderDSAPublicKeyComponent(sender.getPublicParameter());

            // To illustrate "not verified message", I will add the empty space to file(100 page-length)
            if (i == 3){
                receiver.verifyMessage(dssUnsecureChannel.getSenderMessage()+" ", dssUnsecureChannel.getSenderDSASignParameter(), dssUnsecureChannel.getSenderDSAPublicKeyComponent());
            }else{
                receiver.verifyMessage(dssUnsecureChannel.getSenderMessage(), dssUnsecureChannel.getSenderDSASignParameter(), dssUnsecureChannel.getSenderDSAPublicKeyComponent());
            }
        }
    }
}
