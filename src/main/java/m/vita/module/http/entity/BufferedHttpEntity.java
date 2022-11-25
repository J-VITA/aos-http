package m.vita.module.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import m.vita.module.http.HttpEntity;
import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.config.HttpEntityWrapper;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.EntityUtils;

@NotThreadSafe
public class BufferedHttpEntity extends HttpEntityWrapper {

    private final byte[] buffer;

    /**
     * Creates a new buffered entity wrapper.
     *
     * @param entity   the entity to wrap, not null
     * @throws IllegalArgumentException if wrapped is null
     */
    public BufferedHttpEntity(final HttpEntity entity) throws IOException {
        super(entity);
        if (!entity.isRepeatable() || entity.getContentLength() < 0) {
            this.buffer = EntityUtils.toByteArray(entity);
        } else {
            this.buffer = null;
        }
    }

    @Override
    public long getContentLength() {
        if (this.buffer != null) {
            return this.buffer.length;
        } else {
            return super.getContentLength();
        }
    }

    @Override
    public InputStream getContent() throws IOException {
        if (this.buffer != null) {
            return new ByteArrayInputStream(this.buffer);
        } else {
            return super.getContent();
        }
    }

    /**
     * Tells that this entity does not have to be chunked.
     *
     * @return  <code>false</code>
     */
    @Override
    public boolean isChunked() {
        return (buffer == null) && super.isChunked();
    }

    /**
     * Tells that this entity is repeatable.
     *
     * @return  <code>true</code>
     */
    @Override
    public boolean isRepeatable() {
        return true;
    }


    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        if (this.buffer != null) {
            outstream.write(this.buffer);
        } else {
            super.writeTo(outstream);
        }
    }


    // non-javadoc, see interface HttpEntity
    @Override
    public boolean isStreaming() {
        return (buffer == null) && super.isStreaming();
    }

} // class BufferedHttpEntity
