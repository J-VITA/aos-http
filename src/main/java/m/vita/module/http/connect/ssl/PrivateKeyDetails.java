package m.vita.module.http.connect.ssl;

import java.security.cert.X509Certificate;
import java.util.Arrays;

import m.vita.module.http.util.Args;

public final class PrivateKeyDetails {

    private final String type;
    private final X509Certificate[] certChain;

    public PrivateKeyDetails(final String type, final X509Certificate[] certChain) {
        super();
        this.type = Args.notNull(type, "Private key type");
        this.certChain = certChain;
    }

    public String getType() {
        return type;
    }

    public X509Certificate[] getCertChain() {
        return certChain;
    }

    @Override
    public String toString() {
        return type + ':' + Arrays.toString(certChain);
    }

}
