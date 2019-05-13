
import dss_scheme.DSSScheme;
import rsa_scheme.RSAScheme;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private final static String P_HEX_STRING = "b59dd795 68817b4b 9f678982 2d22594f 376e6a9a bc024184 6de426e5 dd8f6edd"+
            "ef00b465 f38f509b 2b183510 64704fe7 5f012fa3 46c5e2c4 42d7c99e ac79b2bc"+
            "8a202c98 327b9681 6cb80426 98ed3734 643c4c05 164e739c b72fba24 f6156b6f"+
            "47a7300e f778c378 ea301e11 41a6b25d 48f19242 68c62ee8 dd313474 5cdf7323";

    private final static String G_HEX_STRING = "44ec9d52 c8f9189e 49cd7c70 253c2eb3 154dd4f0 8467a64a 0267c9de fe4119f2" +
            "e373388c fa350a4e 66e432d6 38ccdc58 eb703e31 d4c84e50 398f9f91 677e8864" +
            "1a2d2f61 57e2f4ec 538088dc f5940b05 3c622e53 bab0b4e8 4b1465f5 738f5496" +
            "64bd7430 961d3e5a 2e7bceb6 2418db74 7386a58f f267a993 9833beef b7a6fd68";

    //helper function to convert hexstrings copied from assignment description to BigInt
    //I've already manually removed newline characters.
    private static BigInteger hexStringToBigInt(String hexString){
        return new BigInteger(hexString.replaceAll("\\s+",""), 16);
    }


    private final static BigInteger PRIME_MODULUS_P = hexStringToBigInt(P_HEX_STRING);
    private final static BigInteger GENERATOR_G = hexStringToBigInt(G_HEX_STRING);

    //Used to calculate secret key x
    //learned about compare to and the random constructor from https://stackoverflow.com/questions/3735664/randomizing-a-biginteger
    private static BigInteger bigIntInRange(BigInteger min, BigInteger max){
        Random rnd = new Random();

        BigInteger x = new BigInteger(max.bitLength(), rnd);
        int count = 0; //Count for debugging purposes

        //while x <= min || x >= max
        //So far while testing I've only seen a max of 4 iterations of the loop.
        //Generally it's either 0 or 1 iterations
        while (x.compareTo(min) <= 0 || x.compareTo(max) >= 0){
            x = new BigInteger(max.bitLength(), rnd);
            count++;
        }
        System.out.println("Count for big int in range: " + count);
        return x;
    }

    //Right to left variant of calculating modular exponentiation.
    //Taken from my submission in assignment 1. Renamed method from performRSA to modularExponentiation
    private static BigInteger modularExponentiation(BigInteger base, BigInteger exponent, BigInteger mod){
        BigInteger y = new BigInteger("1");
        for(int i = 0; i < exponent.bitLength(); i++){
            if (exponent.testBit(i)) {
                y = y.multiply(base);
                y = y.mod(mod);
            }
            base = base.multiply(base);
            base = base.mod(mod);
        }
        return y;
    }

    private static BigInteger gcd(BigInteger a, BigInteger b){
        // Use Euclidean Algorithm of:
        //      gcd(a,0) = a
        //      gcd(a,b) = gcd(b, a mod b)

        // Base case gcd(a,0) = a
        if(b.compareTo(BigInteger.ZERO) == 0){
            return a;
        }

        //Recursive case gcd(a,b) = gcd(b, a mod b)
        else{
            return gcd(b, a.mod(b));
        }
    }

    //From spec: random value k with 0 < k < p-1 and gcd(k,p-1) = 1
    private static BigInteger generateK(BigInteger pMinusOne){
        BigInteger k = bigIntInRange(BigInteger.ZERO, pMinusOne);

        int count = 0;
        //So far while testing I've only seen a max of 6 iterations of the loop.
        while(gcd(k,pMinusOne).compareTo(BigInteger.ONE) != 0)
        {
            k = bigIntInRange(BigInteger.ZERO, pMinusOne);
            count++;
        }

        System.out.println("Count for generate k: " + count);
        return k;
    }


    //Explanation of pseudo-code: https://www.csee.umbc.edu/~chang/cs203.s09/exteuclid.shtml
    private static BigInteger[] xgcd(BigInteger a, BigInteger b){
        if(b.compareTo(BigInteger.ZERO) == 0){
            return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO}; // base case if b == 0 return (a,1,0)
        }

        //where recursiveCall = {d1, s1, t1}
        BigInteger[] recursiveCall = xgcd(b, a.mod(b));
        BigInteger d = recursiveCall[0]; //d = d1
        BigInteger s = recursiveCall[2]; //s = t1
        BigInteger t = recursiveCall[1].subtract(a.divide(b).multiply(recursiveCall[2])); //t = s1 - (a div b) * t1
        return new BigInteger[] {d,s,t};
    }


    //From your number theory notes http://www.computing.dcu.ie/~hamilton/teaching/CA4005/notes/Number1.pdf
    private static BigInteger multiplicativeInverse(BigInteger a, BigInteger N) throws ArithmeticException {
        //ensure gcd is one, i.e ensure d == 1
        BigInteger[] xgcdResult = xgcd(a,N);

        if(xgcdResult[0].compareTo(BigInteger.ONE) == 0){ //if d == 1
            return xgcdResult[1].mod(N); //return s mod N where s == x from the notes (page 8/16) http://www.computing.dcu.ie/~hamilton/teaching/CA4005/notes/Number1.pdf
        }
        else{
            throw new ArithmeticException("No modular inverse as d != 1. d == " + xgcdResult[0].toString());
        }
    }


    private static BigInteger[] generateRAndS(BigInteger secretKey, byte[] hashedMessageBytes){
        //Generate k
        BigInteger randomK = generateK(PRIME_MODULUS_P.subtract(BigInteger.ONE));

        //Generate r
        BigInteger r = modularExponentiation(GENERATOR_G,randomK, PRIME_MODULUS_P);

        //calculate H(m)-xr
        BigInteger hashedMessageBigInt = new BigInteger(hashedMessageBytes); //convert to bigInt
        BigInteger hmMinusXR = hashedMessageBigInt.subtract(secretKey.multiply(r));

        //calculate k^-1 mod(p-1)
        BigInteger kMultiplicativeInverse = multiplicativeInverse(randomK, PRIME_MODULUS_P.subtract(BigInteger.ONE));

        //compute s as H(m)-xr * k^-1 mod(p-1)
        BigInteger s = hmMinusXR.multiply(kMultiplicativeInverse).mod(PRIME_MODULUS_P.subtract(BigInteger.ONE));

        //if s == 0 start over again
        if(s.compareTo(BigInteger.ZERO) == 0)
        {
            BigInteger[] recursiveCall = generateRAndS(secretKey, hashedMessageBytes);
            r = recursiveCall[0];
            s = recursiveCall[1];
        }

        //return r and s
        return new BigInteger[] {r, s};
    }

    private static void writeAssignmentHandInToFile(String outputPath,
                                                    BigInteger publicKeyY,
                                                    BigInteger[] digitalSignature,
                                                    BigInteger secretKeyX,
                                                    byte[] unhashedFile,
                                                    byte[] hashedFile) throws IOException {
        PrintWriter printwriter = new PrintWriter(outputPath, "UTF-8");
        System.out.println("Writing assignment hand-in details to: " + outputPath);
        printwriter.println("Public Key Y: " + publicKeyY.toString(16));
        printwriter.println("\nSecret Key X: " + secretKeyX.toString(16)); //Include X just in case
        printwriter.println("\nDigital Signature Value R: " + digitalSignature[0].toString(16));
        printwriter.println("\nDigital Signature Value S: " +  digitalSignature[1].toString(16));

        //Including the unhashed and hashed files just in case.
        printwriter.println("\nUnhashed File: " + DatatypeConverter.printHexBinary(unhashedFile));
        printwriter.println("\nHashed File: " + DatatypeConverter.printHexBinary(hashedFile));

        printwriter.close();
    }


    private static boolean verify(String hexPublicKeyY,
                                  String hexR,
                                  String hexS,
                                  byte[] unhashedFile) throws NoSuchAlgorithmException{
        BigInteger y = hexStringToBigInt(hexPublicKeyY);
        BigInteger r = hexStringToBigInt(hexR);
        BigInteger s = hexStringToBigInt(hexS);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedFile = digest.digest(unhashedFile); //the hashed file
            BigInteger hm = new BigInteger(hashedFile);

            boolean rCheck = (r.compareTo(BigInteger.ZERO) > 0) && (r.compareTo(PRIME_MODULUS_P) < 0);
            boolean sCheck = (s.compareTo(BigInteger.ZERO) > 0) && (s.compareTo(PRIME_MODULUS_P.subtract(BigInteger.ONE)) < 0);


            //g^H(m) (mod p)
            BigInteger gHmModP = modularExponentiation(GENERATOR_G, hm,PRIME_MODULUS_P);
            //(y^r)(r^s) (mod p)
            BigInteger yRrSModP = modularExponentiation(y,r,PRIME_MODULUS_P).multiply(modularExponentiation(r,s,PRIME_MODULUS_P)).mod(PRIME_MODULUS_P);

            boolean signatureCheck = gHmModP.compareTo(yRrSModP) == 0;

            System.out.println("rCheck: " + rCheck + "\nsCheck " + sCheck + "\nSignature Check: " + signatureCheck);
            return rCheck && sCheck && signatureCheck;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new NoSuchAlgorithmException(e);
        }
    }

    public static void main(String [] args){
        String path = "files/tenPageLength/tenPageLength.txt";
        DSSScheme.startDSSScheme();

/*

        //Calculate secret key x
        BigInteger secretKeyX = bigIntInRange(BigInteger.ONE, PRIME_MODULUS_P.subtract(BigInteger.ONE));

        //Calculate public key y
        BigInteger publicKeyY = modularExponentiation(GENERATOR_G, secretKeyX, PRIME_MODULUS_P);

        //Read path to file
        Scanner scan = new Scanner(System.in, "UTF-8");
        System.out.println("Enter the path to the file: ");
        Path filePath = Paths.get(scan.next());

        try {
            //read in file
            byte[] fileData = Files.readAllBytes(filePath);

            //hash the file
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedFile = digest.digest(fileData); //the hashed file

            //Digitally sign the file
            BigInteger[] digitalSignature = generateRAndS(secretKeyX, hashedFile);

            //Write the assignment results to a file
            String outputFilePath = filePath.toString() + "-assignment-output.txt";
            writeAssignmentHandInToFile(outputFilePath, publicKeyY, digitalSignature, secretKeyX, fileData, hashedFile);


            //Verify
            // Declare variables here to make testing values from other runs manually at a later date easier, just copy and paste to here
            String hexY = publicKeyY.toString(16);
            String hexR = digitalSignature[0].toString(16);
            String hexS = digitalSignature[1].toString(16);
            byte[] unhashedMessage = fileData;

            //Run verification
            System.out.println("Verified: " + verify(hexY,hexR,hexS,unhashedMessage));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
*/
    }

}

