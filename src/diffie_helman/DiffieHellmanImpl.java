package diffie_helman;

import java.math.BigInteger;

public class DiffieHellmanImpl implements DiffieHellman{

    private BigInteger primerNumber_p;
    private BigInteger anyNumber_alpha; // between {0,... primerNumber_p-2}

    public DiffieHellmanImpl(){
        diffieHellmanSetup();
    }


    private void diffieHellmanSetup(){
        primerNumber_p = BigInteger.valueOf(29);
        anyNumber_alpha = BigInteger.valueOf(2);
    }

    public BigInteger publishPrimeNumberToUnsecureChannel(){
        return this.primerNumber_p;
    }

    public BigInteger publishAnyNumberAlphaToUnsecureChannel(){
        return this.anyNumber_alpha;
    }

}
