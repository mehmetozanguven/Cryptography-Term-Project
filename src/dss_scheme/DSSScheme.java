package dss_scheme;

import encryption.des.DESEncryption;
import encryption.rsa.RSAEncryption;
import person.rsa_person.Person;
import person.rsa_person.Receiver;
import person.rsa_person.Sender;
import unsecure_channel.DSSUnsecureChannel;
import unsecure_channel.UnsecureChannel;



public class DSSScheme {
    private static final int BIT_LENGTH = 1024;

    public static void startDSSScheme(){


        // Create unsecureChannel(represents the Internet),
        DSSUnsecureChannel dssUnsecureChannel = new UnsecureChannel();

        RSAEncryption rsaEncryption = new RSAEncryption();
        DESEncryption desEncryption = new DESEncryption();

        // Create a sender person
        Person sender = new Sender();
        // set the rsa bit length
        sender.setBitLength(BIT_LENGTH);
        // generate large prime numbers p & q
        sender.generateLargePrimeNumber_p();
        sender.generateLargePrimeNumber_q();
        // after generating compute N = (p)*(q)
        sender.compute_number_n();
        // compute euler phi function T = (p-1)*(q-1)
        sender.computeEulerPhiFunction();
        // compute public key for sender (N,e)
        sender.computePublicKey();
        // compute private key for sender (N,d)
        sender.computePrivateKey();
        // generate random des key to encrypt the files
        sender.generateRandomDESKey();

        // Create a receiver person, and apply the same steps as I did in sender
        Person receiver = new Receiver();
        receiver.setBitLength(BIT_LENGTH);
        receiver.generateLargePrimeNumber_p();
        receiver.generateLargePrimeNumber_q();
        receiver.compute_number_n();
        receiver.computeEulerPhiFunction();
        receiver.computePublicKey();
        receiver.computePrivateKey();
        receiver.generateRandomDESKey();

        // receiver will publish its public key pair(N,e) to unsecurechannel
        ((UnsecureChannel) dssUnsecureChannel).setReceiverPublicKeyPair(receiver.getPublicKeyPair());

        // now sender will get this pair(receiver's public key)
        // (therefore receiver will have shared its public key with sender) from the unsecure channel
        // to encrypt its random DES key
        sender.encryptRandomDESKeyWithRSA(((UnsecureChannel) dssUnsecureChannel).getReceiverPublicKey(), rsaEncryption);
        // after encrypting DES key, sender will now publish the encrypted DES key to unsecure channel
        ((UnsecureChannel) dssUnsecureChannel).setSenderRandomDESWithRSAEncryption(sender.getRandomDESKeyWithRSAEncryption());

        // sender will publish its public key to unsecure channel
        ((UnsecureChannel) dssUnsecureChannel).setSenderPublicKeyPair(sender.getPublicKeyPair());

        int[] pageLength = new int[] {1,10,100,1000};
        for (int i = 0; i <= 3; i++){
            // sender will generate keys for DSA includes:
            // public key components(p, q, g) and y (public key)
            // private key x
            sender.DSAKeyGeneration();

            // sender will sign the file
            sender.signFile(i);

            // then, sender will send signature parameter (for that file) to unsecure channel
            dssUnsecureChannel.setSenderDSASignParameter(sender.getSignParameter());
            // sender will send public key to unsecure channel
            dssUnsecureChannel.setSenderDSAPublicKeyComponent(sender.getPublicParameter());

            // now sender will encrypt and publish all the files
            sender.encryptFile_publish(i, desEncryption);
            System.out.println("Receiver will verify the page which length " + pageLength[i] + " page :");
            receiver.verifyFile(i,
                    dssUnsecureChannel.getSenderDSASignParameter(),
                    dssUnsecureChannel.getSenderDSAPublicKeyComponent(),
                    rsaEncryption,
                    desEncryption,
                    ((UnsecureChannel) dssUnsecureChannel).getSenderRandomDESWithRSAEncryption());
            System.out.println("--------------");

        }


    }
}
