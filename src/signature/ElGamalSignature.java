package signature;

import java.math.BigInteger;
import java.util.Random;

public class ElGamalSignature {

    private BigInteger primeModulus = new BigInteger(
            "b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6eddef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323",
            16);
    private BigInteger generator = new BigInteger(
            "44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a0267c9defe4119f2e373388cfa350a4e66e432d638ccdc58eb703e31d4c84e50398f9f91677e88641a2d2f6157e2f4ec538088dcf5940b053c622e53bab0b4e84b1465f5738f549664bd7430961d3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68",
            16);


    private static BigInteger generateRandomValue(BigInteger primeModulus, int min) {
        BigInteger randomValue;
        Random rand = new Random();
        do {
            randomValue = new BigInteger(primeModulus.bitLength(), rand);
        } while (randomValue.compareTo(primeModulus.subtract(BigInteger.ONE)) != -1
                && randomValue.compareTo(BigInteger.valueOf(min)) != 1);
        return randomValue;
    }
}
