package m.vita.module.http.exception;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;

@Immutable
public class ClientProtocolException extends IOException {

    private static final long serialVersionUID = -5596590843227115865L;

    public ClientProtocolException() {
        super();
    }

    public ClientProtocolException(final String s) {
        super(s);
    }

    public ClientProtocolException(final Throwable cause) {
        initCause(cause);
    }

    public ClientProtocolException(final String message, final Throwable cause) {
        super(message);
        initCause(cause);
    }


}
