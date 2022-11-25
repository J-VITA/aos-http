package m.vita.module.http.connect.ssl;


import m.vita.module.http.annotation.Immutable;

@Immutable
public class AllowAllHostnameVerifier extends AbstractVerifier {

    public final void verify(
            final String host,
            final String[] cns,
            final String[] subjectAlts) {
        // Allow everything - so never blowup.
    }

    @Override
    public final String toString() {
        return "ALLOW_ALL";
    }

}