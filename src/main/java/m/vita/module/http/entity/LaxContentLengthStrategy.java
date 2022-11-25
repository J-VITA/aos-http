package m.vita.module.http.entity;

import m.vita.module.http.HttpMessage;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.exception.ParseException;
import m.vita.module.http.exception.ProtocolException;
import m.vita.module.http.header.Header;
import m.vita.module.http.header.HeaderElement;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HTTP;

@Immutable
public class LaxContentLengthStrategy implements ContentLengthStrategy {

    public static final LaxContentLengthStrategy INSTANCE = new LaxContentLengthStrategy();

    private final int implicitLen;

    /**
     * Creates <tt>LaxContentLengthStrategy</tt> instance with the given length used per default
     * when content length is not explicitly specified in the message.
     *
     * @param implicitLen implicit content length.
     *
     * @since 4.2
     */
    public LaxContentLengthStrategy(final int implicitLen) {
        super();
        this.implicitLen = implicitLen;
    }

    /**
     * Creates <tt>LaxContentLengthStrategy</tt> instance. {@link ContentLengthStrategy#IDENTITY}
     * is used per default when content length is not explicitly specified in the message.
     */
    public LaxContentLengthStrategy() {
        this(IDENTITY);
    }

    public long determineLength(final HttpMessage message) throws HttpException {
        Args.notNull(message, "HTTP message");

        final Header transferEncodingHeader = message.getFirstHeader(HTTP.TRANSFER_ENCODING);
        // We use Transfer-Encoding if present and ignore Content-Length.
        // RFC2616, 4.4 item number 3
        if (transferEncodingHeader != null) {
            final HeaderElement[] encodings;
            try {
                encodings = transferEncodingHeader.getElements();
            } catch (final ParseException px) {
                throw new ProtocolException
                        ("Invalid Transfer-Encoding header value: " +
                                transferEncodingHeader, px);
            }
            // The chunked encoding must be the last one applied RFC2616, 14.41
            final int len = encodings.length;
            if (HTTP.IDENTITY_CODING.equalsIgnoreCase(transferEncodingHeader.getValue())) {
                return IDENTITY;
            } else if ((len > 0) && (HTTP.CHUNK_CODING.equalsIgnoreCase(
                    encodings[len - 1].getName()))) {
                return CHUNKED;
            } else {
                return IDENTITY;
            }
        }
        final Header contentLengthHeader = message.getFirstHeader(HTTP.CONTENT_LEN);
        if (contentLengthHeader != null) {
            long contentlen = -1;
            final Header[] headers = message.getHeaders(HTTP.CONTENT_LEN);
            for (int i = headers.length - 1; i >= 0; i--) {
                final Header header = headers[i];
                try {
                    contentlen = Long.parseLong(header.getValue());
                    break;
                } catch (final NumberFormatException ignore) {
                }
                // See if we can have better luck with another header, if present
            }
            if (contentlen >= 0) {
                return contentlen;
            } else {
                return IDENTITY;
            }
        }
        return this.implicitLen;
    }

}
