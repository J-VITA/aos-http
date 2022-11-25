package m.vita.module.http.connect;

import java.io.IOException;

import m.vita.module.http.annotation.Immutable;
import m.vita.module.http.io.HttpTransportMetrics;
import m.vita.module.http.io.SessionOutputBuffer;
import m.vita.module.http.util.CharArrayBuffer;
import m.vita.module.http.util.Consts;

@Immutable
public class LoggingSessionOutputBuffer implements SessionOutputBuffer {

    /** Original data transmitter. */
    private final SessionOutputBuffer out;

    /** The wire log to use. */
    private final Wire wire;

    private final String charset;

    /**
     * Create an instance that wraps the specified session output buffer.
     * @param out The session output buffer.
     * @param wire The Wire log to use.
     * @param charset protocol charset, <code>ASCII</code> if <code>null</code>
     */
    public LoggingSessionOutputBuffer(
            final SessionOutputBuffer out, final Wire wire, final String charset) {
        super();
        this.out = out;
        this.wire = wire;
        this.charset = charset != null ? charset : Consts.ASCII.name();
    }

    public LoggingSessionOutputBuffer(final SessionOutputBuffer out, final Wire wire) {
        this(out, wire, null);
    }

    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b,  off,  len);
        if (this.wire.enabled()) {
            this.wire.output(b, off, len);
        }
    }

    public void write(final int b) throws IOException {
        this.out.write(b);
        if (this.wire.enabled()) {
            this.wire.output(b);
        }
    }

    public void write(final byte[] b) throws IOException {
        this.out.write(b);
        if (this.wire.enabled()) {
            this.wire.output(b);
        }
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void writeLine(final CharArrayBuffer buffer) throws IOException {
        this.out.writeLine(buffer);
        if (this.wire.enabled()) {
            final String s = new String(buffer.buffer(), 0, buffer.length());
            final String tmp = s + "\r\n";
            this.wire.output(tmp.getBytes(this.charset));
        }
    }

    public void writeLine(final String s) throws IOException {
        this.out.writeLine(s);
        if (this.wire.enabled()) {
            final String tmp = s + "\r\n";
            this.wire.output(tmp.getBytes(this.charset));
        }
    }

    public HttpTransportMetrics getMetrics() {
        return this.out.getMetrics();
    }

}
