package m.vita.module.http.exception;

public class TruncatedChunkException extends MalformedChunkCodingException {

    private static final long serialVersionUID = -23506263930279460L;

    /**
     * Creates a TruncatedChunkException with the specified detail message.
     *
     * @param message The exception detail message
     */
    public TruncatedChunkException(final String message) {
        super(message);
    }

}
