package rsa_scheme;


import encryption.des.DESEncryption;
import encryption.rsa.RSAEncryption;
import measurement.PerformanceMeasurement;
import person.rsa_person.Person;
import person.rsa_person.Receiver;
import person.rsa_person.Sender;
import unsecure_channel.RSAUnsecureChannel;
import unsecure_channel.UnsecureChannel;

/**
 * RSA Scheme
 * I am using DES encryption to encrypt the file
 * DES key will be exchange via RSA encryption
 */
public class RSAScheme {
    // Define the bit length for rsa algorithm
    private static final int BIT_LENGTH = 512;

    static PerformanceMeasurement performanceMeasurement = PerformanceMeasurement.getInstance();

    public static void startRSAScheme(){

        // Create unsecureChannel(represents the Internet), rsa and des encryption
        RSAUnsecureChannel unsecureChannel = new UnsecureChannel();
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
        unsecureChannel.setReceiverPublicKeyPair(receiver.getPublicKeyPair());

        // now sender will get this pair from the unsecure channel to encrypt its random DES key
        sender.encryptRandomDESKeyWithRSA(unsecureChannel.getReceiverPublicKey(), rsaEncryption);
        // after encrypting DES key, sender will now publish the encrypted DES key to unsecure channel
        unsecureChannel.setSenderRandomDESWithRSAEncryption(sender.getRandomDESKeyWithRSAEncryption());

        // sender will publish its public key to unsecure channel
        unsecureChannel.setSenderPublicKeyPair(sender.getPublicKeyPair());


        int[] pageLength = new int[] {1,10,100,1000};
        for (int i = 0; i <= 3; i++){
            // now sender will encrypt and publish all the files
            sender.encryptFile_publish(pageLength[i], desEncryption);
            System.out.println("Here is the ENCRYPTION time and memory usage of " + pageLength[i] + " page-length");
            System.out.println(performanceMeasurement);
            System.out.println("------------------------------");
            // receiver will decrypt all the files
            receiver.decryptFile(pageLength[i], rsaEncryption, unsecureChannel.getSenderRandomDESWithRSAEncryption(), desEncryption);
            System.out.println("Here is the DECRYPTION time and memory usage of " + pageLength[i] + " page-length");
            System.out.println(performanceMeasurement);
            System.out.println("------------------------------");
        }

    }
}
