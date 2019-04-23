import person.Person;
import person.Receiver;
import person.Sender;
import unsecure_channel.UnsecureChannel;

import java.math.BigInteger;

public class Main {



    public static void main(String[] args) {

        UnsecureChannel unsecureChannel = new UnsecureChannel();
        Person sender = new Sender(unsecureChannel.getPrimeNum_p(), unsecureChannel.getAnyNum_alpha());
        Person receiver = new Receiver(unsecureChannel.getPrimeNum_p(), unsecureChannel.getAnyNum_alpha());

        sender.generatePrivateKey(5);
        sender.generatePublicKey();

        receiver.generatePrivateKey(12);
        receiver.generatePublicKey();

        unsecureChannel.setSenderPublicKey(sender.publishPublicKey());
        unsecureChannel.setReceiverPublicKey(receiver.publishPublicKey());

        System.out.println("Sender public key: " + unsecureChannel.getSenderPublicKey());
        System.out.println("Receiver public key: " + unsecureChannel.getReceiverPublicKey());
        System.out.println("Now sender will send a file with secret key");

        sender.encryptFile_publish(unsecureChannel.getReceiverPublicKey());

        System.out.println("Now receiver will read it");
        receiver.decryptFile(unsecureChannel.getSenderPublicKey());
    }
}
