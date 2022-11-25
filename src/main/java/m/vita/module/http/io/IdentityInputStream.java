package m.vita.module.http.io;

import java.io.IOException;
import java.io.InputStream;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class IdentityInputStream extends InputStream {

    private final SessionInputBuffer in;

    private boolean closed = false;

    /**
     * Wraps session input stream and reads input until the the end of stream.
     *
     * @param in The session input buffer
     */
    public IdentityInputStream(final SessionInputBuffer in) {
        super();
        this.in = Args.notNull(in, "Session input buffer");
    }

    @Override
    public int available() throws IOException {
        if (this.in instanceof BufferInfo) {
            return ((BufferInfo) this.in).length();
        } else {
            return 0;
        }
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }

    @Override
    public int read() throws IOException {
        if (this.closed) {
            return -1;
        } else {
            return this.in.read();
        }
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) {
            return -1;
        } else {
            return this.in.read(b, off, len);
        }
    }

}
