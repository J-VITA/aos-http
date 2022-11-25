package m.vita.module.http.client.impl.io;

import java.io.IOException;
import java.net.Socket;

import m.vita.module.http.annotation.NotThreadSafe;
import m.vita.module.http.header.HttpParams;
import m.vita.module.http.io.EofSensor;
import m.vita.module.http.util.Args;

@NotThreadSafe
public class SocketInputBuffer extends AbstractSessionInputBuffer implements EofSensor {

    private final Socket socket;

    private boolean eof;

    /**
     * Creates an instance of this class.
     *
     * @param socket the socket to read data from.
     * @param buffersize the size of the internal buffer. If this number is less
     *   than <code>0</code> it is set to the value of
     *   {@link Socket#getReceiveBufferSize()}. If resultant number is less
     *   than <code>1024</code> it is set to <code>1024</code>.
     * @param params HTTP parameters.
     */
    public SocketInputBuffer(
            final Socket socket,
            final int buffersize,
            final HttpParams params) throws IOException {
        super();
        Args.notNull(socket, "Socket");
        this.socket = socket;
        this.eof = false;
        int n = buffersize;
        if (n < 0) {
            n = socket.getReceiveBufferSize();
        }
        if (n < 1024) {
            n = 1024;
        }
        init(socket.getInputStream(), n, params);
    }

    @Override
    protected int fillBuffer() throws IOException {
        final int i = super.fillBuffer();
        this.eof = i == -1;
        return i;
    }

    public boolean isDataAvailable(final int timeout) throws IOException {
        boolean result = hasBufferedData();
        if (!result) {
            final int oldtimeout = this.socket.getSoTimeout();
            try {
                this.socket.setSoTimeout(timeout);
                fillBuffer();
                result = hasBufferedData();
            } finally {
                socket.setSoTimeout(oldtimeout);
            }
        }
        return result;
    }

    public boolean isEof() {
        return this.eof;
    }

}
