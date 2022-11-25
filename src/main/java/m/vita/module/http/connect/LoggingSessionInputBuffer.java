package m.vita.module.http.connect;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.io.EofSensor;
import m.vita.module.http.io.HttpTransportMetrics;
import m.vita.module.http.io.SessionInputBuffer;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.Consts;

@Immutable
public class LoggingSessionInputBuffer implements SessionInputBuffer, EofSensor {

    /** Original session input buffer. */
    private final SessionInputBuffer in;

    private final EofSensor eofSensor;

    /** The wire log to use for writing. */
    private final Wire wire;

    private final String charset;

    /**
     * Create an instance that wraps the specified session input buffer.
     * @param in The session input buffer.
     * @param wire The wire log to use.
     * @param charset protocol charset, <code>ASCII</code> if <code>null</code>
     */
    public LoggingSessionInputBuffer(
            final SessionInputBuffer in, final Wire wire, final String charset) {
        super();
        this.in = in;
        this.eofSensor = in instanceof EofSensor ? (EofSensor) in : null;
        this.wire = wire;
        this.charset = charset != null ? charset : Consts.ASCII.name();
    }

    public LoggingSessionInputBuffer(final SessionInputBuffer in, final Wire wire) {
        this(in, wire, null);
    }

    public boolean isDataAvailable(final int timeout) throws IOException {
        return this.in.isDataAvailable(timeout);
    }

    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int l = this.in.read(b,  off,  len);
        if (this.wire.enabled() && l > 0) {
            this.wire.input(b, off, l);
        }
        return l;
    }

    public int read() throws IOException {
        final int l = this.in.read();
        if (this.wire.enabled() && l != -1) {
            this.wire.input(l);
        }
        return l;
    }

    public int read(final byte[] b) throws IOException {
        final int l = this.in.read(b);
        if (this.wire.enabled() && l > 0) {
            this.wire.input(b, 0, l);
        }
        return l;
    }

    public String readLine() throws IOException {
        final String s = this.in.readLine();
        if (this.wire.enabled() && s != null) {
            final String tmp = s + "\r\n";
            this.wire.input(tmp.getBytes(this.charset));
        }
        return s;
    }

    public int readLine(final CharArrayBuffer buffer) throws IOException {
        final int l = this.in.readLine(buffer);
        if (this.wire.enabled() && l >= 0) {
            final int pos = buffer.length() - l;
            final String s = new String(buffer.buffer(), pos, l);
            final String tmp = s + "\r\n";
            this.wire.input(tmp.getBytes(this.charset));
        }
        return l;
    }

    public HttpTransportMetrics getMetrics() {
        return this.in.getMetrics();
    }

    public boolean isEof() {
        if (this.eofSensor != null) {
            return this.eofSensor.isEof();
        } else {
            return false;
        }
    }

}
