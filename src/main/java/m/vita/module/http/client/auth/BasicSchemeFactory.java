package m.vita.module.http.client.auth;

import java.nio.charset.Charset;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.header.HttpContext;
import m.vita.module.http.header.HttpParams;

@Immutable
public class BasicSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {

    private final Charset charset;

    /**
     * @since 4.3
     */
    public BasicSchemeFactory(final Charset charset) {
        super();
        this.charset = charset;
    }

    public BasicSchemeFactory() {
        this(null);
    }

    public AuthScheme newInstance(final HttpParams params) {
        return new BasicScheme();
    }

    public AuthScheme create(final HttpContext context) {
        return new BasicScheme(this.charset);
    }

}
