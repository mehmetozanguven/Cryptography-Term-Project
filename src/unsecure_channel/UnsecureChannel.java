package unsecure_channel;

import fermat_little_theorem.FermatLittleTheorem;
import fermat_little_theorem.FermatLittleTheoremImpl;

import java.math.BigInteger;
import java.util.Random;

public class UnsecureChannel {
    private BigInteger primeNum_p;
    private BigInteger anyNum_alpha;
    private BigInteger senderPublicKey;
    private BigInteger receiverPublicKey;
    private FermatLittleTheorem fermatLittleTheorem;

    public UnsecureChannel(){
        fermatLittleTheorem = new FermatLittleTheoremImpl();
        generateBigIntegerPrimeNumber();
        generateNumberAlpha();
    }

    /**
     * Generate big prime integer
     * To decide whether big integer is prime or not, I am using Fermat's Little Theorem
     * In fermat's little theorem:
     *      a^{p-1} = 1 (mod p) where p is the primeNumCandidate in the code.
     */
    private void generateBigIntegerPrimeNumber(){
        BigInteger base = BigInteger.valueOf(2);
        boolean isPrimeNumberGenerated = false;

        while (!isPrimeNumberGenerated){
            Random rnd = new Random();
            BigInteger primeNumCandidate = new BigInteger(64, rnd);
            BigInteger primeNumCandidate_minus1 = primeNumCandidate.subtract(BigInteger.ONE);
            fermatLittleTheorem.calculateFermatLittleTheorem(base,primeNumCandidate_minus1,primeNumCandidate);
            BigInteger fermatLittleTheoremResult = fermatLittleTheorem.getFermatLittleTheoremResult();
            if (fermatLittleTheoremResult.compareTo(BigInteger.ONE) == 0){
                primeNum_p = primeNumCandidate;
                isPrimeNumberGenerated = true;
            }
        }
    }

    /**
     * Choose number alpha (a) from the {... p-2} where p is the prime number
     */
    private void generateNumberAlpha(){
        BigInteger primeNum_minus2 = primeNum_p.subtract(BigInteger.valueOf(2));
        Random rnd = new Random();
        BigInteger alphaNumCandidate = new BigInteger(primeNum_minus2.bitLength(), rnd);
        anyNum_alpha = alphaNumCandidate;
    }

    public BigInteger getPrimeNum_p() {
        return primeNum_p;
    }

    public BigInteger getAnyNum_alpha() {
        return anyNum_alpha;
    }

    public BigInteger getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(BigInteger senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public BigInteger getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setReceiverPublicKey(BigInteger receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }
}
