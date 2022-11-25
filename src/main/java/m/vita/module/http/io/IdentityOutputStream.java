package m.vita.module.http.io;

import java.io.IOException;
import java.io.OutputStream;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class IdentityOutputStream extends OutputStream {

    /**
     * Wrapped session output buffer.
     */
    private final SessionOutputBuffer out;

    /** True if the stream is closed. */
    private boolean closed = false;

    public IdentityOutputStream(final SessionOutputBuffer out) {
        super();
        this.out = Args.notNull(out, "Session output buffer");
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
        this.out.write(b, off, len);
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
        this.out.write(b);
    }

}
