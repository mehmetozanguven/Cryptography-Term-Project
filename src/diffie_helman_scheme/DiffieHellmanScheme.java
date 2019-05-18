package diffie_helman_scheme;

import measurement.PerformanceMeasurement;
import person.diffie_helman_person.Person;
import person.diffie_helman_person.Receiver;
import person.diffie_helman_person.Sender;
import unsecure_channel.DiffieHellmanUnsecureChannel;
import unsecure_channel.UnsecureChannel;

/**
 * Diffie-Hellman exchange scheme
 * Note : I assume that 1 page-length is 500 words
 *            then      10 page-length will be 500*10 = 5000 words etc..
 *
 * Note : 0 => Encrypts/Decrypts the 1 page - length file
 *        1 => Encrypts/Decrypts the 10 page - length file
 *        2 => Encrypts/Decrypts the 100 page - length file
 *        3 => Encrypts/Decrypts the 1000 page - length file
 */
public class DiffieHellmanScheme {
    static PerformanceMeasurement performanceMeasurement = PerformanceMeasurement.getInstance();

    public static void startDiffieHellmanScheme(){
        // Start the unsecure channel which is the Internet
        DiffieHellmanUnsecureChannel unsecureChannel = new UnsecureChannel();

        // after starting, set prime number and alpha number (these are the numbers publicly known)
        // I will need these number for Diffie-Hellman key exchange
        unsecureChannel.diffieHellmanSetup();

        // Create the sender and receiver and set the prime and alpha numbers
        Person sender = new Sender();
        sender.setPrimeNumber_p_from_unsecureChannel(unsecureChannel.getPrimeNum_p());
        sender.setAnyNumber_alpha_from_unsecureChannel(unsecureChannel.getAnyNum_alpha());

        Person receiver = new Receiver();
        receiver.setPrimeNumber_p_from_unsecureChannel(unsecureChannel.getPrimeNum_p());
        receiver.setAnyNumber_alpha_from_unsecureChannel(unsecureChannel.getAnyNum_alpha());

        // sender will generate its private and public key respectively
        sender.generatePrivateKey();
        sender.generatePublicKey();
        // then it will send its public key to the unsecure channel
        unsecureChannel.setDiffieHellmanSenderPublicKey(sender.publishPublicKey());

        // receiver will generate its private and public key respectively
        receiver.generatePrivateKey();
        receiver.generatePublicKey();
        // then it will send its public key to the unsecure channel
        unsecureChannel.setDiffieHellmanReceiverPublicKey(receiver.publishPublicKey());

        // now Diffie-hellman key exchange is done.
        // From now on, sender will send a file using receiver's public key and
        // receiver will decrypt the file with sender's public key
        int[] pageLength = new int[] {1,10,100,1000};
        for (int i = 0; i <= 3; i++){
            sender.encryptFile_publish(unsecureChannel.getDiffieHellmanReceiverPublicKey(), i);
            System.out.println("Here is the ENCRYPTION time and memory usage of " + pageLength[i] + " page-length");
            System.out.println(performanceMeasurement);
            System.out.println("------------------------------");

            receiver.decryptFile(unsecureChannel.getDiffieHellmanSenderPublicKey(), i);
            System.out.println("Here is the DECRYPTION time and memory usage of " + pageLength[i] + " page-length");
            System.out.println(performanceMeasurement);
            System.out.println("------------------------------");
        }
    }

}
