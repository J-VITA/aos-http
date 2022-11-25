package m.vita.module.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.ContentType;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.HTTP;

@NotThreadSafe
public class StringEntity extends AbstractHttpEntity implements Cloneable {

    protected final byte[] content;

    /**
     * Creates a StringEntity with the specified content and content type.
     *
     * @param string content to be used. Not {@code null}.
     * @param contentType content type to be used. May be {@code null}, in which case the default
     *   MIME type {@link ContentType#TEXT_PLAIN} is assumed.
     *
     * @throws IllegalArgumentException if the string parameter is null
     * @throws UnsupportedCharsetException Thrown when the named charset is not available in
     * this instance of the Java virtual machine
     * @since 4.2
     */
    public StringEntity(final String string, final ContentType contentType) throws UnsupportedCharsetException {
        super();
        Args.notNull(string, "Source string");
        Charset charset = contentType != null ? contentType.getCharset() : null;
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }
        try {
            this.content = string.getBytes(charset.name());
        } catch (final UnsupportedEncodingException ex) {
            // should never happen
            throw new UnsupportedCharsetException(charset.name());
        }
        if (contentType != null) {
            setContentType(contentType.toString());
        }
    }

    /**
     * Creates a StringEntity with the specified content, MIME type and charset
     *
     * @param string content to be used. Not {@code null}.
     * @param mimeType MIME type to be used. May be {@code null}, in which case the default
     *   is {@link HTTP#PLAIN_TEXT_TYPE} i.e. "text/plain"
     * @param charset character set to be used. May be {@code null}, in which case the default
     *   is {@link HTTP#DEF_CONTENT_CHARSET} i.e. "ISO-8859-1"
     * @throws  UnsupportedEncodingException If the named charset is not supported.
     *
     * @since 4.1
     * @throws IllegalArgumentException if the string parameter is null
     *
     * @deprecated (4.1.3) use {@link #StringEntity(String, ContentType)}
     */
    @Deprecated
    public StringEntity(
            final String string, final String mimeType, final String charset) throws UnsupportedEncodingException {
        super();
        Args.notNull(string, "Source string");
        final String mt = mimeType != null ? mimeType : HTTP.PLAIN_TEXT_TYPE;
        final String cs = charset != null ? charset :HTTP.DEFAULT_CONTENT_CHARSET;
        this.content = string.getBytes(cs);
        setContentType(mt + HTTP.CHARSET_PARAM + cs);
    }

    /**
     * Creates a StringEntity with the specified content and charset. The MIME type defaults
     * to "text/plain".
     *
     * @param string content to be used. Not {@code null}.
     * @param charset character set to be used. May be {@code null}, in which case the default
     *   is {@link HTTP#DEF_CONTENT_CHARSET} is assumed
     *
     * @throws IllegalArgumentException if the string parameter is null
     * @throws UnsupportedCharsetException Thrown when the named charset is not available in
     * this instance of the Java virtual machine
     */
    public StringEntity(final String string, final String charset)
            throws UnsupportedCharsetException {
        this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
    }

    /**
     * Creates a StringEntity with the specified content and charset. The MIME type defaults
     * to "text/plain".
     *
     * @param string content to be used. Not {@code null}.
     * @param charset character set to be used. May be {@code null}, in which case the default
     *   is {@link HTTP#DEF_CONTENT_CHARSET} is assumed
     *
     * @throws IllegalArgumentException if the string parameter is null
     *
     * @since 4.2
     */
    public StringEntity(final String string, final Charset charset) {
        this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
    }

    /**
     * Creates a StringEntity with the specified content. The content type defaults to
     * {@link ContentType#TEXT_PLAIN}.
     *
     * @param string content to be used. Not {@code null}.
     *
     * @throws IllegalArgumentException if the string parameter is null
     * @throws UnsupportedEncodingException if the default HTTP charset is not supported.
     */
    public StringEntity(final String string)
            throws UnsupportedEncodingException {
        this(string, ContentType.DEFAULT_TEXT);
    }

    public boolean isRepeatable() {
        return true;
    }

    public long getContentLength() {
        return this.content.length;
    }

    public InputStream getContent() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    public void writeTo(final OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        outstream.write(this.content);
        outstream.flush();
    }

    /**
     * Tells that this entity is not streaming.
     *
     * @return <code>false</code>
     */
    public boolean isStreaming() {
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

} // class StringEntity
