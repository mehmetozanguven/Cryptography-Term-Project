# Cryptography Term Project

> You may find the term project assignment in [this](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/assignment/CENG471_Term%20Assignment_v2.pdf) link

> Note: In the project, due to measure the performance, there are 3 different types of files that will be encrypted/decrypted/signed/verified.
>
> One-page-length is the file type which contains only one page characters
>
> Ten-page-length is the file type which contains ten page characters
>
> ....

This is my cryptography term project which implemented stage by stage during the spring term of Cryptography 471(2019). This project includes:

- [AES or DES with Diffie-Hellman Scheme](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/diffie_helman_scheme/DiffieHellmanScheme.java)
- [RSA Scheme](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/rsa_scheme/RSAScheme.java)
- [DSS(Digital Signature Signing and Verification) Scheme](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/dss_scheme/DSSScheme.java)

To implement these schemes, I also implemented following properties/algorithms/procedures:

- [Extended Euclidean Algorithm](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/extended_euclidean_algorithm/ExtendedEuclideanAlgorithmImpl.java)
- [Fermat Little Theorem](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/fermat_little_theorem/FermatLittleTheoremImpl.java)
- [Fast Exponentiation](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/fast_exponentiation/FastExponentiationImpl.java)

To run any stage, just uncomment the following statements in the [Main.java](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/Main.java):

````java
public class Main {
    public static void main(String [] args){
        //DiffieHellmanScheme.startDiffieHellmanScheme();
        //RSAScheme.startRSAScheme();
        //DSSScheme.startDSSScheme();
    }
}

````


## First Stage - AES or DES with Diffie-Hellman Scheme

First of all, I created `PerformanceMeasurement` singleton instance to measure performance of the encryption/decryption process

In this stage, we were free to choose AES or DES. I chose the DES to encrypt & decrypt the files. You may find [DESEncryption.java](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/encryption/des/DESEncryption.java)

1. Create a unsecure channel which represents the Internet

````java
        DiffieHellmanUnsecureChannel unsecureChannel = new UnsecureChannel();
````

2. Then set the prime number(p) and alpha number(a), these are the public numbers and I will need these number for Diffie-Hellman Key exchange

````java
unsecureChannel.diffieHellmanSetup();
````

3. Create the sender and receiver parties and set the prime number(p) and alpha number(a)

````java
Person sender = new Sender();
sender.setPrimeNumber_p_from_unsecureChannel(unsecureChannel.getPrimeNum_p());
sender.setAnyNumber_alpha_from_unsecureChannel(unsecureChannel.getAnyNum_alpha());

Person receiver = new Receiver();
receiver.setPrimeNumber_p_from_unsecureChannel(unsecureChannel.getPrimeNum_p());
receiver.setAnyNumber_alpha_from_unsecureChannel(unsecureChannel.getAnyNum_alpha());

````

4. Sender will generate his/her private and public key respectively

````java
sender.generatePrivateKey();
sender.generatePublicKey();
````

5. After that sender will send its public key to the Internet(Unsecure Channel)

````java
unsecureChannel.setDiffieHellmanSenderPublicKey(sender.publishPublicKey());
````

6. Receiver will generate his/her private and public key respectively and also send his/her public key to the Internet(Unsecure Channel)

````java
receiver.generatePrivateKey();
receiver.generatePublicKey();

unsecureChannel.setDiffieHellmanReceiverPublicKey(receiver.publishPublicKey());
````

7. An this steps, Diffie-Hellman key exchange is done. Because both parties can generate common secret key. For example, sender can generate the common secret via:

````java
unsecureChannel.getDiffieHellmanReceiverPublicKey(); // receiver will get the sender public key A
unsecureChannel.getPrimeNum_p(); // sender will get the prime number(p)
unsecureChannel.getAnyNum_alpha(); // sender will get the alpha number(a)
// then sender will do: A^b mod p (where b is the sender private key)
// this step is done in the encrpytFile_publish() method
````

8. After that, sender will encrypt the files(one-page-length, ten-page-length, hundred-page-length, thousand-page-length) and publish it

````java
sender.encryptFile_publish(unsecureChannel.getDiffieHellmanReceiverPublicKey(), i); // where i is the array index which represents the file types (i = 1 => one-page-length file, i = 10 => ten-page length file ...)
````

9. At the same time, I am going to measure the performance of encryption & decryption process via `performanceMeasurement` instance