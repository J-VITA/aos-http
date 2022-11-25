package m.vita.module.http.io;

import java.io.IOException;
import java.io.OutputStream;

import m.vita.module.http.annotation.NotThreadSafe;

@NotThreadSafe
public class ChunkedOutputStream extends OutputStream {

    // ----------------------------------------------------- Instance Variables
    private final SessionOutputBuffer out;

    private final byte[] cache;

    private int cachePosition = 0;

    private boolean wroteLastChunk = false;

    /** True if the stream is closed. */
    private boolean closed = false;

    /**
     * Wraps a session output buffer and chunk-encodes the output.
     *
     * @param out The session output buffer
     * @param bufferSize The minimum chunk size (excluding last chunk)
     * @throws IOException not thrown
     *
     * @deprecated (4.3) use {@link ChunkedOutputStream#ChunkedOutputStream(int, SessionOutputBuffer)}
     */
    @Deprecated
    public ChunkedOutputStream(final SessionOutputBuffer out, final int bufferSize)
            throws IOException {
        this(bufferSize, out);
    }

    /**
     * Wraps a session output buffer and chunks the output. The default buffer
     * size of 2048 was chosen because the chunk overhead is less than 0.5%
     *
     * @param out       the output buffer to wrap
     * @throws IOException not thrown
     *
     * @deprecated (4.3) use {@link ChunkedOutputStream#ChunkedOutputStream(int, SessionOutputBuffer)}
     */
    @Deprecated
    public ChunkedOutputStream(final SessionOutputBuffer out)
            throws IOException {
        this(2048, out);
    }

    /**
     * Wraps a session output buffer and chunk-encodes the output.
     *
     * @param bufferSize The minimum chunk size (excluding last chunk)
     * @param out The session output buffer
     */
    public ChunkedOutputStream(final int bufferSize, final SessionOutputBuffer out) {
        super();
        this.cache = new byte[bufferSize];
        this.out = out;
    }

    /**
     * Writes the cache out onto the underlying stream
     */
    protected void flushCache() throws IOException {
        if (this.cachePosition > 0) {
            this.out.writeLine(Integer.toHexString(this.cachePosition));
            this.out.write(this.cache, 0, this.cachePosition);
            this.out.writeLine("");
            this.cachePosition = 0;
        }
    }

    /**
     * Writes the cache and bufferToAppend to the underlying stream
     * as one large chunk
     */
    protected void flushCacheWithAppend(final byte bufferToAppend[], final int off, final int len) throws IOException {
        this.out.writeLine(Integer.toHexString(this.cachePosition + len));
        this.out.write(this.cache, 0, this.cachePosition);
        this.out.write(bufferToAppend, off, len);
        this.out.writeLine("");
        this.cachePosition = 0;
    }

    protected void writeClosingChunk() throws IOException {
        // Write the final chunk.
        this.out.writeLine("0");
        this.out.writeLine("");
    }

    // ----------------------------------------------------------- Public Methods
    /**
     * Must be called to ensure the internal cache is flushed and the closing
     * chunk is written.
     * @throws IOException in case of an I/O error
     */
    public void finish() throws IOException {
        if (!this.wroteLastChunk) {
            flushCache();
            writeClosingChunk();
            this.wroteLastChunk = true;
        }
    }

    // -------------------------------------------- OutputStream Methods
    @Override
    public void write(final int b) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        this.cache[this.cachePosition] = (byte) b;
        this.cachePosition++;
        if (this.cachePosition == this.cache.length) {
            flushCache();
        }
    }

    /**
     * Writes the array. If the array does not fit within the buffer, it is
     * not split, but rather written out as one large chunk.
     */
    @Override
    public void write(final byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * Writes the array. If the array does not fit within the buffer, it is
     * not split, but rather written out as one large chunk.
     */
    @Override
    public void write(final byte src[], final int off, final int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        if (len >= this.cache.length - this.cachePosition) {
            flushCacheWithAppend(src, off, len);
        } else {
            System.arraycopy(src, off, cache, this.cachePosition, len);
            this.cachePosition += len;
        }
    }

    /**
     * Flushes the content buffer and the underlying stream.
     */
    @Override
    public void flush() throws IOException {
        flushCache();
        this.out.flush();
    }

    /**
     * Finishes writing to the underlying stream, but does NOT close the underlying stream.
     */
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            finish();
            this.out.flush();
        }
    }
}
