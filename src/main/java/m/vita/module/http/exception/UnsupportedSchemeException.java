package m.vita.module.http.exception;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;

@Immutable
public class UnsupportedSchemeException extends IOException {

    private static final long serialVersionUID = 3597127619218687636L;

    /**
     * Creates a UnsupportedSchemeException with the specified detail message.
     */
    public UnsupportedSchemeException(final String message) {
        super(message);
    }

}