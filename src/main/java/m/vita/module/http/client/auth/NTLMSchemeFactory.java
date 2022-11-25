package m.vita.module.http.client.auth;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;

@Immutable
public class NTLMSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {

    public AuthScheme newInstance(final HttpParams params) {
        return new NTLMScheme();
    }

    public AuthScheme create(final HttpContext context) {
        return new NTLMScheme();
    }

}
