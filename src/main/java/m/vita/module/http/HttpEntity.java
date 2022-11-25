package m.vita.module.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import m.vita.module.http.connect.HttpConnection;
import m.vita.module.http.header.Header;
import m.vita.module.http.util.EntityUtils;

public interface HttpEntity {
    /**
     * Tells if the entity is capable of producing its data more than once.
     * A repeatable entity's getContent() and writeTo(OutputStream) methods
     * can be called more than once whereas a non-repeatable entity's can not.
     * @return true if the entity is repeatable, false otherwise.
     */
    boolean isRepeatable();

    /**
     * Tells about chunked encoding for this entity.
     * The primary purpose of this method is to indicate whether
     * chunked encoding should be used when the entity is sent.
     * For entities that are received, it can also indicate whether
     * the entity was received with chunked encoding.
     * <br/>
     * The behavior of wrapping entities is implementation dependent,
     * but should respect the primary purpose.
     *
     * @return  <code>true</code> if chunked encoding is preferred for this
     *          entity, or <code>false</code> if it is not
     */
    boolean isChunked();

    /**
     * Tells the length of the content, if known.
     *
     * @return  the number of bytes of the content, or
     *          a negative number if unknown. If the content length is known
     *          but exceeds {@link Long#MAX_VALUE Long.MAX_VALUE},
     *          a negative number is returned.
     */
    long getContentLength();

    /**
     * Obtains the Content-Type header, if known.
     * This is the header that should be used when sending the entity,
     * or the one that was received with the entity. It can include a
     * charset attribute.
     *
     * @return  the Content-Type header for this entity, or
     *          <code>null</code> if the content type is unknown
     */
    Header getContentType();

    /**
     * Obtains the Content-Encoding header, if known.
     * This is the header that should be used when sending the entity,
     * or the one that was received with the entity.
     * Wrapping entities that modify the content encoding should
     * adjust this header accordingly.
     *
     * @return  the Content-Encoding header for this entity, or
     *          <code>null</code> if the content encoding is unknown
     */
    Header getContentEncoding();

    /**
     * Returns a content stream of the entity.
     * {@link #isRepeatable Repeatable} entities are expected
     * to create a new instance of {@link InputStream} for each invocation
     * of this method and therefore can be consumed multiple times.
     * Entities that are not {@link #isRepeatable repeatable} are expected
     * to return the same {@link InputStream} instance and therefore
     * may not be consumed more than once.
     * <p>
     * IMPORTANT: Please note all entity implementations must ensure that
     * all allocated resources are properly deallocated after
     * the {@link InputStream#close()} method is invoked.
     *
     * @return content stream of the entity.
     *
     * @throws IOException if the stream could not be created
     * @throws IllegalStateException
     *  if content stream cannot be created.
     *
     * @see #isRepeatable()
     */
    InputStream getContent() throws IOException, IllegalStateException;

    /**
     * Writes the entity content out to the output stream.
     * <p>
     * <p>
     * IMPORTANT: Please note all entity implementations must ensure that
     * all allocated resources are properly deallocated when this method
     * returns.
     *
     * @param outstream the output stream to write entity content to
     *
     * @throws IOException if an I/O error occurs
     */
    void writeTo(OutputStream outstream) throws IOException;

    /**
     * Tells whether this entity depends on an underlying stream.
     * Streamed entities that read data directly from the socket should
     * return <code>true</code>. Self-contained entities should return
     * <code>false</code>. Wrapping entities should delegate this call
     * to the wrapped entity.
     *
     * @return  <code>true</code> if the entity content is streamed,
     *          <code>false</code> otherwise
     */
    boolean isStreaming(); // don't expect an exception here

    /**
     * This method is deprecated since version 4.1. Please use standard
     * java convention to ensure resource deallocation by calling
     * {@link InputStream#close()} on the input stream returned by
     * {@link #getContent()}
     * <p>
     * This method is called to indicate that the content of this entity
     * is no longer required. All entity implementations are expected to
     * release all allocated resources as a result of this method
     * invocation. Content streaming entities are also expected to
     * dispose of the remaining content, if any. Wrapping entities should
     * delegate this call to the wrapped entity.
     * <p>
     * This method is of particular importance for entities being
     * received from a {@link HttpConnection connection}. The entity
     * needs to be consumed completely in order to re-use the connection
     * with keep-alive.
     *
     * @throws IOException if an I/O error occurs.
     *
     * @deprecated (4.1) Use {@link EntityUtils#consume(HttpEntity)}
     *
     * @see #getContent() and #writeTo(OutputStream)
     */
    @Deprecated
    void consumeContent() throws IOException;
}
