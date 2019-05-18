package person.dss_signature_person;

import file_handler.FilePathHandler;
import file_handler.FileToStringConverter;
import signature.DSASignature;

import java.math.BigInteger;

/**
 * Abstract class for DSA scheme(receiver and sender)
 */
public abstract class Person {

    private String fileAsString;
    private DSASignature DSASignature;
    private BigInteger[] signParameter;
    private BigInteger[] publicParameter;

    public Person() {
        this.DSASignature = new DSASignature();
    }

    /**
     * Generates essential keys for DSA algorithm
     * To generate keys, invoke the keyGeneration method in DSASignature class
     * Then get the public parameter as an array
     */
    public void DSAKeyGeneration(){
        DSASignature.keyGeneration();
        publicParameter = DSASignature.getPublicParameter();
    }

    /**
     * Sign the file
     * @param fileType represents the file type.
     *                 0 => file which is one page length
     *                 1 =>               ten page length
     *                 2 =>               hundred page length
     *                 others =>          thousand page length
     *
     *  After learning file type, get the file content as string
     *  then sign that string(file content) with invoking signMessage method in DSASignature
     *  after all, get the signature parameter(r,s) as an array
     */
    public void signFile(int fileType){
        if (fileType == 0)
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.ONE_PAGE_PLAINTEXT_PATH);
        else if (fileType == 1)
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.TEN_PAGE_PLAINTEXT_PATH);
        else if (fileType == 2)
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.HUNDRED_PAGE_PLAINTEXT_PATH);
        else
            fileAsString = FileToStringConverter.getFileContentAsString(FilePathHandler.THOUSAND_PAGE_PLAINTEXT_PATH);

        DSASignature.signMessage(fileAsString);
        signParameter =  DSASignature.getSignParameter();
    }

    /**
     * @return signature parameter
     */
    public BigInteger[] getSignParameter(){
       return signParameter;
    }

    /**
     * @return public key parameter
     */
    public BigInteger[] getPublicParameter(){
        return publicParameter;
    }

    /**
     * @return file content as string
     */
    public String getFileAsString(){
        return fileAsString;
    }

    /**
     * Verify the message
     * @param message that will be verified
     * @param signParameter is the sender's signature parameter
     * @param publicParameter is the sender's receiver public key parameter
     * invoke the method verifyMessage in DSASignature
     */
    public void verifyMessage(String message, BigInteger[] signParameter, BigInteger[] publicParameter){
        DSASignature.verifyMessage(message, signParameter, publicParameter);
    }




}
