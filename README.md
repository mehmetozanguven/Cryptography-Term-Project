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


## Second Stage - RSA Encryption & Decryption

In this stage, sender & receiver will generate their own RSA public-private key pairs.
Then according to the RSA implementation, they will encrypt/decrypt the files
Measure the performance of encryption/decryption process

In this stage, we were not allowed to use any RSA implementation libraries. Then, I had to create new class.
- [BigNumberGenerator](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/bignumber_generator/BigNumberGenerator.java) to generate big prime integers p & q

Even technically possible, RSA is not an appropriate choice for big files encryption/decryption. Because:
- RSA encrypts "messages" of limited size
- RSA is a slower algorithm for big file sizes

Then I came up with the solution called Hybrid model:
- Using asymmetric and symmetric encryption/decryption.
- One of the problem in symmetric encryption is to sharing the key.
- To solve that problem, I used the RSA algorithm.

In hybrid encryption:
- Sender will generate random DES key
- Sender will encrypt the files with that DES key.
- Then sender will encrypt the DES key with RSA(via Receiver's public key) and publish to unsecure channel
(Internet)
- Receiver will decrypt the encrypted DES key with its private key.
- Then receiver will be able to decrypt the files.

1. Create a unsecure channel which represents the Internet

````java
RSAUnsecureChannel unsecureChannel = new UnsecureChannel();
````

2. Create [RSAEncryption](https://github.com/mehmetozanguven/Cryptography-Term-Project/blob/master/src/encryption/rsa/RSAEncryption.java) and DESEncryption (implemented in the first stage)

````java
RSAEncryption rsaEncryption = new RSAEncryption();
DESEncryption desEncryption = new DESEncryption();
````

3. Create a sender person

````java
Person sender = new Sender();
````

4. Set the RSA bit length

````java
sender.setBitLength(BIT_LENGTH);
````

5. Generate large prime number p & q

````java
sender.generateLargePrimeNumber_p();
sender.generateLargePrimeNumber_q();
````

6. Compute number N = p*q

````java
sender.compute_number_n();
````

7. Compute Euler's phi function T = (p-1)*(q-1)

````java
sender.computeEulerPhiFunction();
````

8. Compute public and private key

````java
sender.computePublicKey();
sender.computePrivateKey();
````

9. Generate random DES key to encrypt the files

````java
sender.generateRandomDESKey();
````



Apply the same processes to the receiver instance (`Person receiver = new Receiver()` )



After that, receiver will publish his/her public key to the Internet

````java
unsecureChannel.setReceiverPublicKeyPair(receiver.getPublicKeyPair());
````

Right now, sender will get this pair(receiver's public key). (therefore receiver will have shared its public key with sender) from the unsecure channel to encrypt his/her random DES key

````java
sender.encryptRandomDESKeyWithRSA(unsecureChannel.getReceiverPublicKey(), rsaEncryption); // sender encrpyts the DES key with Receiver's public key
````



After encrypting DES key, sender will now publish the encrypted DES key to Internet

````java
unsecureChannel.setSenderRandomDESWithRSAEncryption(sender.getRandomDESKeyWithRSAEncryption());
````

Then sender will publish his/her public key to Internet

````java
unsecureChannel.setSenderPublicKeyPair(sender.getPublicKeyPair());
````

After all, now sender can encrypt the files and publish them:

````java
sender.encryptFile_publish(i, desEncryption);// where i represents the file's type
````

Then receiver can decrypt the files:

````java
receiver.decryptFile(i, rsaEncryption, unsecureChannel.getSenderRandomDESWithRSAEncryption(), desEncryption);
````

## Third Stage - DSS

### How identification works in this stage?
- In this stage, sender will sign the message with his/her private key. That's means only one who has the
  sender's public key can verify the message. Therefore receiver can be sure about the sender. Because
  not other fake keys will not work.

### How integrity works in this stage?
- In this stage, integrity can be applied via hash function. Because hash function is one-way property
  function and output of hash function will be unique for the message. Therefore, if any bit changes in the
  message, hash function output will be different, then we can say that "sender's message is changed
  during the transmission"

### How non-repudiation works in this stage?
- In DSA algorithm, we create signature with sender's private key. That's means only one who has the
  sender's public key can verify the message. Therefore we can be sure that this message belongs to the
  sender not attacker or something else. Thus, sender can not deny the creation of the message.

### How to satisfy secrecy?
- To satisfy secrecy, I used encryption algorithm. Because of large data, I decided to use my DES
implementation.
- However there is key exchange problem in DES. To solve that issue, I used my RSA implementation.
- In other words, I used hybrid encryption where key exchange was done by RSA, and encryption of the
data was done by DES

