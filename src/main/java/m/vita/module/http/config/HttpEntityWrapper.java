package m.vita.module.http.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.Header;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class HttpEntityWrapper implements HttpEntity {

    /** The wrapped entity. */
    protected HttpEntity wrappedEntity;

    /**
     * Creates a new entity wrapper.
     */
    public HttpEntityWrapper(final HttpEntity wrappedEntity) {
        super();
        this.wrappedEntity = Args.notNull(wrappedEntity, "Wrapped entity");
    } // constructor

    public boolean isRepeatable() {
        return wrappedEntity.isRepeatable();
    }

    public boolean isChunked() {
        return wrappedEntity.isChunked();
    }

    public long getContentLength() {
        return wrappedEntity.getContentLength();
    }

    public Header getContentType() {
        return wrappedEntity.getContentType();
    }

    public Header getContentEncoding() {
        return wrappedEntity.getContentEncoding();
    }

    public InputStream getContent()
            throws IOException {
        return wrappedEntity.getContent();
    }

    public void writeTo(final OutputStream outstream)
            throws IOException {
        wrappedEntity.writeTo(outstream);
    }

    public boolean isStreaming() {
        return wrappedEntity.isStreaming();
    }

    /**
     * @deprecated (4.1) Either use {@link #getContent()} and call {@link InputStream#close()} on that;
     * otherwise call {@link #writeTo(OutputStream)} which is required to free the resources.
     */
    @Deprecated
    public void consumeContent() throws IOException {
        wrappedEntity.consumeContent();
    }

}
