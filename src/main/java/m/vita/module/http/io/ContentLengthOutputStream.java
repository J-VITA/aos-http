package m.vita.module.http.io;

import java.io.IOException;
import java.io.OutputStream;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class ContentLengthOutputStream extends OutputStream {

    /**
     * Wrapped session output buffer.
     */
    private final SessionOutputBuffer out;

    /**
     * The maximum number of bytes that can be written the stream. Subsequent
     * write operations will be ignored.
     */
    private final long contentLength;

    /** Total bytes written */
    private long total = 0;

    /** True if the stream is closed. */
    private boolean closed = false;

    /**
     * Wraps a session output buffer and cuts off output after a defined number
     * of bytes.
     *
     * @param out The session output buffer
     * @param contentLength The maximum number of bytes that can be written to
     * the stream. Subsequent write operations will be ignored.
     *
     * @since 4.0
     */
    public ContentLengthOutputStream(final SessionOutputBuffer out, final long contentLength) {
        super();
        this.out = Args.notNull(out, "Session output buffer");
        this.contentLength = Args.notNegative(contentLength, "Content length");
    }

    /**
     * <p>Does not close the underlying socket output.</p>
     *
     * @throws IOException If an I/O problem occurs.
     */
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.out.flush();
        }
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (this.total < this.contentLength) {
            final long max = this.contentLength - this.total;
            int chunk = len;
            if (chunk > max) {
                chunk = (int) max;
            }
            this.out.write(b, off, chunk);
            this.total += chunk;
        }
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(final int b) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (this.total < this.contentLength) {
            this.out.write(b);
            this.total++;
        }
    }

}
