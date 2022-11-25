package m.vita.module.http.exception;

import m.vita.module.http.annotation.Immutable;

@Immutable
public class HttpResponseException extends ClientProtocolException {

    private static final long serialVersionUID = -7186627969477257933L;

    private final int statusCode;

    public HttpResponseException(final int statusCode, final String s) {
        super(s);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

}