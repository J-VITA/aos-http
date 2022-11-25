package m.vita.module.http.client.impl.entity;

import java.io.IOException;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.HttpMessage;
import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.entity.BasicHttpEntity;
import m.vita.module.http.entity.ContentLengthStrategy;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.header.Header;
import m.vita.module.http.io.ChunkedInputStream;
import m.vita.module.http.io.ContentLengthInputStream;
import m.vita.module.http.io.IdentityInputStream;
import m.vita.module.http.io.SessionInputBuffer;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HTTP;

@Immutable // assuming injected dependencies are immutable
public class EntityDeserializer {

    private final ContentLengthStrategy lenStrategy;

    public EntityDeserializer(final ContentLengthStrategy lenStrategy) {
        super();
        this.lenStrategy = Args.notNull(lenStrategy, "Content length strategy");
    }

    /**
     * Creates a {@link BasicHttpEntity} based on properties of the given
     * message. The content of the entity is created by wrapping
     * {@link SessionInputBuffer} with a content decoder depending on the
     * transfer mechanism used by the message.
     * <p>
     * This method is called by the public
     * {@link #deserialize(SessionInputBuffer, HttpMessage)}.
     *
     * @param inbuffer the session input buffer.
     * @param message the message.
     * @return HTTP entity.
     * @throws HttpException in case of HTTP protocol violation.
     * @throws IOException in case of an I/O error.
     */
    protected BasicHttpEntity doDeserialize(
            final SessionInputBuffer inbuffer,
            final HttpMessage message) throws HttpException, IOException {
        final BasicHttpEntity entity = new BasicHttpEntity();

        final long len = this.lenStrategy.determineLength(message);
        if (len == ContentLengthStrategy.CHUNKED) {
            entity.setChunked(true);
            entity.setContentLength(-1);
            entity.setContent(new ChunkedInputStream(inbuffer));
        } else if (len == ContentLengthStrategy.IDENTITY) {
            entity.setChunked(false);
            entity.setContentLength(-1);
            entity.setContent(new IdentityInputStream(inbuffer));
        } else {
            entity.setChunked(false);
            entity.setContentLength(len);
            entity.setContent(new ContentLengthInputStream(inbuffer, len));
        }

        final Header contentTypeHeader = message.getFirstHeader(HTTP.CONTENT_TYPE);
        if (contentTypeHeader != null) {
            entity.setContentType(contentTypeHeader);
        }
        final Header contentEncodingHeader = message.getFirstHeader(HTTP.CONTENT_ENCODING);
        if (contentEncodingHeader != null) {
            entity.setContentEncoding(contentEncodingHeader);
        }
        return entity;
    }

    /**
     * Creates an {@link HttpEntity} based on properties of the given message.
     * The content of the entity is created by wrapping
     * {@link SessionInputBuffer} with a content decoder depending on the
     * transfer mechanism used by the message.
     * <p>
     * The content of the entity is NOT retrieved by this method.
     *
     * @param inbuffer the session input buffer.
     * @param message the message.
     * @return HTTP entity.
     * @throws HttpException in case of HTTP protocol violation.
     * @throws IOException in case of an I/O error.
     */
    public HttpEntity deserialize(
            final SessionInputBuffer inbuffer,
            final HttpMessage message) throws HttpException, IOException {
        Args.notNull(inbuffer, "Session input buffer");
        Args.notNull(message, "HTTP message");
        return doDeserialize(inbuffer, message);
    }

}
