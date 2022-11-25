package m.vita.module.http.io;

import java.io.IOException;
import java.io.InputStream;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.exception.HttpException;
import m.vita.module.http.exception.MalformedChunkCodingException;
import m.vita.module.http.exception.TruncatedChunkException;
import m.vita.module.http.header.Header;
import m.vita.module.http.util.Args;
import m.vita.module.http.util.CharArrayBuffer;

@NotThreadSafe
public class ChunkedInputStream extends InputStream {

    private static final int CHUNK_LEN               = 1;
    private static final int CHUNK_DATA              = 2;
    private static final int CHUNK_CRLF              = 3;

    private static final int BUFFER_SIZE = 2048;

    /** The session input buffer */
    private final SessionInputBuffer in;

    private final CharArrayBuffer buffer;

    private int state;

    /** The chunk size */
    private int chunkSize;

    /** The current position within the current chunk */
    private int pos;

    /** True if we've reached the end of stream */
    private boolean eof = false;

    /** True if this stream is closed */
    private boolean closed = false;

    private Header[] footers = new Header[] {};

    /**
     * Wraps session input stream and reads chunk coded input.
     *
     * @param in The session input buffer
     */
    public ChunkedInputStream(final SessionInputBuffer in) {
        super();
        this.in = Args.notNull(in, "Session input buffer");
        this.pos = 0;
        this.buffer = new CharArrayBuffer(16);
        this.state = CHUNK_LEN;
    }

    @Override
    public int available() throws IOException {
        if (this.in instanceof BufferInfo) {
            final int len = ((BufferInfo) this.in).length();
            return Math.min(len, this.chunkSize - this.pos);
        } else {
            return 0;
        }
    }

    /**
     * <p> Returns all the data in a chunked stream in coalesced form. A chunk
     * is followed by a CRLF. The method returns -1 as soon as a chunksize of 0
     * is detected.</p>
     *
     * <p> Trailer headers are read automatically at the end of the stream and
     * can be obtained with the getResponseFooters() method.</p>
     *
     * @return -1 of the end of the stream has been reached or the next data
     * byte
     * @throws IOException in case of an I/O error
     */
    @Override
    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        }
        if (this.eof) {
            return -1;
        }
        if (state != CHUNK_DATA) {
            nextChunk();
            if (this.eof) {
                return -1;
            }
        }
        final int b = in.read();
        if (b != -1) {
            pos++;
            if (pos >= chunkSize) {
                state = CHUNK_CRLF;
            }
        }
        return b;
    }

    /**
     * Read some bytes from the stream.
     * @param b The byte array that will hold the contents from the stream.
     * @param off The offset into the byte array at which bytes will start to be
     * placed.
     * @param len the maximum number of bytes that can be returned.
     * @return The number of bytes returned or -1 if the end of stream has been
     * reached.
     * @throws IOException in case of an I/O error
     */
    @Override
    public int read (final byte[] b, final int off, final int len) throws IOException {

        if (closed) {
            throw new IOException("Attempted read from closed stream.");
        }

        if (eof) {
            return -1;
        }
        if (state != CHUNK_DATA) {
            nextChunk();
            if (eof) {
                return -1;
            }
        }
        final int bytesRead = in.read(b, off, Math.min(len, chunkSize - pos));
        if (bytesRead != -1) {
            pos += bytesRead;
            if (pos >= chunkSize) {
                state = CHUNK_CRLF;
            }
            return bytesRead;
        } else {
            eof = true;
            throw new TruncatedChunkException("Truncated chunk "
                    + "( expected size: " + chunkSize
                    + "; actual size: " + pos + ")");
        }
    }

    /**
     * Read some bytes from the stream.
     * @param b The byte array that will hold the contents from the stream.
     * @return The number of bytes returned or -1 if the end of stream has been
     * reached.
     * @throws IOException in case of an I/O error
     */
    @Override
    public int read (final byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Read the next chunk.
     * @throws IOException in case of an I/O error
     */
    private void nextChunk() throws IOException {
        chunkSize = getChunkSize();
        if (chunkSize < 0) {
            throw new MalformedChunkCodingException("Negative chunk size");
        }
        state = CHUNK_DATA;
        pos = 0;
        if (chunkSize == 0) {
            eof = true;
            parseTrailerHeaders();
        }
    }

    /**
     * Expects the stream to start with a chunksize in hex with optional
     * comments after a semicolon. The line must end with a CRLF: "a3; some
     * comment\r\n" Positions the stream at the start of the next line.
     */
    private int getChunkSize() throws IOException {
        final int st = this.state;
        switch (st) {
            case CHUNK_CRLF:
                this.buffer.clear();
                final int bytesRead1 = this.in.readLine(this.buffer);
                if (bytesRead1 == -1) {
                    return 0;
                }
                if (!this.buffer.isEmpty()) {
                    throw new MalformedChunkCodingException(
                            "Unexpected content at the end of chunk");
                }
                state = CHUNK_LEN;
                //$FALL-THROUGH$
            case CHUNK_LEN:
                this.buffer.clear();
                final int bytesRead2 = this.in.readLine(this.buffer);
                if (bytesRead2 == -1) {
                    return 0;
                }
                int separator = this.buffer.indexOf(';');
                if (separator < 0) {
                    separator = this.buffer.length();
                }
                try {
                    return Integer.parseInt(this.buffer.substringTrimmed(0, separator), 16);
                } catch (final NumberFormatException e) {
                    throw new MalformedChunkCodingException("Bad chunk header");
                }
            default:
                throw new IllegalStateException("Inconsistent codec state");
        }
    }

    /**
     * Reads and stores the Trailer headers.
     * @throws IOException in case of an I/O error
     */
    private void parseTrailerHeaders() throws IOException {
        try {
            this.footers = AbstractMessageParser.parseHeaders
                    (in, -1, -1, null);
        } catch (final HttpException ex) {
            final IOException ioe = new MalformedChunkCodingException("Invalid footer: "
                    + ex.getMessage());
            ioe.initCause(ex);
            throw ioe;
        }
    }

    /**
     * Upon close, this reads the remainder of the chunked message,
     * leaving the underlying socket at a position to start reading the
     * next response without scanning.
     * @throws IOException in case of an I/O error
     */
    @Override
    public void close() throws IOException {
        if (!closed) {
            try {
                if (!eof) {
                    // read and discard the remainder of the message
                    final byte buff[] = new byte[BUFFER_SIZE];
                    while (read(buff) >= 0) {
                    }
                }
            } finally {
                eof = true;
                closed = true;
            }
        }
    }

    public Header[] getFooters() {
        return this.footers.clone();
    }

}
